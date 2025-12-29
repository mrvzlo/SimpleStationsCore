package com.ave.simplestationscore.mainblock;

import java.util.HashMap;
import java.util.Map;

import com.ave.simplestationscore.managers.ExportManager;
import com.ave.simplestationscore.managers.WorkManager;
import com.ave.simplestationscore.resources.EnergyResource;
import com.ave.simplestationscore.resources.StationResource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public abstract class BaseStationBlockEntity extends StationContainer {
    public static final int FUEL_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public final Map<Integer, StationResource> resources = new HashMap<Integer, StationResource>();
    public int type = -1;
    public float progress = 0;
    public boolean working = false;

    public int speed = 1;
    public int soundCooldown = 0;
    protected int particleCooldown = 0;

    public ItemStack toProduce;
    public int fuelValue = 0;
    public int fuelMax = 0;

    public BaseStationBlockEntity(BlockEntityType entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
    }

    public void tick() {
        if (level.isClientSide) {
            if (showParticle())
                addParticle();
            return;
        }

        if (progress >= getMaxProgress())
            progress -= getMaxProgress();

        checkNewType();
        for (var entry : resources.entrySet())
            checkResource(entry.getKey(), entry.getValue());

        working = WorkManager.getWorkingState(this);
        ExportManager.pushOutput(this);
        fuelValue = resources.get(BaseStationBlockEntity.FUEL_SLOT).get();

        if (!working)
            return;

        WorkManager.performWorkTick(this);

        if (progress < getMaxProgress())
            return;

        WorkManager.performWorkEnd(this);
    }

    private void checkResource(int slot, StationResource resource) {
        var stack = inventory.getStackInSlot(slot);
        if (!resource.tryIncrement(stack))
            return;

        if (stack.getItem().equals(Items.WATER_BUCKET) || stack.getItem().equals(Items.LAVA_BUCKET))
            inventory.setStackInSlot(slot, new ItemStack(Items.BUCKET));
        else
            stack.shrink(1);
    }

    public IEnergyStorage getEnergyStorage() {
        var resource = resources.get(BaseStationBlockEntity.FUEL_SLOT);
        if (resource instanceof EnergyResource energy)
            return energy.storage;
        return null;
    }

    public FluidTank getWaterStorage() {
        return null;
    }

    private void checkNewType() {
        var newType = getCurrentType();
        if (type == newType)
            return;

        type = newType;
        progress = 0;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        saveAll(tag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        saveAll(tag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        type = tag.getInt("type");
        progress = tag.getFloat("progress");
        for (var res : resources.values())
            res.load(tag);
    }

    protected void saveAll(CompoundTag tag) {
        tag.putFloat("progress", progress);
        tag.putInt("type", type);
        for (var res : resources.values())
            res.save(tag);
    }

    protected boolean showParticle() {
        if (progress == 0)
            return false;
        if (particleCooldown <= 0)
            return true;
        particleCooldown--;
        return false;
    }

    protected void addParticle() {
        return;
    }

    public abstract int getMaxProgress();

    public abstract SoundEvent getWorkSound();

    public abstract ItemStack getProduct(boolean check);

    protected abstract int getCurrentType();
}
