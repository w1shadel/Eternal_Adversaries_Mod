package com.Maxwell.Eternal_Adversaries.Misc.Items;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.FieldGeneratorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Misc.Blocks.FieldGeneratorBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class RangeWandItem extends Item {

    public RangeWandItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 0; // 0にすると、右クリックを離したときにだけuseOnが呼ばれるようになる
    }
    @Override
    public boolean isFoil(ItemStack pStack) {
        // 2つの座標が選択されたら光るようにする
        CompoundTag nbt = pStack.getTag();
        return nbt != null && nbt.contains("pos1") && nbt.contains("pos2");
    }
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Player player = pContext.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        ItemStack stack = pContext.getItemInHand();
        BlockPos clickedPos = pContext.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        // --- 1. クリックしたブロックが「フィールドジェネレーター」だった場合の処理 ---
        if (clickedState.getBlock() instanceof FieldGeneratorBlock) {
            if (level.getBlockEntity(clickedPos) instanceof FieldGeneratorBlockEntity be) {
                CompoundTag nbt = stack.getTag();
                // ワンドに2つの座標が設定されているか？
                if (nbt != null && nbt.contains("pos1") && nbt.contains("pos2")) {
                    BlockPos pos1 = NbtUtils.readBlockPos(nbt.getCompound("pos1"));
                    BlockPos pos2 = NbtUtils.readBlockPos(nbt.getCompound("pos2"));

                    // ブロックエンティティに範囲を設定
                    be.setRange(pos1, pos2);

                    player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.applied").withStyle(ChatFormatting.GOLD));
                    return InteractionResult.CONSUME;
                } else {
                    player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.not_enough_pos").withStyle(ChatFormatting.RED));
                    return InteractionResult.FAIL;
                }
            }
        }

        // --- 2. クリックしたのが他のブロックだった場合の、座標設定処理 ---
        CompoundTag nbt = stack.getOrCreateTag();
        if (player.isShiftKeyDown()) {
            nbt.remove("pos1");
            nbt.remove("pos2");
            player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.reset").withStyle(ChatFormatting.YELLOW));
        } else {
            if (!nbt.contains("pos1")) {
                nbt.put("pos1", NbtUtils.writeBlockPos(clickedPos));
                player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.pos1_set").append(Component.literal(": " + posToString(clickedPos))).withStyle(ChatFormatting.GREEN));
            } else if (!nbt.contains("pos2")) {
                nbt.put("pos2", NbtUtils.writeBlockPos(clickedPos));
                player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.pos2_set").append(Component.literal(": " + posToString(clickedPos))).withStyle(ChatFormatting.GREEN));
                player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.set_barrier").withStyle(ChatFormatting.AQUA));
            } else {
                player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.error").withStyle(ChatFormatting.RED));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag nbt = pStack.getTag();
        if (nbt != null) {
            if (nbt.contains("pos1")) {
                BlockPos pos1 = NbtUtils.readBlockPos(nbt.getCompound("pos1"));
                pTooltipComponents.add(Component.literal("Pos 1: " + posToString(pos1)).withStyle(ChatFormatting.GRAY));
            }
            if (nbt.contains("pos2")) {
                BlockPos pos2 = NbtUtils.readBlockPos(nbt.getCompound("pos2"));
                pTooltipComponents.add(Component.literal("Pos 2: " + posToString(pos2)).withStyle(ChatFormatting.GRAY));
            }
        }
        pTooltipComponents.add(Component.translatable("item.eternal_adversaries.wand_of_barrier.desc1").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private static String posToString(BlockPos pos) {
        return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
    }
}