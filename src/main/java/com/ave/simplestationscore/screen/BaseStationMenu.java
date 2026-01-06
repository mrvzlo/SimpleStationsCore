package com.ave.simplestationscore.screen;

import com.ave.simplestationscore.handlers.CommonItemHandler;
import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;
import com.ave.simplestationscore.mainblock.StationContainer;
import com.ave.simplestationscore.resources.EnergyResource;
import com.ave.simplestationscore.uihelpers.Square;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public abstract class BaseStationMenu extends AbstractContainerMenu {
    public final Level level;
    public final StationContainer blockEntity;
    private final int invSize;

    public BaseStationMenu(int containerId, Inventory inventory, FriendlyByteBuf data, MenuType<?> menu) {
        this(containerId, inventory,
                (StationContainer) inventory.player.level().getBlockEntity(data.readBlockPos()), menu);
    }

    public BaseStationMenu(int containerId, Inventory inventory, StationContainer be, MenuType<?> menu) {
        super(menu, containerId);
        level = inventory.player.level();
        blockEntity = be;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        addItemSlots();

        if (blockEntity instanceof BaseStationBlockEntity station)
            addDataSlots(station);
        invSize = blockEntity.inventory.getSlots();
    }

    protected void addItemSlot(CommonItemHandler handler, int slot, Square square) {
        addSlot(new SlotItemHandler(handler, slot, square.left, square.top));
    }

    protected void addItemSlots() {
    }

    protected void addDataSlots(BaseStationBlockEntity station) {
        var energyRes = (EnergyResource) station.getEnergyResource();
        addDataSlot(DataSlotHelper.fromInt(() -> (int) station.progress, (x) -> station.progress = x));
        addDataSlot(DataSlotHelper.fromBool(() -> station.working, (x) -> station.working = x));
        addDataSlot(DataSlotHelper.fromInt(() -> energyRes.getLow(), (x) -> energyRes.setLow(x)));
        addDataSlot(DataSlotHelper.fromInt(() -> energyRes.getHigh(), (x) -> energyRes.setHigh(x)));
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        int HOTBAR_SLOT_COUNT = 9;
        int PLAYER_INVENTORY_ROW_COUNT = 3;
        int PLAYER_INVENTORY_COLUMN_COUNT = 9;
        int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
        int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
        int VANILLA_FIRST_SLOT_INDEX = 0;
        int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
        int TE_INVENTORY_SLOT_COUNT = invSize;

        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem())
            return ItemStack.EMPTY; // EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY; // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
                    false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int k = 0; k < 3; ++k)
            for (int j = 0; j < 9; ++j)
                addSlot(new Slot(inventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));

    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int j = 0; j < 9; ++j)
            addSlot(new Slot(inventory, j, 8 + j * 18, 142));
    }
}
