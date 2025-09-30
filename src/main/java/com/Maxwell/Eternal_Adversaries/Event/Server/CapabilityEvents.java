package com.Maxwell.Eternal_Adversaries.Event.Server;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities.FieldGeneratorDataProvider;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID)
public class CapabilityEvents {
    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() instanceof ServerLevel) {
            System.out.println("[DEBUG-2] Attaching capability to ServerLevel!"); // 検問所2
            event.addCapability(

                    new ResourceLocation(Eternal_Adversaries.MODID, "field_generator_data"),
                    new FieldGeneratorDataProvider()
            );
        }
    }
}