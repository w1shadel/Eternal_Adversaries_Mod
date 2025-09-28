package com.Maxwell.Eternal_Adversaries.Event.Server;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.Volcanic_TyrantMain;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.Maxwell.Eternal_Adversaries.Register.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Eternal_AdversariesEventBusEvent {
    @SubscribeEvent
    public static void registerAttributes_Volc(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.VOLCANIC_TYRANT.get(), Volcanic_TyrantMain.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_Meta(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.METALLURGIC_INFUSER_BOSS.get(), Volcanic_TyrantMain.createAttributes().build());
    }
}