package com.Maxwell.Eternal_Adversaries.Event.Server;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.FieldGeneratorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities.FieldGeneratorSavedData;
import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities.ModCapabilities;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID)
public class ProtectionFieldEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level() instanceof ServerLevel serverLevel) {
            // ★★★ isProtectedヘルパーメソッドを呼び出す ★★★
            if (isProtected(serverLevel, event.getPos())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().level() instanceof ServerLevel serverLevel) {
            // ★★★ isProtectedヘルパーメソッドを呼び出す ★★★
            if (isProtected(serverLevel, event.getPos())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPistonMove(PistonEvent.Pre event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pistonPos = event.getPos();
            Direction moveDirection = event.getDirection();

            BlockPos pushedPos = pistonPos.relative(moveDirection);
            // ★★★ isProtectedヘルパーメソッドを呼び出す ★★★
            if (isProtected(serverLevel, pushedPos)) {
                event.setCanceled(true);
                return;
            }
            if (event.getPistonMoveType() == PistonEvent.PistonMoveType.RETRACT) {
                BlockPos pulledPos = pistonPos.relative(moveDirection, 2);
                if (isProtected(serverLevel, pulledPos)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    /**
     * ★★★ これが、Capabilityを使う新しいヘルパーメソッドです ★★★
     */
    private static boolean isProtected(ServerLevel serverLevel, BlockPos pos) {
        // ★★★ ワールドから直接SavedDataを取得して判定 ★★★
        FieldGeneratorSavedData savedData = FieldGeneratorSavedData.get(serverLevel);

        return savedData.getActiveGenerators().stream()
                .anyMatch(generatorPos -> {
                    if (serverLevel.getBlockEntity(generatorPos) instanceof FieldGeneratorBlockEntity be) {
                        return be.isActive() && be.getProtectionBox().contains(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    }
                    return false;
                });
    }
}