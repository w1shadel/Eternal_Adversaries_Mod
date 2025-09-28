package com.Maxwell.Eternal_Adversaries.Entity.Boss.B;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.Volcanic_TyrantMain;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
@OnlyIn(Dist.CLIENT)
public class Mettalrugic_infuser_bossModel extends GeoModel<Mettalrugic_infuser_bossMain> {
    @Override
    public ResourceLocation getModelResource(Mettalrugic_infuser_bossMain Mettalrugic_infuser_bossMain) {
        return new ResourceLocation(Eternal_Adversaries.MODID, "geo/entity/metallurgic_infuser_boss.json");
    }
    @Override
    public ResourceLocation getTextureResource(Mettalrugic_infuser_bossMain Mettalrugic_infuser_bossMain) {
        return new ResourceLocation(Eternal_Adversaries.MODID, "textures/entity/metallurgic_infuser_boss.png");
    }
    @Override
    public ResourceLocation getAnimationResource(Mettalrugic_infuser_bossMain Mettalrugic_infuser_bossMain) {
        return new ResourceLocation(Eternal_Adversaries.MODID, "animations/entity/metallurgic_infuser_boss.animation.json");
    }
}
