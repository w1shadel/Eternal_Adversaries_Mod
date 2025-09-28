package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier;

import com.Maxwell.Eternal_Adversaries.Register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FieldGeneratorBlockEntity extends BlockEntity {

    private boolean isActive = true; // デフォルトではフィールドは有効

    // ★★★ 保護範囲を定義 ★★★
    // 例：第一範囲を10x10x10, 第二範囲を20x20x20とする
    @Nullable // nullの可能性があるのでアノテーションを付ける
    private BlockPos pos1;
    @Nullable
    private BlockPos pos2;

    public FieldGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FILED_BARRIER.get(), pPos, pBlockState);
    }

    // --- 状態管理 ---
    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        if (this.isActive != active) {
            this.isActive = active;
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }

            // マネージャーのリストも更新
            if(this.isActive) {
                FieldGeneratorManager.addGenerator(this.worldPosition);
            } else {
                FieldGeneratorManager.removeGenerator(this.worldPosition);
            }
        }
    }
    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        FieldGeneratorManager.removeGenerator(this.worldPosition);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        FieldGeneratorManager.removeGenerator(this.worldPosition);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.isActive) {
            FieldGeneratorManager.addGenerator(this.worldPosition);
        }
    }
    public AABB getProtectionBox() {
        if (this.pos1 != null && this.pos2 != null) {
            // 2つの対角座標からAABBを生成
            return new AABB(this.pos1, this.pos2);
        }
        // 範囲が設定されていない場合は、何もない範囲を返す
        return AABB.ofSize(Vec3.ZERO, 0, 0, 0);
    }

    public void setRange(BlockPos pos1, BlockPos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.setActive(true); // 範囲が設定されたので、フィールドを有効化

        // ★★★ setChangedAndSync() の代わりに、ここでも直接呼び出す ★★★
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    // --- データ保存/読み込み ---
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putBoolean("is_active", this.isActive);
        if (this.pos1 != null) {
            pTag.put("pos1", NbtUtils.writeBlockPos(this.pos1));
        }
        if (this.pos2 != null) {
            pTag.put("pos2", NbtUtils.writeBlockPos(this.pos2));
        }
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.isActive = pTag.getBoolean("is_active");
        if (pTag.contains("pos1")) {
            this.pos1 = NbtUtils.readBlockPos(pTag.getCompound("pos1"));
        }
        if (pTag.contains("pos2")) {
            this.pos2 = NbtUtils.readBlockPos(pTag.getCompound("pos2"));
        }
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    // ... getUpdateTag と getUpdatePacket も実装
}