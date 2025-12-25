package com.ave.simplestationscore.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
        public DeferredRegister<MenuType<?>> MENUS;

        public ModMenuTypes(String id) {
                MENUS = DeferredRegister.create(net.minecraft.core.registries.Registries.MENU, id);
        }

        public void register(IEventBus eventBus) {
                MENUS.register(eventBus);
        }

        public <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(
                        String name, IContainerFactory<T> factory) {
                return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
        }
}
