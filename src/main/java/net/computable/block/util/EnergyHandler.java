package net.computable.block.util;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHandler implements IEnergyStorage, INBTSerializable<Tag> {
    private float energy = 0;
    private final float capacity;
    private final float maxReceive;
    private final float maxExtract;

    public EnergyHandler(int capacity, int maxReceive, int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        float energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            if (energyReceived != 0) {
                energy += energyReceived;
                onEnergyChanged();
            }
        }
        return Math.round(energyReceived);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        float energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            if (energyExtracted != 0) {
                energy -= energyExtracted;
                onEnergyChanged();
            }
        }
        return Math.round(energyExtracted);
    }

    public float consumeEnergy(float energy, boolean simulate) {
        float energyConsumed = Math.min(this.energy, energy);
        if (!simulate) {
            if (energyConsumed != 0) {
                this.energy -= energyConsumed;
                onEnergyChanged();
            }
        }
        return energyConsumed;
    }

    public float setEnergy(int energy, boolean simulate) {
        float change = energy - this.energy;
        if (!simulate) {
            if (change != 0) {
                this.energy = energy;
                onEnergyChanged();
            }
        }
        return change;
    }

    public float getActualEnergyStored() {
        return energy;
    }

    public float getActualMaxEnergyStored() {
        return capacity;
    }

    @Override
    public int getEnergyStored() {
        return Math.round(energy);
    }

    @Override
    public int getMaxEnergyStored() {
        return Math.round(capacity);
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    public Tag serializeNBT() {
        return FloatTag.valueOf(energy);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof FloatTag floatNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        energy = floatNbt.getAsFloat();
    }

    public void onEnergyChanged() {}
}
