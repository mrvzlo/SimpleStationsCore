package com.ave.simplestationscore.resources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

public class FluidItemResource extends BaseResource implements StationResource {
    public int stored;
    public final int max;
    private final int baseInc;
    private final String key;

    public FluidItemResource(int max, int usage, int inc, String key) {
        this.usage = usage;
        baseInc = inc;
        this.key = key;
        this.max = max;
    }

    public boolean useEveryTick() {
        return true;
    }

    public void load(CompoundTag tag) {
        set(tag.getInt(key));
    }

    public void save(CompoundTag tag) {
        tag.putInt(key, get());
    }

    public void set(int value) {
        stored = value;
    }

    public int get() {
        return stored;
    }

    public int getMax() {
        return max;
    }

    public void add(int amount) {
        stored += amount;
        if (stored > max)
            stored = max;
    }

    public void substract() {
        stored -= usage;
        if (stored < 0)
            stored = 0;
    }

    public int getRequired() {
        return usage;
    }

    public int getIncrement(Item item) {
        return baseInc;
    }
}
