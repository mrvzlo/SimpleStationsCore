package com.ave.simplestationscore.resources;

import com.ave.simplestationscore.managers.TemperatureManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class TemperatureResource implements StationResource {
    public float value;
    public int cooldown;
    private final int RiseSpeed;
    private static int MinBurn = 50;

    public TemperatureResource(int riseSpeed) {
        RiseSpeed = riseSpeed;
        this.value = 20;
    }

    public boolean useEveryTick() {
        return true;
    }

    public void load(CompoundTag tag) {
        value = tag.getFloat("temp");
    }

    public void save(CompoundTag tag) {
        tag.putFloat("temp", value);
    }

    public int get() {
        return (int) value;
    }

    public int getMax() {
        return (int) TemperatureManager.MaxTemp;
    }

    public void add(int amount) {
        if (value + amount > getMax())
            value = getMax();
        else
            value += amount;
    }

    public void substract() {
        value -= TemperatureManager.getCool(value);
    }

    public int getRequired() {
        return TemperatureManager.MinTemp;
    }

    public int getIncrement(Item item) {
        var burn = new ItemStack(item).getBurnTime(RecipeType.SMELTING);
        if (burn < MinBurn) {
            cooldown = 1;
            return RiseSpeed * burn / MinBurn;
        }
        cooldown = burn / MinBurn;
        return RiseSpeed;
    }

    @Override
    public boolean tryIncrement(ItemStack stack) {
        if (cooldown > 0) {
            cooldown--;
            if (cooldown > 0)
                add(RiseSpeed);
            return false;
        }
        if (value > TemperatureManager.MaxFeed)
            return false;
        return StationResource.super.tryIncrement(stack);
    }
}
