package com.ave.simplestationscore.managers;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ExportStrategy {
    public void pushOutput(BaseStationBlockEntity station) {
        ItemStack stack = station.inventory.getStackInSlot(BaseStationBlockEntity.OUTPUT_SLOT);
        if (stack.isEmpty())
            return;

        var belowPos = station.getBlockPos().below();
        IItemHandler handler = null;

        for (int dz = -1; dz <= 1; dz++)
            for (int dx = -1; dx <= 1; dx++) {
                if (handler != null)
                    break;
                var pos = belowPos.offset(dx, 0, dz);
                var be = station.getLevel().getBlockEntity(pos);
                if (be == null)
                    continue;
                var cap = be.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
                if (cap.isPresent())
                    handler = cap.orElse(null);
            }

        if (handler == null)
            return;

        var remaining = ItemHandlerHelper.insertItem(handler, stack, false);
        station.inventory.setStackInSlot(BaseStationBlockEntity.OUTPUT_SLOT, remaining);
    }
}
