package com.ave.simplestationscore.registrations;

import java.util.Arrays;
import java.util.function.Function;

import com.ave.simplestationscore.partblock.PartBlock;
import com.ave.simplestationscore.partblock.PartBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistrationManager {
        public final DeferredRegister.Blocks BLOCKS;
        public final DeferredRegister.Items ITEMS;
        public final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
        public static Station<PartBlockEntity, PartBlock> PART;
        public static BlockBehaviour.Properties blockProps = BlockBehaviour.Properties.of().strength(0.1F)
                        .lightLevel((state) -> 11)
                        .noOcclusion();

        public RegistrationManager(String id) {
                BLOCKS = DeferredRegister.createBlocks(id);
                ITEMS = DeferredRegister.createItems(id);
                BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, id);
                PART = registerEmptyStation("part", (p) -> new PartBlock(p), PartBlockEntity::new);
        }

        public void register(IEventBus bus) {
                BLOCKS.register(bus);
                ITEMS.register(bus);
                BLOCK_ENTITIES.register(bus);
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

        public DeferredItem<Item>[] registerEmptyItems(String prefix, String[] list) {
                return Arrays.stream(list)
                                .map(x -> ITEMS.registerItem(prefix + x, Item::new, new Item.Properties()))
                                .toArray(DeferredItem[]::new);
        }

        public DeferredBlock<Block>[] registerEmptyBlocks(String prefix, String[] list) {
                return Arrays.stream(list)
                                .map(x -> BLOCKS.register(prefix + x, () -> new Block(BlockBehaviour.Properties.of())))
                                .toArray(DeferredBlock[]::new);
        }
}
