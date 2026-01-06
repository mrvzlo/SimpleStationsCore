package com.ave.simplestationscore.resources;

import com.ave.simplestationscore.handlers.CommonItemHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemResource implements StationResource {
    private final CommonItemHandler inventory;
    private final int slot;
    public int usage;

    public ItemResource(CommonItemHandler inventory, int slot, int usage) {
        this.usage = usage;
        this.inventory = inventory;
        this.slot = slot;
    }

    public boolean useEveryTick() {
        return false;
    }

    public void load(CompoundTag tag) {
        return;
    }

    public void save(CompoundTag tag) {
        return;
    }

    public void set(int amount) {
        return;
    }

    @Override
    public boolean tryIncrement(ItemStack item) {
        return false;
    }

    public int get() {
        return getSlot().getCount();
    }

    public int getMax() {
        return getSlot().getMaxStackSize();
    }

    public void add(int amount) {
        getSlot().grow(amount);
    }

    public void substract() {
        getSlot().shrink(usage);
    }

    public int getRequired() {
        return usage;
    }

    public int getIncrement(Item item) {
        return 1;
    }

    private ItemStack getSlot() {
        return inventory.getStackInSlot(slot);
    }
}
