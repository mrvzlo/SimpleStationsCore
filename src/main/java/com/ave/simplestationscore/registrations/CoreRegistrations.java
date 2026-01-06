package com.ave.simplestationscore.registrations;

import com.ave.simplestationscore.SimpleStationsCore;
import com.ave.simplestationscore.partblock.PartBlock;
import com.ave.simplestationscore.partblock.PartBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CoreRegistrations {
        private static RegistrationManager MANAGER = new RegistrationManager(SimpleStationsCore.MODID);

        public static final RegistryObject<Item> CRATE = MANAGER.registerItem("crate");
        public static Station<PartBlockEntity, PartBlock> PART = MANAGER.registerEmptyStation("part",
                        (p) -> new PartBlock(p), PartBlockEntity::new);

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
                        .create(Registries.CREATIVE_MODE_TAB, SimpleStationsCore.MODID);

        public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS
                        .register("simple_stations", () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.simplestationscore"))
                                        .withTabsBefore(CreativeModeTabs.COMBAT)
                                        .icon(() -> CoreRegistrations.CRATE.get().getDefaultInstance())
                                        .build());

        public static void register(IEventBus bus) {
                MANAGER.register(bus);
                CREATIVE_MODE_TABS.register(bus);
        }
}
