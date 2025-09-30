package com.Maxwell.Eternal_Adversaries.Misc.Items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DungeonStaffItem extends Item {

    public DungeonStaffItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        Player player = pContext.getPlayer();

        // サーバーサイドでのみ動作
        if (level.isClientSide || player == null) {
            return InteractionResult.SUCCESS;
        }

        // この杖は開発者用のツールなので、クリエイティブモード/OP権限を持つプレイヤーのみ使用可能
        if (!player.canUseGameMasterBlocks()) {
            player.sendSystemMessage(Component.literal("You don't have permission to use this staff."));
            return InteractionResult.FAIL;
        }

        // 右クリックしたブロックがジグソーブロックか確認
        if (!(level.getBlockState(pos).getBlock() instanceof JigsawBlock)) {
            player.sendSystemMessage(Component.literal("This staff can only be used on Jigsaw Blocks."));
            return InteractionResult.FAIL;
        }

        // ブロックエンティティを取得
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            // 通常はありえないが、念のため
            return InteractionResult.FAIL;
        }

        // --- ここからが /data merge の処理 ---

        // 1. マージしたいNBTデータを作成する
        CompoundTag nbtToMerge = new CompoundTag();
        nbtToMerge.putString("name", "eternal_adversaries:giant_door_marker");   // "yourmod" はあなたのMOD IDに書き換えてください
        nbtToMerge.putString("target", "minecraft:air");             // これはJigsaw Blockの標準的な設定
        nbtToMerge.putString("pool", "eternal_adversaries:dungeon/giant_door_boss_key"); // "yourmod" はあなたのMOD IDに

        // 2. 既存のNBTデータを取得し、新しいデータをマージ（上書き）する
        CompoundTag existingNbt = blockEntity.saveWithoutMetadata();
        CompoundTag mergedNbt = existingNbt.merge(nbtToMerge);

        // 3. マージしたNBTをブロックエンティティにロードし直す
        blockEntity.load(mergedNbt);
        blockEntity.setChanged(); // 変更を保存

        // 4. プレイヤーに成功を通知
        player.sendSystemMessage(Component.literal("Jigsaw Block configured for Giant Door!"));
        level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8f, 1.2f);

        return InteractionResult.SUCCESS;
    }

    // (任意) アイテムにエンチャントのような輝きを持たせる
    @Override
    public boolean isFoil(net.minecraft.world.item.ItemStack pStack) {
        return true;
    }
}