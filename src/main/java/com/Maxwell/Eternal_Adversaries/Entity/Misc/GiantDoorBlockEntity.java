package com.Maxwell.Eternal_Adversaries.Entity.Misc;

import com.Maxwell.Eternal_Adversaries.Register.ModBlockEntities;
import com.Maxwell.Eternal_Adversaries.Register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.Maxwell.Eternal_Adversaries.Misc.Blocks.GiantDoorPartBlock.OPEN;


public class GiantDoorBlockEntity extends BlockEntity {

    private boolean isOpen = false;
    private ItemStack keyStack = ItemStack.EMPTY;
    private boolean isMaster = false;
    private BlockPos masterPos;
    private List<BlockPos> doorParts = new ArrayList<>();
    private boolean isBeingDestroyed = false; // 破壊の連鎖を防ぐフラグ

    public GiantDoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GIANT_DOOR.get(), pPos, pBlockState);
        this.masterPos = pPos;
    }
    private int tier = -1; // 自分がどの段か (0=下, 1=中, 2=上)
    private int destructionPhase = 0; // マスターのみ使用 (現在破壊可能な段)
    // --- ゲッター / セッター ---
    public boolean isOpen() {
        return isOpen;
    }
    public void setMaster(BlockPos pos) {
        this.isMaster = false;
        this.masterPos = pos;
        setChanged();
    }
    public void setAsMaster() {
        this.isMaster = true;
        this.masterPos = this.worldPosition;
        setChanged();
    }
    public void setDoorStructure(List<BlockPos> parts) {
        if (this.isMaster) {
            this.doorParts = new ArrayList<>(parts);
            setChanged();
        }
    }
    public boolean isBeingDestroyed() {
        return isBeingDestroyed;
    }
    public int getTier() { return this.tier; }
    public void setTier(int tier) {
        this.tier = tier;
        setChanged();
    }
    public int getDestructionPhase() {
        GiantDoorBlockEntity master = getMaster();
        return master != null ? master.destructionPhase : 0;
    }

    // --- メインロジック ---
    public GiantDoorBlockEntity getMaster() {
        if (this.isMaster) return this;
        if (level != null && level.getBlockEntity(this.masterPos) instanceof GiantDoorBlockEntity master) {
            return master;
        }
        // マスターが見つからない = 孤立したパーツ。安全のため自身を空気にする
        if (level != null && !level.isClientSide) {
            level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
        }
        return null;
    }

    public InteractionResult onRightClick(Player player, InteractionHand hand) {
        if (!this.isMaster || this.level == null) return InteractionResult.FAIL;

        ItemStack heldItem = player.getItemInHand(hand);

        // 1. 鍵の設定
        if (this.keyStack.isEmpty()) {
            if (!heldItem.isEmpty()) {
                this.keyStack = heldItem.copy();
                this.keyStack.setCount(1);
                this.level.playSound(null, this.worldPosition, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 1.5f);
                setChangedAndSync();
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        // 2. 鍵の照合と開閉
        if (ItemStack.isSameItemSameTags(heldItem, this.keyStack)) {
            this.isOpen = !this.isOpen;
            this.level.playSound(null, this.worldPosition, this.isOpen ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
            updateDoorState(this.isOpen); // BlockStateを更新
            setChangedAndSync();
            return InteractionResult.SUCCESS;
        } else {
            this.level.playSound(null, this.worldPosition, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 0.5f);
            return InteractionResult.FAIL;
        }
    }

    public void destroyColumn(BlockPos bottomPos) {
        if (this.level == null || !this.isMaster || this.isBeingDestroyed) return;
        this.isBeingDestroyed = true;

        // X座標とZ座標が同じパーツ（＝同じ列）を探し出して破壊する
        for (BlockPos partPos : new ArrayList<>(this.doorParts)) { // ConcurrentModificationExceptionを防ぐためコピーを走査
            if (partPos.getX() == bottomPos.getX() && partPos.getZ() == bottomPos.getZ()) {
                if (level.getBlockState(partPos).is(ModBlocks.GIANT_DOOR.get())) {
                    level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 3);
                    // 全体のパーツリストからも削除する
                    this.doorParts.remove(partPos);
                }
            }
        }

        // もし全てのパーツがなくなったら、このブロックエンティティも消えるべき
        // (onRemoveがよしなにやってくれるので、通常は追加処理不要)

        setChangedAndSync();
        this.isBeingDestroyed = false; // 次の破壊に備える
    }

    private void updateDoorState(boolean isOpen) {
        if (this.level == null || !this.isMaster) return;
        for (BlockPos partPos : this.doorParts) {
            BlockState currentState = this.level.getBlockState(partPos);
            if (currentState.is(ModBlocks.GIANT_DOOR.get())) { // GIANT_DOOR_PART に修正
                this.level.setBlock(partPos, currentState.setValue(OPEN, isOpen), 3);
            }
        }
    }

    public void destroyDoor() {
        if (this.level == null || !this.isMaster || this.isBeingDestroyed) return;
        this.isBeingDestroyed = true;
        for (BlockPos partPos : this.doorParts) {
            if (level.getBlockState(partPos).is(ModBlocks.GIANT_DOOR.get())) {
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }
    public void checkForPhaseUpdate() {
        if (level == null || !isMaster) return;

        // 現在のフェーズのパーツが全て残っているかチェック
        boolean allDestroyed = true;
        for (BlockPos partPos : this.doorParts) {
            if (level.getBlockEntity(partPos) instanceof GiantDoorBlockEntity be) {
                if (be.getTier() == this.destructionPhase) {
                    allDestroyed = false; // まだ残っているパーツがあった
                    break;
                }
            }
        }

        if (allDestroyed) {
            // 全て破壊されたので、次のフェーズに進む
            this.destructionPhase++;
            // ここで効果音などを鳴らすと良い
            setChangedAndSync();
        }
    }
    public void destroyPart(BlockPos partPos) {
        if (level == null || !isMaster || isBeingDestroyed) return;
        isBeingDestroyed = true;

        if (level.getBlockState(partPos).is(ModBlocks.GIANT_DOOR.get())) {
            level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 3);
            this.doorParts.remove(partPos);

            // 破壊後にフェーズが更新されるかチェック
            checkForPhaseUpdate();
        }

        isBeingDestroyed = false;
    }
    // --- データ関連 ---
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("is_open", this.isOpen);
        if (!this.keyStack.isEmpty()) pTag.put("key", this.keyStack.save(new CompoundTag()));
        pTag.putBoolean("is_master", this.isMaster);
        pTag.put("master_pos", NbtUtils.writeBlockPos(this.masterPos));
        if (this.isMaster && !this.doorParts.isEmpty()) {
            ListTag listTag = new ListTag();
            this.doorParts.forEach(pos -> listTag.add(NbtUtils.writeBlockPos(pos)));
            pTag.put("DoorParts", listTag);
        }
        pTag.putInt("tier", this.tier);
        if (isMaster) {
            pTag.putInt("destruction_phase", this.destructionPhase);
        }
    }
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.isOpen = pTag.getBoolean("is_open");
        this.keyStack = pTag.contains("key") ? ItemStack.of(pTag.getCompound("key")) : ItemStack.EMPTY;
        this.isMaster = pTag.getBoolean("is_master");
        this.masterPos = NbtUtils.readBlockPos(pTag.getCompound("master_pos"));
        if (pTag.contains("DoorParts")) {
            ListTag listTag = pTag.getList("DoorParts", 10);
            this.doorParts.clear();
            listTag.forEach(tag -> this.doorParts.add(NbtUtils.readBlockPos((CompoundTag) tag)));
        }
        this.tier = pTag.getInt("tier");
        if (isMaster) {
            this.destructionPhase = pTag.getInt("destruction_phase");
        }
    }

    @Override
    public CompoundTag getUpdateTag() { return this.saveWithoutMetadata(); }
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    private void setChangedAndSync() {
        setChanged();
        if(level != null && !level.isClientSide) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }


}