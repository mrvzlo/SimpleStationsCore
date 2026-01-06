package com.ave.simplestationscore.resources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyResource implements StationResource {
    public EnergyStorage storage;
    private final int baseInc;
    private int lowValue;
    private int highValue;
    public int usage;

    public EnergyResource(int max, int usage, int inc) {
        this.usage = usage;
        baseInc = inc;
        this.storage = new EnergyStorage(max);
    }

    public boolean useEveryTick() {
        return true;
    }

    public void load(CompoundTag tag) {
        set(tag.getInt("fuel"));
    }

    public void save(CompoundTag tag) {
        tag.putInt("fuel", get());
    }

    public void set(int amount) {
        storage.extractEnergy(get(), false);
        add(amount);
    }

    public int get() {
        return storage.getEnergyStored();
    }

    public int getMax() {
        return storage.getMaxEnergyStored();
    }

    public void add(int amount) {
        storage.receiveEnergy(amount, false);
        recalcParts();
    }

    public void substract() {
        storage.extractEnergy(usage, false);
        recalcParts();
    }

    public int getRequired() {
        return usage;
    }

    public int getIncrement(Item item) {
        if (item.equals(Items.COAL_BLOCK) || item.equals(Items.REDSTONE_BLOCK))
            return baseInc * 9;
        return baseInc;
    }

    public int getLow() {
        return lowValue;
    }

    public int getHigh() {
        return highValue;
    }

    public void setLow(int amount) {
        set((lowValue & 0xFFFF) | (highValue << 16));
    }

    public void setHigh(int amount) {
        set((lowValue & 0xFFFF) | (highValue << 16));
    }

    private void recalcParts() {
        highValue = get() >> 16;
        lowValue = get() & 0xFFFF;
    }
}
