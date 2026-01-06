package com.ave.simplestationscore;

import com.ave.simplestationscore.registrations.CoreRegistrations;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;

@Mod(SimpleStationsCore.MODID)
public class SimpleStationsCore {
        public static final String MODID = "simplestationscore";

        public SimpleStationsCore(IEventBus modEventBus, ModContainer modContainer) {
                CoreRegistrations.register(modEventBus);
        }
}