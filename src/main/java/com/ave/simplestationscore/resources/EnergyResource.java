package com.ave.simplestationscore.resources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.energy.EnergyStorage;

public class EnergyResource implements StationResource {
    public EnergyStorage storage;
    private int usage;
    private final int coalIncrement;

    public EnergyResource(int max, int usage, int coalInc) {
        this.usage = usage;
        coalIncrement = coalInc;
        this.storage = new EnergyStorage(max);
    }

    public boolean useEveryTick() {
        return true;
    }

    public void load(CompoundTag tag) {
        storage.extractEnergy(get(), false);
        add(tag.getInt("fuel"));
    }

    public void save(CompoundTag tag) {
        tag.putInt("fuel", get());
    }

    public int get() {
        return storage.getEnergyStored();
    }

    public int getMax() {
        return storage.getMaxEnergyStored();
    }

    public void add(int amount) {
        storage.receiveEnergy(amount, false);
    }

    public void substract() {
        storage.extractEnergy(usage, false);
    }

    public int getRequired() {
        return usage;
    }

    public int getIncrement(Item item) {
        if (item.equals(Items.COAL_BLOCK))
            return coalIncrement * 9;
        return coalIncrement;
    }
}
