package com.Maxwell.Eternal_Adversaries.Event.Client;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.Volcanic_TyrantRenderer;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.B.Mettalrugic_infuser_bossRenderer;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.C.The_Voidsteel_VanguardModel;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.Maxwell.Eternal_Adversaries.Register.ModBlockEntities;
import com.Maxwell.Eternal_Adversaries.Register.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.C.The_VoidSteel_Vanguard_Renderer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class Eternal_AdversariesEventBusClientEvent {
    @SubscribeEvent
    public static void onRegisterRenderers_Volc(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.VOLCANIC_TYRANT.get(), Volcanic_TyrantRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_Meta(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.METALLURGIC_INFUSER_BOSS.get(), Mettalrugic_infuser_bossRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_TVSV(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THE_VOIDSTEEL_VANGUARD.get(), The_VoidSteel_Vanguard_Renderer::new);
    }
    @SubscribeEvent
    public static void registerLayerDefinitions_TVSV(
            EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(The_Voidsteel_VanguardModel.LAYER_LOCATION,
                The_Voidsteel_VanguardModel::createBodyLayer);
    }

}