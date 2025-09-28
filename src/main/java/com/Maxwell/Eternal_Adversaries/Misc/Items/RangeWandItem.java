package com.Maxwell.Eternal_Adversaries.Misc.Items;

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

import javax.annotation.Nullable;
import java.util.List;

public class RangeWandItem extends Item {

    public RangeWandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!level.isClientSide) {
            Player player = pContext.getPlayer();
            ItemStack stack = pContext.getItemInHand();
            BlockPos clickedPos = pContext.getClickedPos();

            // NBTデータを取得（なければ新規作成）
            CompoundTag nbt = stack.getOrCreateTag();

            // スニークしながら右クリックで座標をリセット
            if (player.isShiftKeyDown()) {
                nbt.remove("pos1");
                nbt.remove("pos2");
                player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.reset").withStyle(ChatFormatting.YELLOW));
            } else {
                // 1番目の座標がまだ設定されていないか？
                if (!nbt.contains("pos1")) {
                    nbt.put("pos1", NbtUtils.writeBlockPos(clickedPos));
                    player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.pos1_set").withStyle(ChatFormatting.GREEN));
                    player.sendSystemMessage(Component.literal(posToString(clickedPos)).withStyle(ChatFormatting.GREEN));
                }
                // 1番目は設定済みだが、2番目がまだか？
                else if (!nbt.contains("pos2")) {
                    nbt.put("pos2", NbtUtils.writeBlockPos(clickedPos));
                    player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.pos2_set").withStyle(ChatFormatting.GREEN));
                    player.sendSystemMessage(Component.literal(posToString(clickedPos)).withStyle(ChatFormatting.GREEN));
                    player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.set_barrier").withStyle(ChatFormatting.AQUA));
                }

                // 両方設定済みの場合
                else {
                    player.sendSystemMessage(Component.translatable("item.eternal_adversaries.wand_of_barrier.error").withStyle(ChatFormatting.RED));
                }
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