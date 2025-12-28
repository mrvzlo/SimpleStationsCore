package com.ave.simplestationscore.screen;

import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.world.inventory.DataSlot;

public class DataSlotHelper {
    public static DataSlot fromInt(IntSupplier getter, IntConsumer setter) {
        return new DataSlot() {
            @Override
            public int get() {
                return getter.getAsInt();
            }

            @Override
            public void set(int value) {
                setter.accept(value);
            }
        };
    }

    public static DataSlot fromBool(BooleanSupplier getter, BooleanConsumer setter) {
        return new DataSlot() {
            @Override
            public int get() {
                return getter.getAsBoolean() ? 1 : 0;
            }

            @Override
            public void set(int value) {
                setter.accept(value != 0);
            }
        };
    }
}
