package com.ave.simplestationscore;

import com.ave.simplestationscore.registrations.CoreRegistrations;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SimpleStationsCore.MODID)
public class SimpleStationsCore {
        public static final String MODID = "simplestationscore";

        public SimpleStationsCore(IEventBus modEventBus, ModContainer modContainer) {
                modContainer.registerConfig(ModConfig.Type.COMMON, CoreConfig.SPEC);
                CoreRegistrations.register(modEventBus);
        }
}