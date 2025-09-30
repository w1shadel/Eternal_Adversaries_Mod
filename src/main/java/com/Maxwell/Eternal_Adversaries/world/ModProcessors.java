package com.Maxwell.Eternal_Adversaries.world;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Eternal_Adversaries.MODID);

    public static final RegistryObject<StructureProcessorType<FieldGeneratorProcessor>> FIELD_GENERATOR_PROCESSOR =
            PROCESSORS.register("field_generator_processor", () -> () -> FieldGeneratorProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<GiantDoorProcessor>> LOCKED_DOOR_PROCESSOR =
            PROCESSORS.register("giant_door_processor", () -> () -> GiantDoorProcessor.CODEC);
    public static void register(IEventBus eventBus) {
        PROCESSORS.register(eventBus);
    }
}