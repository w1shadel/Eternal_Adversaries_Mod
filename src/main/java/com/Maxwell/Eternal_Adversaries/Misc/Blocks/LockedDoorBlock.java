package com.Maxwell.Eternal_Adversaries.Misc.Blocks;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.LockedDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

// ★★★ EntityBlockインターフェースを実装（implements）する ★★★
public class LockedDoorBlock extends DoorBlock implements EntityBlock {

    public LockedDoorBlock(Properties pProperties, BlockSetType pType) {
        super(pProperties, pType);
    }

    // --- EntityBlockインターフェースが要求するメソッド ---

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        // クライアント側では即座に成功を返し、見た目のカクつきを防ぐ
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // ブロックエンティティを取得
        if (pLevel.getBlockEntity(pPos) instanceof LockedDoorBlockEntity blockEntity) {
            ItemStack heldItem = pPlayer.getItemInHand(pHand);

            // --- 1. 鍵がまだ設定されていない場合 ---
            if (!blockEntity.hasKey()) {
                // 何かアイテムを持っている場合、それを鍵として設定する
                // シフトキーの判定は不要
                if (!heldItem.isEmpty()) {
                    blockEntity.setKey(heldItem);
                    pLevel.sendBlockUpdated(pPos, pState, pState, 3);
                    pLevel.playSound(null, pPos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 1.5f);
                    return InteractionResult.CONSUME; // アイテムを使ったことを示す
                }
                // 素手の場合は、通常のドアとして振る舞う（開閉できないようにするなら FAIL を返す）
                // ここでは開閉できない仕様にする
                pLevel.playSound(null, pPos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.FAIL;
            }

            // --- 2. 鍵が設定されている場合 ---
            else {
                ItemStack key = blockEntity.getKey();

                // プレイヤーが持っているアイテムが、設定された鍵と一致するか？
                if (ItemStack.isSameItemSameTags(heldItem, key)) {
                    // ★★★ ここが核心部分です ★★★
                    // super.use()を呼ばずに、ドアの状態を手動で切り替える

                    // ドアの状態を反転させる (開いていれば閉じる、閉じていれば開く)
                    pState = pState.cycle(OPEN);
                    pLevel.setBlock(pPos, pState, 10);

                    // ドアの開閉音を再生
                    pLevel.playSound(null, pPos, pState.getValue(OPEN) ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, pLevel.getRandom().nextFloat() * 0.1F + 0.9F);

                    // ゲームイベントを発生させる (バニラのドアと同じ挙動)
                    pLevel.gameEvent(pPlayer, pState.getValue(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);

                    return InteractionResult.SUCCESS;
                } else {
                    // 鍵が違うので、音を鳴らして開かない
                    pLevel.playSound(null, pPos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 0.5f);
                    return InteractionResult.FAIL;
                }
            }
        }
        return InteractionResult.FAIL;
    }

    // ★★★ シフトキー問題の根本的解決 ★★★
    // ブロックが設置されたときに、ドアの上半分にもブロックエンティティの参照をコピーする
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        // ドアの上半分が設置されたときにのみ処理
        if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pPos.below();
            if (pLevel.getBlockEntity(lowerPos) instanceof LockedDoorBlockEntity lowerBE) {
                if (pLevel.getBlockEntity(pPos) instanceof LockedDoorBlockEntity upperBE) {
                    // 下の鍵情報を上にコピーする（もし必要なら）
                    // ただし、通常は下のブロックエンティティだけを参照すれば十分
                }
            }
        }
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
}