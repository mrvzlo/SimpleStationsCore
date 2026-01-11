package com.ave.simplestationscore.registrations;

import java.util.function.Function;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class Station<BE extends BlockEntity, B extends Block> {
    private final DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> entity;
    private final DeferredBlock<Block> block;
    private final DeferredItem<BlockItem> item;

    public Station(String name,
            Function<BlockBehaviour.Properties, B> blockFactory,
            BlockEntityType.BlockEntitySupplier<BE> entityFactory, RegistrationManager manager,
            BlockBehaviour.Properties props, boolean createItem) {
        this.block = manager.BLOCKS.register(name, () -> blockFactory.apply(props));

        this.entity = manager.BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder
                .of(entityFactory, this.block.get()).build(null));

        this.item = createItem ? manager.ITEMS.registerSimpleBlockItem(name, this.block) : null;
    }

    public BlockEntityType<BE> getEntity() {
        return entity.get();
    }

    public Block getBlock() {
        return block.get();
    }

    public BlockItem getItem() {
        return item == null ? null : item.get();
    }
}