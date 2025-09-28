package com.Maxwell.Eternal_Adversaries.Misc.Items;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.GiantDoorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class GiantDoorItem extends Item {
    private final int width;
    private final int height;

    public GiantDoorItem(Properties pProperties, int width, int height) {
        super(pProperties);
        this.width = width;
        this.height = height;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos().relative(pContext.getClickedFace());
        Direction facing = pContext.getHorizontalDirection();

        // 3x3 (または指定されたサイズ) の設置スペースがあるかチェック
        if (!canPlaceDoor(level, pos, facing)) {
            return InteractionResult.FAIL;
        }

        // ドアを設置
        placeDoor(level, pos, facing);

        // アイテムを消費
        pContext.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }

    private void placeDoor(Level level, BlockPos basePos, Direction facing) {
        // ★★★ マスターを中央に設定 ★★★
        BlockPos masterPos = basePos.above(height / 2).relative(facing.getCounterClockWise(), width / 2);

        List<BlockPos> doorPartPositions = new ArrayList<>();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                BlockPos currentPos = basePos.above(y).relative(facing.getCounterClockWise(), x);
                doorPartPositions.add(currentPos);
            }
        }

        for (BlockPos currentPos : doorPartPositions) {
            level.setBlock(currentPos, ModBlocks.GIANT_DOOR.get().defaultBlockState(), 3);

            if (level.getBlockEntity(currentPos) instanceof GiantDoorBlockEntity be) {
                if (currentPos.equals(masterPos)) {
                    be.setAsMaster();
                } else {
                    be.setMaster(masterPos);
                }

                // ★★★ 階層（Tier）を設定 ★★★
                // basePosからの相対的なY座標で判断する (0=下, 1=中, 2=上)
                int tier = currentPos.getY() - basePos.getY();
                be.setTier(tier);
            }
        }

        if (level.getBlockEntity(masterPos) instanceof GiantDoorBlockEntity masterBE) {
            masterBE.setDoorStructure(doorPartPositions);
        }
    }

    private boolean canPlaceDoor(Level level, BlockPos basePos, Direction facing) {

        return true;
    }
}