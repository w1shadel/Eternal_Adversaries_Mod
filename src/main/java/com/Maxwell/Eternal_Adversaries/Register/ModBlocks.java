package com.Maxwell.Eternal_Adversaries.Register;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.Maxwell.Eternal_Adversaries.Misc.Blocks.FieldGeneratorBlock;
import com.Maxwell.Eternal_Adversaries.Misc.Blocks.GiantDoorPartBlock;
import com.Maxwell.Eternal_Adversaries.Misc.Blocks.LockedDoorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;



public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Eternal_Adversaries.MODID);

            public static final RegistryObject<Block> LOCKED_DOOR = BLOCKS.register("locked_door",
            () -> new LockedDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR), BlockSetType.IRON));

    public static final RegistryObject<Block> GIANT_DOOR = BLOCKS.register("giant_door",
            () -> new GiantDoorPartBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR)));

    public static final RegistryObject<Block> FILED_BARRIER = BLOCKS.register("filed_barrier",
            () -> new FieldGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR)));
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}