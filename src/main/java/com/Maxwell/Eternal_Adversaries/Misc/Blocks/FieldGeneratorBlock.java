package com.Maxwell.Eternal_Adversaries.Misc.Blocks;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.FieldGeneratorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Misc.Items.RangeWandItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class FieldGeneratorBlock extends BaseEntityBlock {

    // ★★★ レッドストーン信号でオン/オフを切り替えるためのプロパティ ★★★
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public FieldGeneratorBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FieldGeneratorBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL; // 通常のモデルを持つ
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            ItemStack heldItem = pPlayer.getItemInHand(pHand);

            // プレイヤーが手に持っているのは、範囲設定ワンドか？
            if (heldItem.getItem() instanceof RangeWandItem) {
                if (pLevel.getBlockEntity(pPos) instanceof FieldGeneratorBlockEntity be) {
                    CompoundTag nbt = heldItem.getTag();
                    // ワンドに2つの座標が設定されているか？
                    if (nbt != null && nbt.contains("pos1") && nbt.contains("pos2")) {
                        BlockPos pos1 = NbtUtils.readBlockPos(nbt.getCompound("pos1"));
                        BlockPos pos2 = NbtUtils.readBlockPos(nbt.getCompound("pos2"));

                        // ブロックエンティティに範囲を設定
                        be.setRange(pos1, pos2);

                        pPlayer.sendSystemMessage(Component.literal("Protection field range applied!").withStyle(ChatFormatting.GOLD));
                        return InteractionResult.SUCCESS;
                    } else {
                        pPlayer.sendSystemMessage(Component.literal("Wand must have 2 positions selected.").withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
        return InteractionResult.PASS; // ワンド以外でのクリックは、何もしない
    }
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        if (!pLevel.isClientSide) {
            // このブロックがレッドストーン動力を受け取っているか？
            boolean isPowered = pLevel.hasNeighborSignal(pPos);

            // 現在の状態と、新しい状態が異なる場合のみ更新
            if (isPowered != pState.getValue(POWERED)) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, isPowered), 2);

                // ブロックエンティティにも状態を伝える
                if (pLevel.getBlockEntity(pPos) instanceof FieldGeneratorBlockEntity be) {
                    be.setActive(!isPowered); // 信号があるとき、フィールドは「無効(false)」になる
                }
            }
        }
    }
}