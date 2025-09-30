package com.Maxwell.Eternal_Adversaries.Register;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.B.Mettalrugic_infuser_bossMain;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.C.The_VoidSteel_Vanguard;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.*;
@SuppressWarnings("removal")
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Eternal_Adversaries.MODID);
    public static final RegistryObject<EntityType<Volcanic_TyrantMain>> VOLCANIC_TYRANT = ENTITY_TYPES.register("volcanic_tyrant", () -> EntityType.Builder.of(Volcanic_TyrantMain::new, MobCategory.MONSTER).sized(6, 4).clientTrackingRange(8).build(new ResourceLocation(Eternal_Adversaries.MODID, "volcanic_tyrant").toString()));
    public static final RegistryObject<EntityType<Mettalrugic_infuser_bossMain>> METALLURGIC_INFUSER_BOSS = ENTITY_TYPES.register("metallurgic_infuser_boss", () -> EntityType.Builder.of(Mettalrugic_infuser_bossMain::new, MobCategory.MONSTER).sized(1, 1).clientTrackingRange(30).build(new ResourceLocation(Eternal_Adversaries.MODID, "metallurgic_infuser_boss").toString()));
    public static final RegistryObject<EntityType<The_VoidSteel_Vanguard>> THE_VOIDSTEEL_VANGUARD = ENTITY_TYPES.register("the_voidsteel_vanguard", () -> EntityType.Builder.of(The_VoidSteel_Vanguard::new, MobCategory.MONSTER).sized(1, 2.5f).clientTrackingRange(30).build(new ResourceLocation(Eternal_Adversaries.MODID, "the_voidsteel_vanguard").toString()));

}
