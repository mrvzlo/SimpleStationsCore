package com.ave.simplestationscore.managers;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class ExportStrategy {
    public void pushOutput(BaseStationBlockEntity station) {
        ItemStack stack = station.inventory.getStackInSlot(BaseStationBlockEntity.OUTPUT_SLOT);
        if (stack.isEmpty())
            return;

        BlockPos belowPos = station.getBlockPos().below();
        IItemHandler handler = null;

        for (int dz = -1; dz <= 1; dz++)
            for (int dx = -1; dx <= 1; dx++) {
                if (handler != null)
                    break;
                var pos = belowPos.offset(dx, 0, dz);
                handler = Capabilities.ItemHandler.BLOCK.getCapability(station.getLevel(), pos, null, null,
                        Direction.UP);
            }

        if (handler == null)
            return;

        ItemStack remaining = ItemHandlerHelper.insertItem(handler, stack, false);
        station.inventory.setStackInSlot(BaseStationBlockEntity.OUTPUT_SLOT, remaining);
    }
}
