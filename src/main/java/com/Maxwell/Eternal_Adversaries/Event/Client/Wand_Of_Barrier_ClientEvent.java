package com.Maxwell.Eternal_Adversaries.Event.Client;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.Maxwell.Eternal_Adversaries.Misc.Items.RangeWandItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Wand_Of_Barrier_ClientEvent {

    /**
     * ★★★ ワールドの描画が行われる各段階で呼び出されるイベント ★★★
     */
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        // ワイヤーフレームを描画するのに適した "AFTER_SOLID_BLOCKS" ステージで処理
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack heldStack = player.getMainHandItem();
        // プレイヤーが手に持っているのは、範囲設定ワンドか？
        if (heldStack.getItem() instanceof RangeWandItem) {
            CompoundTag nbt = heldStack.getTag();
            if (nbt != null && nbt.contains("pos1")) {
                BlockPos pos1 = NbtUtils.readBlockPos(nbt.getCompound("pos1"));

                // 2番目の座標は、まだ設定されていなければプレイヤーが現在見ているブロック
                BlockPos pos2;
                if (nbt.contains("pos2")) {
                    pos2 = NbtUtils.readBlockPos(nbt.getCompound("pos2"));
                } else {
                    // Raycastでプレイヤーが見ているブロックを取得
                    var hitResult = player.pick(20, 0, false); // 20ブロック先まで
                    pos2 = BlockPos.containing(hitResult.getLocation());
                }

                // --- 描画処理の本体 ---
                renderBox(event.getPoseStack(), pos1, pos2);
            }
        }
    }
    private static void renderBox(PoseStack poseStack, BlockPos pos1, BlockPos pos2) {
        // カメラの位置を取得して、描画位置をワールド座標に合わせる
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // 描画用のバッファを取得
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lines());

        // ★★★ 修正箇所 ★★★
        // 1. プレイヤーの選択順序に関わらず、正しいAABBを生成
        AABB box = new AABB(pos1, pos2);

        // 2. 境界上のブロック全体を囲むように、AABBを1ブロック分広げる
        AABB renderBox = box.expandTowards(1, 1, 1);

        // LevelRenderer.renderLineBoxを使ってワイヤーフレームを描画
        LevelRenderer.renderLineBox(poseStack, buffer, renderBox, 1.0f, 0.8f, 0.2f, 1.0f); // 色 (R, G, B, A) - オレンジ色

        // 描画を確定
        bufferSource.endBatch(RenderType.lines());
        poseStack.popPose();
    }
}