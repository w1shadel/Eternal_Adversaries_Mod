package com.Maxwell.Eternal_Adversaries.Entity.Boss.A;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class Volcanic_TyrantRenderer extends GeoEntityRenderer<Volcanic_TyrantMain> {
    public Volcanic_TyrantRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Volcanic_TyrantModel());
        this.shadowRadius = 0f; // 影のサイズを設定
    }
    @Override
    protected void applyRotations(Volcanic_TyrantMain entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick);
        float scaleFactor = 1.5f; // エンティティからスケール値を取得
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor); // X, Y, Z軸にスケーリング適用
    }
}