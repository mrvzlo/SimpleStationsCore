package com.ave.simplestationscore.handlers;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class BaseTank extends FluidTank {
    public final Fluid type;

    public BaseTank(Fluid type, int value, int capacity) {
        super(capacity);
        this.type = type;
        fill(value);
    }

    public void fill(int value) {
        super.fill(new FluidStack(type, value), FluidAction.EXECUTE);
    }

    public void drain(IntValue value) {
        super.drain(value.get(), FluidAction.EXECUTE);
    }

    public void drain(int value) {
        super.drain(value, FluidAction.EXECUTE);
    }

    public float getPercent() {
        return (float) getFluidAmount() / getCapacity();
    }
}
