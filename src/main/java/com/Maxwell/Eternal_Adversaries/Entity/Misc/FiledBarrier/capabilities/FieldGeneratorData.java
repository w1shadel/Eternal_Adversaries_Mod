package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;

import java.util.HashSet;
import java.util.Set;

public class FieldGeneratorData implements IFieldGeneratorData {

    // ジェネレーターの座標を重複なく保持するため、HashSetを使用
    private final Set<BlockPos> activeGenerators = new HashSet<>();

    @Override
    public Set<BlockPos> getActiveGenerators() {
        return this.activeGenerators;
    }

    @Override
    public void addGenerator(BlockPos pos) {
        this.activeGenerators.add(pos);
        System.out.println("[DEBUG-3] Adding generator at " + pos.toString() + " to the list."); // 検問所3
    }

    @Override
    public void removeGenerator(BlockPos pos) {
        this.activeGenerators.remove(pos);
    }

    @Override
    public void clear() {
        this.activeGenerators.clear();
    }

    /**
     * ★★★ このデータをNBT（セーブデータ）形式に変換するメソッド ★★★
     */
    public CompoundTag serializeNBT() {
        // ★★★ ここに検問所を設置 ★★★
        System.out.println("[DEBUG-SAVE] Firing serializeNBT. Current list size is: " + this.activeGenerators.size());

        CompoundTag nbt = new CompoundTag();
        ListTag listTag = new ListTag();

        this.activeGenerators.forEach(pos -> {
            System.out.println("[DEBUG-SAVE]   - Saving position: " + pos.toString());
            listTag.add(NbtUtils.writeBlockPos(pos));
        });

        nbt.put("generators", listTag);
        return nbt;
    }

    /**
     * ★★★ NBT（セーブデータ）からデータを復元するメソッド ★★★
     */
    public void deserializeNBT(CompoundTag nbt) {
        // ★★★ こちらにも検問所を設置 ★★★
        System.out.println("[DEBUG-LOAD] Firing deserializeNBT.");

        this.activeGenerators.clear();

        if (nbt.contains("generators", 9)) {
            ListTag listTag = nbt.getList("generators", 10);

            System.out.println("[DEBUG-LOAD]   - Found " + listTag.size() + " generators in NBT.");
            listTag.forEach(tag -> {
                BlockPos pos = NbtUtils.readBlockPos((CompoundTag) tag);
                System.out.println("[DEBUG-LOAD]   - Loading position: " + pos.toString());
                this.activeGenerators.add(pos);
            });
        } else {
            System.out.println("[DEBUG-LOAD]   - No 'generators' tag found in NBT.");
        }
    }
}