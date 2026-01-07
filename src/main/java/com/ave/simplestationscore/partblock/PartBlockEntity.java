package com.ave.simplestationscore.partblock;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;
import com.ave.simplestationscore.registrations.CoreRegistrations;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;

public class PartBlockEntity extends BlockEntity {
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> getEnergyStorage(this));
    private final LazyOptional<IFluidHandler> fluid = LazyOptional.of(() -> getWaterStorage(this));
    private final LazyOptional<IItemHandler> sidedItemHandler = LazyOptional
            .of(() -> getItemHandler(Direction.DOWN, this));
    private final LazyOptional<IItemHandler> inventoryHandler = LazyOptional.of(() -> getItemHandler(null, this));

    private BlockPos controllerPos;

    public PartBlockEntity(BlockPos pos, BlockState state) {
        super(CoreRegistrations.PART.getEntity(), pos, state);
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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY)
            return energy.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluid.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return side == null ? inventoryHandler.cast() : sidedItemHandler.cast();

        return super.getCapability(cap, side);
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
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (controllerPos == null)
            return;
        tag.putLong("Controller", controllerPos.asLong());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        controllerPos = BlockPos.of(tag.getLong("Controller"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
