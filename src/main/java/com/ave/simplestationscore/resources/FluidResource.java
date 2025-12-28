package com.ave.simplestationscore.resources;

import com.ave.simplestationscore.handlers.BaseTank;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidResource implements StationResource {
    public BaseTank storage;
    private int usage;

    public FluidResource(Fluid fluid, int max, int usage) {
        this.usage = usage;
        this.storage = new BaseTank(fluid, 0, max);
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
        if (storage.type.equals(Fluids.WATER) && item.equals(Items.WATER_BUCKET))
            return 1000;
        if (storage.type.equals(Fluids.LAVA) && item.equals(Items.LAVA_BUCKET))
            return 1000;
        return 0;
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
