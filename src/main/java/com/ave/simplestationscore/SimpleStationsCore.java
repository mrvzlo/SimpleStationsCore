package com.ave.simplestationscore;

import com.ave.simplestationscore.registrations.CoreRegistrations;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleStationsCore.MODID)
public class SimpleStationsCore {
        public static final String MODID = "simplestationscore";

        public SimpleStationsCore(FMLJavaModLoadingContext context) {
                var modEventBus = context.getModEventBus();
                CoreRegistrations.register(modEventBus);
        }
}