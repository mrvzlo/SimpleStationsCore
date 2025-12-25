package com.ave.simplestationscore.resources;

import com.ave.simplestationscore.handlers.WaterTank;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class WaterResource implements StationResource {
    public WaterTank storage;
    private int usage;

    public WaterResource(int max, int usage) {
        this.usage = usage;
        this.storage = WaterTank.create(0, max);
    }

    public boolean useEveryTick() {
        return false;
    }

    public int get() {
        return storage.getFluidAmount();
    }

    public int getMax() {
        return storage.getCapacity();
    }

    public int getIncrement(Item item) {
        return item.equals(Items.WATER_BUCKET) ? 1000 : 0;
    }

    public void add(int amount) {
        storage.fill(amount);
    }

    public void substract() {
        storage.drain(getRequired());
    }

    public int getRequired() {
        return usage;
    }

    public void load(CompoundTag tag) {
        storage.drain(get());
        add(tag.getInt("water"));
    }

    public void save(CompoundTag tag) {
        tag.putInt("water", get());
    }
}
