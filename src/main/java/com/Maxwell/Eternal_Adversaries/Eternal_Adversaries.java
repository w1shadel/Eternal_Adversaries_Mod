package com.Maxwell.Eternal_Adversaries;

import com.Maxwell.Eternal_Adversaries.Register.ModBlockEntities;
import com.Maxwell.Eternal_Adversaries.Register.ModBlocks;
import com.Maxwell.Eternal_Adversaries.Register.ModEntities;
import com.Maxwell.Eternal_Adversaries.Register.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Eternal_Adversaries.MODID)
public class Eternal_Adversaries
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "eternal_adversaries";
    public Eternal_Adversaries(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
    }
}
