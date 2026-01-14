package com.ave.simplestationscore;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@SuppressWarnings("removal")
@EventBusSubscriber(modid = SimpleStationsCore.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CoreConfig {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        static ModConfigSpec SPEC;

        public static ModConfigSpec.IntValue POWER_MAX;
        public static ModConfigSpec.IntValue POWER_PER_COAL;
        public static ModConfigSpec.IntValue FLUID_MAX;
        public static ModConfigSpec.IntValue POWER_PER_RED;

        static {
                setupGenerationConfig();
                SPEC = BUILDER.build();
        }

        private static void setupGenerationConfig() {
                POWER_MAX = BUILDER
                                .comment("Max redstone power to store\n Default: 100000")
                                .defineInRange("power_max", 100000, 1, 10000000);
                POWER_PER_COAL = BUILDER
                                .comment("Base RF amount received from 1 coal\n Default: 48000")
                                .defineInRange("power_coal", 48000, 1, 1000000);
                FLUID_MAX = BUILDER
                                .comment("Max fluid to store\n Default: 10000")
                                .defineInRange("fluid_max", 10000, 1, 30000);
                POWER_PER_RED = BUILDER
                                .comment("How much power one redstone adds\n Default: 1800")
                                .defineInRange("power_per_red", 1800, 1, 10000);
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {

        }

}
