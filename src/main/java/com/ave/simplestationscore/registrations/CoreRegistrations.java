package com.ave.simplestationscore.registrations;

import com.ave.simplestationscore.SimpleStationsCore;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CoreRegistrations {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SimpleStationsCore.MODID);

    public static final DeferredItem<Item> CRATE = ITEMS.registerItem("crate", Item::new, new Item.Properties());

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, SimpleStationsCore.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS
            .register("simple_stations", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.simplestationscore"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> CoreRegistrations.CRATE.get().getDefaultInstance())
                    .build());

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
    }
}
