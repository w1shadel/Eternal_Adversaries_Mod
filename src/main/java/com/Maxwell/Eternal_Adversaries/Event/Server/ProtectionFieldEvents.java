package com.Maxwell.Eternal_Adversaries.Event.Server;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.FieldGeneratorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.FieldGeneratorManager;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID)
public class ProtectionFieldEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity target = event.getEntity();

        // levelがServerLevelのインスタンスであるかを確認
        if (target.level() instanceof ServerLevel serverLevel) {

            // ダメージソースが「炎」または「マグマ」であるか？
            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                BlockPos entityPos = target.blockPosition();

                // ★★★ マネージャーのリストをチェックするだけ！ ★★★
                boolean isInFireproofField = FieldGeneratorManager.getActiveGenerators()
                        .anyMatch(generatorPos -> {
                            // マネージャーは座標しか持っていないので、実際にBlockEntityを取得して状態を確認
                            if (serverLevel.getBlockEntity(generatorPos) instanceof FieldGeneratorBlockEntity be) {
                                // BlockEntityが有効で、かつ、ターゲットが第二範囲内にいるか？
                                return be.isActive() && be.getProtectionBox().contains(entityPos.getX() + 0.5, entityPos.getY() + 0.5, entityPos.getZ() + 0.5);
                            }
                            // もし座標に対応するBlockEntityが見つからなければ、保護されていない
                            return false;
                        });

                // もし保護フィールド内にいるなら、イベントをキャンセル
                if (isInFireproofField) {
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level() instanceof ServerLevel serverLevel) {
            BlockPos breakPos = event.getPos();

            // ★★★ マネージャーのリストをチェックするだけ！ ★★★
            boolean isProtected = FieldGeneratorManager.getActiveGenerators()
                    .anyMatch(generatorPos -> {
                        if (serverLevel.getBlockEntity(generatorPos) instanceof FieldGeneratorBlockEntity be) {
                            return be.getProtectionBox().contains(breakPos.getX() + 0.5, breakPos.getY() + 0.5, breakPos.getZ() + 0.5);
                        }
                        return false;
                    });

            if (isProtected) {
                event.setCanceled(true);
            }
        }
    }

}