package com.Maxwell.Eternal_Adversaries.Entity.Boss.C;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class The_VoidSteel_Vanguard_Renderer extends MobRenderer<The_VoidSteel_Vanguard, The_Voidsteel_VanguardModel> {
    @SuppressWarnings("removal")
    private static final ResourceLocation TEXTURE = new ResourceLocation(Eternal_Adversaries.MODID,"textures/entity/the_voidsteel_vanguard.png");

    public The_VoidSteel_Vanguard_Renderer(EntityRendererProvider.Context renderManagerIn) {
        // ★★★ 修正箇所 ★★★
        // EtAMModelLayers.DRAUGR_MODEL から、The_Voidsteel_VanguardModel.LAYER_LOCATION に変更
        super(renderManagerIn, new The_Voidsteel_VanguardModel(renderManagerIn.bakeLayer(The_Voidsteel_VanguardModel.LAYER_LOCATION)), 0.5F);
    }

    protected void scale(The_VoidSteel_Vanguard p_114907_, PoseStack p_114908_, float p_114909_) {
        float f = 1.0F;
        p_114908_.scale(f, f, f);
        super.scale(p_114907_, p_114908_, p_114909_);
    }

    public ResourceLocation getTextureLocation(The_VoidSteel_Vanguard entity) {
        return TEXTURE;
    }

}