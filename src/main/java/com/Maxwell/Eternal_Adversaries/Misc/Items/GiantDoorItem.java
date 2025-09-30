package com.Maxwell.Eternal_Adversaries.Misc.Items;

import com.Maxwell.Eternal_Adversaries.Misc.Blocks.GiantDoorBlock_Boss2;
import com.Maxwell.Eternal_Adversaries.Register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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
        // サーバーサイドでのみ処理を行う
        if (pContext.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Level level = pContext.getLevel();
        BlockPos basePos = pContext.getClickedPos().relative(pContext.getClickedFace());
        Direction facing = pContext.getHorizontalDirection(); // プレイヤーの水平な向き

        // ドアを設置できるかチェックする
        if (!canPlaceDoor(level, basePos, facing)) {
            // (任意) プレイヤーに設置できないことを伝えるチャットメッセージなど
            return InteractionResult.FAIL;
        }

        // ドアを設置する
        placeDoor(level, basePos, facing);

        // アイテムを1つ消費する
//        pContext.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }

    /**
     * 指定された範囲にドアブロックを設置するだけのシンプルなメソッド
     */
    private void placeDoor(Level level, BlockPos basePos, Direction facing) {
        // 全てのドアブロックで向きを統一するためのBlockStateを作成
        BlockState doorState = ModBlocks.GIANT_DOOR.get().defaultBlockState()
                .setValue(GiantDoorBlock_Boss2.FACING, facing);

        // 指定された幅と高さでループ処理
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                // ドアの左下隅を基準に、各パーツの位置を計算する
                BlockPos currentPos = basePos.above(y).relative(facing.getCounterClockWise(), x);
                level.setBlock(currentPos, doorState, 3);
            }
        }
        // これで設置は完了。BlockEntityのセットアップは一切不要。
        // 全てのブロックは「マスターが未設定」の状態でワールドに配置される。
    }

    /**
     * ドアを設置するのに十分なスペースがあるかを確認するメソッド
     */
    private boolean canPlaceDoor(Level level, BlockPos basePos, Direction facing) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                BlockPos currentPos = basePos.above(y).relative(facing.getCounterClockWise(), x);
                // 設置先に空気や草など、上書き可能なブロック以外が存在しないかチェック
                if (!level.getBlockState(currentPos).canBeReplaced()) {
                    return false;
                }
            }
        }
        return true;
    }
}