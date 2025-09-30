package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class FieldGeneratorDataProvider implements ICapabilityProvider{

    private final FieldGeneratorData data = new FieldGeneratorData();
    private final LazyOptional<IFieldGeneratorData> optionalData = LazyOptional.of(() -> data);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.FIELD_GENERATOR_DATA) {
            return optionalData.cast();
        }
        return LazyOptional.empty();
    }
}