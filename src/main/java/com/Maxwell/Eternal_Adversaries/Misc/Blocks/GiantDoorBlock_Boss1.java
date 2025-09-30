package com.Maxwell.Eternal_Adversaries.Misc.Blocks;

import com.Maxwell.Eternal_Adversaries.Register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class GiantDoorBlock_Boss1 extends Block {

    // --- BlockStateの定義 ---
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    // 各ブロックが、ドアの左下隅から見てどの位置にいるかを記憶するためのプロパティ
    public static final IntegerProperty OFFSET_X = IntegerProperty.create("offset_x", 0, 2); // width - 1
    public static final IntegerProperty OFFSET_Y = IntegerProperty.create("offset_y", 0, 2); // height - 1

    // --- ドアのサイズ ---
    private final int width = 3;
    private final int height = 3;

    public GiantDoorBlock_Boss1(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(OFFSET_X, 0)
                .setValue(OFFSET_Y, 0));
    }

    // --- メインロジック：プレイヤーが右クリックした時の処理 ---
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack heldItem = pPlayer.getItemInHand(pHand);

        // ★★★ 要件1：鍵はここでハードコーディング ★★★
        if (heldItem.is(ModItems.BOSS1_KEY.get())) {

            // すでに開いている場合は何もしない
            if (pState.getValue(OPEN)) {
                return InteractionResult.PASS;
            }

            // ★★★ 要件2：全てのブロックがマスターとして機能する ★★★
            // クリックされたブロックの位置情報(OFFSET)から、ドア全体のブロックを破壊する
            destroyEntireDoor(pLevel, pState, pPos);

            pLevel.playSound(null, pPos, SoundEvents.WITHER_DEATH, SoundSource.BLOCKS, 0.8f, 1.0f);
            return InteractionResult.SUCCESS;

        } else {
            // 鍵が違う、または何も持っていない場合
            pLevel.playSound(null, pPos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 0.5f);
            return InteractionResult.FAIL;
        }
    }

    // --- 設置と破壊のロジック ---

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (pLevel.isClientSide) return;

        Direction facing = pState.getValue(FACING);

        // pPosをドアの左下隅(offsetX=0, offsetY=0)として、他のパーツを設置する
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BlockPos currentPos = pPos.relative(facing.getCounterClockWise(), x).above(y);

                // 設置する各ブロックに、自身の位置情報をBlockStateとして書き込む
                BlockState partState = this.defaultBlockState()
                        .setValue(FACING, facing)
                        .setValue(OFFSET_X, x)
                        .setValue(OFFSET_Y, y);

                // 既にブロックがある場所（pPos）以外に設置する
                if (!currentPos.equals(pPos)) {
                    pLevel.setBlock(currentPos, partState, 3);
                } else {
                    // pPosにも自身の位置情報を設定
                    pLevel.setBlock(pPos, partState, 3);
                }
            }
        }
    }

    // ★★★ 要件3：1つでも破壊されたら、全体が連鎖的に破壊される ★★★
    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide && !pState.getValue(OPEN)) { // 開いているときは何もしない
            // 破壊されたブロックから、ドア全体を破壊する
            destroyEntireDoor(pLevel, pState, pPos);
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    /**
     * 指定されたブロックを含むドア全体を破壊（空気ブロックに置換）するヘルパーメソッド
     */
    private void destroyEntireDoor(Level level, BlockState state, BlockPos pos) {
        int offsetX = state.getValue(OFFSET_X);
        int offsetY = state.getValue(OFFSET_Y);
        Direction facing = state.getValue(FACING);

        // 破壊されたブロックの位置情報(OFFSET)から、ドアの左下隅の座標を逆算する
        BlockPos bottomLeftPos = pos.relative(facing.getClockWise(), offsetX).below(offsetY);

        // 左下隅からドア全体のブロックをループし、全て空気ブロックに置き換える
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BlockPos currentPos = bottomLeftPos.relative(facing.getCounterClockWise(), x).above(y);
                // 安全のため、そこにあるブロックが本当にこのドアの一部か確認する
                if (level.getBlockState(currentPos).is(this)) {
                    level.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 35);
                }
            }
        }
    }

    // --- BlockStateの定義 ---
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OPEN, OFFSET_X, OFFSET_Y);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}