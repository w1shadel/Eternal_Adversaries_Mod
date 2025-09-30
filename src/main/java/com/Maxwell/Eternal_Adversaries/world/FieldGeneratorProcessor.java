package com.Maxwell.Eternal_Adversaries.world;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.FieldGeneratorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Register.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;


public class FieldGeneratorProcessor extends StructureProcessor {
    public static final FieldGeneratorProcessor INSTANCE = new FieldGeneratorProcessor();
    public static final Codec<FieldGeneratorProcessor> CODEC = Codec.unit(() -> INSTANCE);

    // NBTに書き込むキーを定義
    public static final String MARKER_TAG = "field_generator_marker";
    public static final String POS1_OFFSET_TAG = "pos1_offset";
    public static final String POS2_OFFSET_TAG = "pos2_offset";

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader pLevel, BlockPos pLocalPos, BlockPos pGlobalPos, StructureTemplate.StructureBlockInfo pBlockInfo, StructureTemplate.StructureBlockInfo pRelativeBlockInfo, StructurePlaceSettings pSettings) {

        // ワールド生成時(サーバーサイド)のみ動作
        if (pLevel instanceof ServerLevel serverLevel) {
            // 今回はマーカーとしてコマンドブロックを使う例
            if (pRelativeBlockInfo.state().getBlock() instanceof CommandBlock) {
                CompoundTag nbt = pRelativeBlockInfo.nbt();
                // NBTに我々のマーカータグがあるかチェック
                if (nbt != null && nbt.getBoolean(MARKER_TAG)) {

                    // 1. マーカーを本物のFieldGeneratorBlockに置き換える
                    BlockState generatorState = ModBlocks.FILED_BARRIER.get().defaultBlockState();
                    serverLevel.setBlock(pGlobalPos, generatorState, 3);

                    // 2. 設置したばかりのBlockEntityを取得
                    BlockEntity be = serverLevel.getBlockEntity(pGlobalPos);
                    if (be instanceof FieldGeneratorBlockEntity generatorBE) {

                        // 3. マーカーに保存された相対座標を読み込む
                        if (nbt.contains(POS1_OFFSET_TAG) && nbt.contains(POS2_OFFSET_TAG)) {
                            BlockPos pos1Offset = NbtUtils.readBlockPos(nbt.getCompound(POS1_OFFSET_TAG));
                            BlockPos pos2Offset = NbtUtils.readBlockPos(nbt.getCompound(POS2_OFFSET_TAG));

                            // 4. グローバル座標に変換
                            BlockPos globalPos1 = pGlobalPos.offset(pos1Offset);
                            BlockPos globalPos2 = pGlobalPos.offset(pos2Offset);

                            // 5. BlockEntityに範囲を設定し、有効化する
                            System.out.println("Activating protection field for dungeon at " + pGlobalPos);
                            generatorBE.setRange(globalPos1, globalPos2);
                        }
                    }

                    // 6. マーカーとして使ったコマンドブロックは消す
                    return new StructureTemplate.StructureBlockInfo(pGlobalPos, Blocks.AIR.defaultBlockState(), null);
                }
            }
        }
        return pRelativeBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.FIELD_GENERATOR_PROCESSOR.get(); // 登録したProcessorタイプを返す
    }
}