package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier;

import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class FieldGeneratorManager {
    // ★★★ ワールドに存在する全てのアクティブなジェネレーターの位置を保持するセット ★★★
    private static final Set<BlockPos> ACTIVE_GENERATORS = new HashSet<>();

    public static void addGenerator(BlockPos pos) {
        ACTIVE_GENERATORS.add(pos);
    }

    public static void removeGenerator(BlockPos pos) {
        ACTIVE_GENERATORS.remove(pos);
    }

    public static Stream<BlockPos> getActiveGenerators() {
        return ACTIVE_GENERATORS.stream();
    }
}