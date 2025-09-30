package com.Maxwell.Eternal_Adversaries.Register;


import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.FieldGeneratorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Entity.Misc.LockedDoorBlockEntity;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Eternal_Adversaries.MODID);

    public static final RegistryObject<BlockEntityType<LockedDoorBlockEntity>> LOCKED_DOOR =
            BLOCK_ENTITIES.register("locked_door", () ->
                    BlockEntityType.Builder.of(LockedDoorBlockEntity::new,
                            ModBlocks.LOCKED_DOOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<FieldGeneratorBlockEntity>> FILED_BARRIER =
            BLOCK_ENTITIES.register("filed_barrier", () ->
                    BlockEntityType.Builder.of(FieldGeneratorBlockEntity::new,
                            ModBlocks.FILED_BARRIER.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}