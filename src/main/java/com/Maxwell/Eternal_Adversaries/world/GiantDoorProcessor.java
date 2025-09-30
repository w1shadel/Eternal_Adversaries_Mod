package com.Maxwell.Eternal_Adversaries.world;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.Maxwell.Eternal_Adversaries.Register.ModBlocks;
import com.Maxwell.Eternal_Adversaries.Register.ModItems;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GiantDoorProcessor extends StructureProcessor {
    public static final GiantDoorProcessor INSTANCE = new GiantDoorProcessor();
    public static final Codec<GiantDoorProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public GiantDoorProcessor()
    {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!! GiantDoorProcessor INSTANCE CREATED SUCCESSFULLY !!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    // ダンジョンNBT内のJigsawブロックに設定する目印
    private static final ResourceLocation MARKER_JIGSAW_NAME = new ResourceLocation(Eternal_Adversaries.MODID, "giant_door_marker");

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader pLevel, BlockPos pLocalPos, BlockPos pGlobalPos, StructureTemplate.StructureBlockInfo pBlockInfo, StructureTemplate.StructureBlockInfo pRelativeBlockInfo, StructurePlaceSettings pSettings) {
        // ==================================================================================
        System.out.println("[DOOR PROCESSOR] processBlock method was CALLED for block at " + pGlobalPos);
        // ==================================================================================

        if (pLevel instanceof ServerLevel serverLevel) {
            System.out.println("[DOOR PROCESSOR]   - It is a ServerLevel. Checking block type...");
            if (pRelativeBlockInfo.state().getBlock() instanceof JigsawBlock) {
                System.out.println("[DOOR PROCESSOR]   - Block IS a JigsawBlock. Checking NBT...");

                if (pRelativeBlockInfo.nbt() != null) {
                    System.out.println("[DOOR PROCESSOR]   - NBT exists. Jigsaw name is: " + pRelativeBlockInfo.nbt().getString("name"));

                    if (MARKER_JIGSAW_NAME.equals(new ResourceLocation(pRelativeBlockInfo.nbt().getString("name")))) {
                        System.out.println("[DOOR PROCESSOR]   - SUCCESS: Found a Giant Door Marker!");

                        ResourceLocation doorStructureRL = new ResourceLocation(pRelativeBlockInfo.nbt().getString("pool"));
                        System.out.println("[DOOR PROCESSOR]   - Attempting to load structure: " + doorStructureRL);

                        StructureTemplateManager manager = serverLevel.getStructureManager();
                        var templateOpt = manager.get(doorStructureRL);

                        if (templateOpt.isPresent()) {
                            System.out.println("[DOOR PROCESSOR]   - SUCCESS: Structure loaded successfully!");
                            StructureTemplate doorTemplate = templateOpt.get();
                            doorTemplate.placeInWorld(serverLevel, pGlobalPos, pGlobalPos, pSettings, serverLevel.getRandom(), 2);
                            System.out.println("[DOOR PROCESSOR]   - Placed door structure in world.");
                        } else {
                            System.err.println("[DOOR PROCESSOR]   - ERROR: FAILED TO LOAD STRUCTURE! Path is likely incorrect: " + doorStructureRL);
                        }
                        return new StructureTemplate.StructureBlockInfo(pRelativeBlockInfo.pos(), Blocks.AIR.defaultBlockState(), null);
                    } else {
                        System.out.println("[DOOR PROCESSOR]   - Jigsaw name did not match the marker.");
                    }
                } else {
                    System.out.println("[DOOR PROCESSOR]   - NBT was null.");
                }
            }
        }
        return pRelativeBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.LOCKED_DOOR_PROCESSOR.get(); // 登録したProcessorタイプを返す
    }
}