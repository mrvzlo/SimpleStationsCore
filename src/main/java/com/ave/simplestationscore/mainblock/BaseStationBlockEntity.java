package com.ave.simplestationscore.mainblock;

import java.util.HashMap;
import java.util.Map;

import com.ave.simplestationscore.managers.ExportStrategy;
import com.ave.simplestationscore.managers.WorkStrategy;
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

    private WorkStrategy workStrategy = new WorkStrategy();
    private ExportStrategy exportStrategy = new ExportStrategy();

    @SuppressWarnings({ "rawtypes", "unchecked" })
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

        working = workStrategy.getState(this);
        exportStrategy.pushOutput(this);

        if (!working)
            return;

        preWorkTick();
        workStrategy.performTick(this);

        if (progress < getMaxProgress())
            return;

        workStrategy.performEnd(this);
    }

    protected void preWorkTick() {
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

    public StationResource getEnergyResource() {
        return resources.get(FUEL_SLOT);
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
