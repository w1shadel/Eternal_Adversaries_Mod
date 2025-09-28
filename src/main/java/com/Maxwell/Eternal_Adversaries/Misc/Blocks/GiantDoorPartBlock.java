package com.Maxwell.Eternal_Adversaries.Misc.Blocks;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.GiantDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GiantDoorPartBlock extends BaseEntityBlock {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    protected static final VoxelShape EMPTY_SHAPE = Shapes.empty();
    protected static final VoxelShape FULL_CUBE_SHAPE = Shapes.block();

    public GiantDoorPartBlock(Properties pProperties) { // BlockSetTypeは不要なので削除
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(OPEN);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return pState.getValue(OPEN) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(OPEN) ? EMPTY_SHAPE : FULL_CUBE_SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return FULL_CUBE_SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.getValue(OPEN) ? EMPTY_SHAPE : FULL_CUBE_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GiantDoorBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof GiantDoorBlockEntity partBE) {
            GiantDoorBlockEntity masterBE = partBE.getMaster();
            if (masterBE != null) {
                // マスターに処理を丸投げ
                return masterBE.onRightClick(pPlayer, pHand);
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof GiantDoorBlockEntity partBE) {
            GiantDoorBlockEntity masterBE = partBE.getMaster();

            if (masterBE != null && masterBE.isOpen()) {
                // ★★★ 破壊フェーズと自分の階層が一致するか？ ★★★
                if (masterBE.getDestructionPhase() != partBE.getTier()) {
                    // 一致しないので、破壊不可能
                    return 0.0F;
                }
            }
        }
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        // 新しいブロックが同じ種類でない場合（＝実際に破壊された場合）のみ、処理を続行
        if (!pState.is(pNewState.getBlock())) {

            // 私たちのカスタムロジックは、サーバーサイドでのみ実行する
            if (!pLevel.isClientSide()) {
                if (pLevel.getBlockEntity(pPos) instanceof GiantDoorBlockEntity partBE) {
                    GiantDoorBlockEntity masterBE = partBE.getMaster();
                    if (masterBE != null && !masterBE.isBeingDestroyed()) {

                        // --- 状態に応じた破壊ロジックの分岐 ---
                        if (masterBE.isOpen()) {
                            // --- ケース1：ドアが開いている場合 ---
                            // 自分の下に、同じドアのパーツがあるか？
                            BlockState belowState = pLevel.getBlockState(pPos.below());
                            boolean hasPartBelow = belowState.is(this);

                            if (!hasPartBelow) {
                                // このブロックは最下段なので、この列の破壊を許可する
                                masterBE.destroyColumn(pPos);
                            }
                            // 最下段でなければ、何もしない（破壊は実質キャンセルされる）

                        } else {
                            // --- ケース2：ドアが閉じている場合 ---
                            // 今まで通り、ドア全体を一度に破壊する
                            masterBE.destroyDoor();
                        }
                    }
                }
            }

            // ブロックエンティティの削除など、バニラのクリーンアップ処理を呼び出す
            // この呼び出しは、if (!pState.is(pNewState.getBlock())) の内側で行うのが安全
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }
}