package com.ave.simplestationscore.handlers;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class CommonItemHandler extends ItemStackHandler {
    public CommonItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == BaseStationBlockEntity.OUTPUT_SLOT)
            return false;
        if (slot == BaseStationBlockEntity.FUEL_SLOT)
            return stack.getItem() == Items.COAL_BLOCK || stack.getItem() == Items.COAL;
        return super.isItemValid(slot, stack);
    }

    public NonNullList<ItemStack> getAsList() {
        return stacks;
    }
}