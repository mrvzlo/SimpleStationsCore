package com.ave.simplestationscore.registrations;

import java.util.Arrays;
import java.util.function.Function;

import com.ave.simplestationscore.partblock.PartBlock;
import com.ave.simplestationscore.partblock.PartBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistrationManager {
        public final DeferredRegister.Blocks BLOCKS;
        public final DeferredRegister.Items ITEMS;
        public final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
        public final DeferredRegister<MenuType<?>> MENUS;

        public static Station<PartBlockEntity, PartBlock> PART;
        public static BlockBehaviour.Properties blockProps = BlockBehaviour.Properties.of().strength(0.1F)
                        .lightLevel((state) -> 11)
                        .noOcclusion();

        public RegistrationManager(String id) {
                BLOCKS = DeferredRegister.createBlocks(id);
                ITEMS = DeferredRegister.createItems(id);
                BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, id);
                PART = registerEmptyStation("part", (p) -> new PartBlock(p), PartBlockEntity::new);
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
        public DeferredItem<Item>[] registerEmptyItems(String prefix, String[] list) {
                return Arrays.stream(list).map(x -> registerItem(prefix + x)).toArray(DeferredItem[]::new);
        }

        @SuppressWarnings("unchecked")
        public DeferredBlock<Block>[] registerEmptyBlocks(String prefix, String[] list) {
                return Arrays.stream(list).map(x -> registerBlock(prefix + x)).toArray(DeferredBlock[]::new);
        }

        public DeferredItem<Item> registerItem(String name) {
                return ITEMS.registerItem(name, Item::new, new Item.Properties());
        }

        public DeferredBlock<Block> registerBlock(String name) {
                return BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.of()));
        }

        public <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(
                        String name, IContainerFactory<T> factory) {
                return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
        }
}
