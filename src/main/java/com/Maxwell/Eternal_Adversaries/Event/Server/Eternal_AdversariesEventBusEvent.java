package com.Maxwell.Eternal_Adversaries.Event.Server;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.Volcanic_TyrantMain;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.B.Mettalrugic_infuser_bossMain;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.C.The_VoidSteel_Vanguard;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.C.The_Voidsteel_VanguardModel;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.Maxwell.Eternal_Adversaries.Register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
        event.put(ModEntities.METALLURGIC_INFUSER_BOSS.get(), Mettalrugic_infuser_bossMain.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_TVSV(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.THE_VOIDSTEEL_VANGUARD.get(), The_VoidSteel_Vanguard.createAttributes().build());
    }

}