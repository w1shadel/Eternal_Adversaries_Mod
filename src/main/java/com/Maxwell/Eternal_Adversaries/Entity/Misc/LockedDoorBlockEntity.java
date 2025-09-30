package com.Maxwell.Eternal_Adversaries.Entity.Misc;

import com.Maxwell.Eternal_Adversaries.Register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class LockedDoorBlockEntity extends BlockEntity {

    // ★★★ このドアの「鍵」となるアイテムを保存する変数 ★★★
    private ItemStack keyStack = ItemStack.EMPTY;

    public LockedDoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.LOCKED_DOOR.get(), pPos, pBlockState);
    }

    // --- 鍵を設定・取得するためのメソッド ---
    public void setKey(ItemStack stack) {
        this.keyStack = stack.copy();
        this.keyStack.setCount(1); // 鍵は常に1つ
        setChanged(); // データが変更されたことをMinecraftに通知
    }

    public ItemStack getKey() {
        return this.keyStack;
    }

    public boolean hasKey() {
        return !this.keyStack.isEmpty();
    }

    // --- データの保存と読み込み ---
    // ワールドが保存されるときに呼ばれる
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (hasKey()) {
            pTag.put("key", this.keyStack.save(new CompoundTag()));
        }
    }

    // ワールドが読み込まれるときに呼ばれる
    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("key")) {
            this.keyStack = ItemStack.of(pTag.getCompound("key"));
        }
    }

    // --- サーバーとクライアントのデータ同期 ---
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}