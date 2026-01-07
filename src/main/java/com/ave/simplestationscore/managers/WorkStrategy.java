package com.ave.simplestationscore.managers;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;

import net.minecraft.sounds.SoundSource;

public class WorkStrategy {
    public boolean getState(BaseStationBlockEntity station) {
        var slot = station.inventory.getStackInSlot(BaseStationBlockEntity.OUTPUT_SLOT);
        if (station.type == -1)
            return false;
        for (var res : station.resources.values())
            if (!res.isEnough())
                return false;

        if (slot.getCount() == 0)
            return true;

        var product = station.getProduct(true);
        if (product.isEmpty() || slot.getCount() + product.getCount() > slot.getMaxStackSize())
            return false;
        return slot.is(product.getItem());
    }

    public void performTick(BaseStationBlockEntity station) {
        if (!station.working)
            return;
        station.toProduce = station.getProduct(false);

        for (var res : station.resources.values())
            if (res.useEveryTick())
                res.substract();

        station.progress += station.speed;
        playSound(station);
    }

    private void playSound(BaseStationBlockEntity station) {
        if (station.soundCooldown > 0) {
            station.soundCooldown--;
            return;
        }
        station.soundCooldown += 20;
        station.getLevel().playSound(null, station.getBlockPos(), station.getWorkSound(), SoundSource.BLOCKS);
    }

    public void performEnd(BaseStationBlockEntity station) {
        for (var res : station.resources.values())
            if (!res.useEveryTick())
                res.substract();

        var toAdd = station.toProduce.copy();
        var slot = station.inventory.getStackInSlot(BaseStationBlockEntity.OUTPUT_SLOT);
        toAdd.grow(slot.getCount());
        station.inventory.setStackInSlot(BaseStationBlockEntity.OUTPUT_SLOT, toAdd);
        station.setChanged();
    }
}
