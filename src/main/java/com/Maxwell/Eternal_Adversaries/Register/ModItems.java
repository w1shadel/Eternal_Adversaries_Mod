package com.Maxwell.Eternal_Adversaries.Register;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.Maxwell.Eternal_Adversaries.Misc.Items.GiantDoorItem;
import com.Maxwell.Eternal_Adversaries.Misc.Items.KeyItem;
import com.Maxwell.Eternal_Adversaries.Misc.Items.RangeWandItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static final RegistryObject<CreativeModeTab> EtA_TAB;
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<BlockItem> LOOCKED_DOOR;
    public static final RegistryObject<BlockItem> GIANT_DOOR;
    public static final RegistryObject<BlockItem> FILED_BARRIER;
    public static final RegistryObject<Item> GIANT_DOOR_SUMMONER_3x3;
    public static final RegistryObject<Item> GIANT_DOOR_SUMMONER_2x3;
    public static final RegistryObject<Item> KEY;
    public static final RegistryObject<Item> WAND_OF_BARRIER;

    static
    {
        TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "miniboss");
        ITEMS = DeferredRegister.create(Registries.ITEM,Eternal_Adversaries.MODID);
        LOOCKED_DOOR = ITEMS.register("loocked_door", () -> new BlockItem((Block) ModBlocks.LOCKED_DOOR.get(), new Item.Properties()));
        FILED_BARRIER = ITEMS.register("filed_barrier", () -> new BlockItem((Block) ModBlocks.FILED_BARRIER.get(), new Item.Properties()));
        GIANT_DOOR = ITEMS.register("giant_door", () -> new BlockItem((Block) ModBlocks.GIANT_DOOR.get(), new Item.Properties()));
        KEY = ITEMS.register("key", () -> new KeyItem(new Item.Properties().rarity(Rarity.EPIC)));
        WAND_OF_BARRIER = ITEMS.register("wand_of_barrier_item", () -> new RangeWandItem(new Item.Properties().rarity(Rarity.EPIC)));
        GIANT_DOOR_SUMMONER_3x3 = ITEMS.register("giant_door_summoner_3x3", () -> new GiantDoorItem(new Item.Properties().rarity(Rarity.EPIC),3,3));
        GIANT_DOOR_SUMMONER_2x3 = ITEMS.register("giant_door_summoner_2x3", () -> new GiantDoorItem(new Item.Properties().rarity(Rarity.EPIC),2,3));
        EtA_TAB = TABS.register("eternal_adversaries_items", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.eternal_adversaries.items")).icon(() ->
                new ItemStack(KEY.get())).displayItems((enabledFeatures, entries) ->
        {
            entries.accept(KEY.get());
            entries.accept(GIANT_DOOR_SUMMONER_2x3.get());
            entries.accept(GIANT_DOOR_SUMMONER_3x3.get());
            entries.accept(WAND_OF_BARRIER.get());
            entries.accept(FILED_BARRIER.get());
        }).build());
    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        TABS.register(eventBus);
    }
}
