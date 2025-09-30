package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashSet;
import java.util.Set;

public class FieldGeneratorSavedData extends SavedData {

    // このMODのデータを識別するための名前
    private static final String DATA_NAME = Eternal_Adversaries.MODID + "_field_generators";

    private final Set<BlockPos> activeGenerators = new HashSet<>();

    // --- データの読み書き ---

    /**
     * NBTからデータを読み込むためのコンストラクタ
     */
    public FieldGeneratorSavedData(CompoundTag nbt) {
        ListTag listTag = nbt.getList("generators", 10);
        listTag.forEach(tag -> this.activeGenerators.add(NbtUtils.readBlockPos((CompoundTag) tag)));
    }

    /**
     * NBTにデータを保存するためのメソッド
     */
    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag listTag = new ListTag();
        this.activeGenerators.forEach(pos -> listTag.add(NbtUtils.writeBlockPos(pos)));
        pCompoundTag.put("generators", listTag);
        System.out.println("DEBUG-3 "  + listTag);
        return pCompoundTag;
    }

    // --- リスト操作メソッド ---
    public void addGenerator(BlockPos pos) {
        this.activeGenerators.add(pos);
        setDirty(); // データが変更されたことをマーク
    }

    public void removeGenerator(BlockPos pos) {
        this.activeGenerators.remove(pos);
        setDirty();
    }

    public Set<BlockPos> getActiveGenerators() {
        return this.activeGenerators;
    }

    // ★★★ これが、ワールドからこのデータを取得するための最も重要なメソッドです ★★★
    public static FieldGeneratorSavedData get(ServerLevel level) {
        // ワールドのデータストレージを取得
        DimensionDataStorage storage = level.getDataStorage();

        // "eternal_adversaries_field_generators" という名前のデータを要求する
        // もしデータが存在すればそれをロードし、存在しなければ新しいインスタンスを作成する
        return storage.computeIfAbsent(
                FieldGeneratorSavedData::new,      // NBTからロードするためのコンストラクタ
                FieldGeneratorSavedData::new,      // 新規作成するためのコンストラクタ
                DATA_NAME
        );
    }

    // 新規作成用の空のコンストラクタ
    public FieldGeneratorSavedData() {}
}