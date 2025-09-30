package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities;

import net.minecraft.core.BlockPos;

import java.util.Set;

public interface IFieldGeneratorData {
    /**
     * 現在アクティブな全ジェネレーターの座標セットを取得します。
     * @return 座標のSet
     */
    Set<BlockPos> getActiveGenerators();

    /**
     * 新しいアクティブなジェネレーターをリストに追加します。
     * @param pos 追加するジェネレーターの座標
     */
    void addGenerator(BlockPos pos);

    /**
     * ジェネレーターをリストから削除します。
     * @param pos 削除するジェネレーターの座標
     */
    void removeGenerator(BlockPos pos);

    /**
     * リストを空にします。
     */
    void clear();
}