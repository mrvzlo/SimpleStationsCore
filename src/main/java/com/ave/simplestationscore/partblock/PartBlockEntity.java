package com.ave.simplestationscore.partblock;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;
import com.ave.simplestationscore.registrations.RegistrationManager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;

public class PartBlockEntity extends BlockEntity {

    private BlockPos controllerPos;

    public PartBlockEntity(BlockPos pos, BlockState state) {
        super(RegistrationManager.PART.getEntity(), pos, state);
    }

    public void setControllerPos(BlockPos pos) {
        controllerPos = pos;
        setChanged();
        if (level == null || level.isClientSide)
            return;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public int getStationType() {
        return (getController() == null) ? 0 : getController().type;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RegistrationManager.PART.getEntity(),
                (be, direction) -> getItemHandler(direction, be));
    }

    public static IItemHandler getItemHandler(Direction side, PartBlockEntity be) {
        var controller = be.getController();
        if (controller == null)
            return null;
        return controller.getItemHandler(side);
    }

    public static IEnergyStorage getEnergyStorage(PartBlockEntity be) {
        var controller = be.getController();
        if (controller == null)
            return null;
        return controller.getEnergyStorage();
    }

    public static FluidTank getWaterStorage(PartBlockEntity be) {
        var controller = be.getController();
        if (controller == null)
            return null;
        return controller.getWaterStorage();
    }

    public BaseStationBlockEntity getController() {
        if (controllerPos == null)
            return null;
        return ((BaseStationBlockEntity) getLevel().getBlockEntity(controllerPos));
    }

    public boolean isEdge() {
        return sameX() || sameZ();
    }

    public boolean sameX() {
        if (this.controllerPos == null)
            return false;
        return this.controllerPos.getX() == getBlockPos().getX();
    }

    public boolean sameZ() {
        if (this.controllerPos == null)
            return false;
        return this.controllerPos.getZ() == getBlockPos().getZ();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (controllerPos == null)
            return;
        tag.putLong("Controller", controllerPos.asLong());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        controllerPos = BlockPos.of(tag.getLong("Controller"));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
