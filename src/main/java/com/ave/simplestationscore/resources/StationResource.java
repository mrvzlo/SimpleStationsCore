package com.ave.simplestationscore.resources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface StationResource {
    int get();

    int getMax();

    int getRequired();

    int getIncrement(Item item);

    void add(int amount);

    void set(int amount);

    boolean useEveryTick();

    void substract();

    default boolean tryIncrement(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        var item = stack.getItem();
        var inc = getIncrement(item);
        if (inc == 0 || !canAdd(inc))
            return false;
        add(inc);
        return true;
    }

    default boolean canAdd(int value) {
        return get() + value <= getMax();
    }

    default boolean canSubstract(int amount) {
        return get() >= amount;
    }

    void save(CompoundTag tag);

    void load(CompoundTag tag);

    default boolean isEnough() {
        return get() >= getRequired();
    }

    default float getPercent() {
        return (float) get() / getMax();
    }

    int getLow();

    int getHigh();

    void setLow(int amount);

    void setHigh(int amount);
}
