package com.Maxwell.Eternal_Adversaries.Misc;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class DamagetypeRegister {
    public static final ResourceKey<DamageType> Volcanic_attack = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Eternal_Adversaries.MODID,"volcanic_attack"));
    public static final ResourceKey<DamageType> Meta_attack = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Eternal_Adversaries.MODID,"meta_attack"));



    public static DamageSource causeVolcanicattackDamage(LivingEntity attacker) {
        return new DamageSource(attacker.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(Volcanic_attack), attacker);
    }
    public static DamageSource causeMetaattackDamage(LivingEntity attacker) {
        return new DamageSource(attacker.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(Meta_attack), attacker);
    }


    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, EntityType<?>... toIgnore) {
        return getEntityDamageSource(level, type, null, toIgnore);
    }

    public static DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, EntityType<?>... toIgnore) {
        return getIndirectEntityDamageSource(level, type, attacker, attacker, toIgnore);
    }

    public static DamageSource getIndirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker, EntityType<?>... toIgnore) {
        return toIgnore.length > 0 ? new MoreDamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), toIgnore) : new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
    }

}