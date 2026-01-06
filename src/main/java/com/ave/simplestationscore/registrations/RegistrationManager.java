package com.ave.simplestationscore.registrations;

import java.util.Arrays;
import java.util.function.Function;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistrationManager {
        public final DeferredRegister<Block> BLOCKS;
        public final DeferredRegister<Item> ITEMS;
        public final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
        public final DeferredRegister<MenuType<?>> MENUS;

        public static BlockBehaviour.Properties blockProps = BlockBehaviour.Properties.of().strength(3F)
                        .lightLevel((state) -> 11).requiresCorrectToolForDrops().noOcclusion();

        public RegistrationManager(String id) {
                BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, id);
                ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, id);
                BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, id);
                MENUS = DeferredRegister.create(net.minecraft.core.registries.Registries.MENU, id);
        }

        public void register(IEventBus bus) {
                BLOCKS.register(bus);
                ITEMS.register(bus);
                BLOCK_ENTITIES.register(bus);
                MENUS.register(bus);
        }

        public <B extends Block, BE extends BlockEntity> Station<BE, B> registerStation(String name,
                        Function<BlockBehaviour.Properties, B> blockFactory,
                        BlockEntityType.BlockEntitySupplier<BE> entityFactory) {
                return new Station<>(name, blockFactory, entityFactory, this, blockProps, true);
        }

        public <B extends Block, BE extends BlockEntity> Station<BE, B> registerEmptyStation(String name,
                        Function<BlockBehaviour.Properties, B> blockFactory,
                        BlockEntityType.BlockEntitySupplier<BE> entityFactory) {
                return new Station<>(name, blockFactory, entityFactory, this, blockProps, false);
        }

        @SuppressWarnings("unchecked")
        public RegistryObject<Item>[] registerEmptyItems(String prefix, String[] list) {
                return Arrays.stream(list).map(x -> registerItem(prefix + x)).toArray(RegistryObject[]::new);
        }

        @SuppressWarnings("unchecked")
        public RegistryObject<Block>[] registerEmptyBlocks(String prefix, String[] list) {
                return Arrays.stream(list).map(x -> registerBlock(prefix + x)).toArray(RegistryObject[]::new);
        }

        public RegistryObject<Item> registerItem(String name) {
                return ITEMS.register(name, () -> new Item(new Item.Properties()));
        }

        public RegistryObject<Block> registerBlock(String name) {
                return BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.of()));
        }

        public <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
                        String name, IContainerFactory<T> factory) {
                return MENUS.register(name, () -> IForgeMenuType.create(factory));
        }
}
