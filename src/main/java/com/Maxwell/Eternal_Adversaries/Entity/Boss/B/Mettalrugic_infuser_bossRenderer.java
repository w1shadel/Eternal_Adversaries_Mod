package com.Maxwell.Eternal_Adversaries.Entity.Boss.B;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.Volcanic_TyrantMain;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.Volcanic_TyrantModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class Mettalrugic_infuser_bossRenderer  extends GeoEntityRenderer<Mettalrugic_infuser_bossMain> {
    public Mettalrugic_infuser_bossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Mettalrugic_infuser_bossModel());
        this.shadowRadius = 0f; // 影のサイズを設定
    }
}