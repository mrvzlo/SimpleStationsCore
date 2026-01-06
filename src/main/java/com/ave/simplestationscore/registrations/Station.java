package com.ave.simplestationscore.registrations;

import java.util.function.Function;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public class Station<BE extends BlockEntity, B extends Block> {
    private final RegistryObject<BlockEntityType<BE>> entity;
    private final RegistryObject<Block> block;
    private final RegistryObject<BlockItem> item;

    public Station(String name,
            Function<BlockBehaviour.Properties, B> blockFactory,
            BlockEntityType.BlockEntitySupplier<BE> entityFactory, RegistrationManager manager,
            BlockBehaviour.Properties props, boolean createItem) {
        this.block = manager.BLOCKS.register(name, () -> blockFactory.apply(props));

        this.entity = manager.BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder
                .of(entityFactory, this.block.get()).build(null));

        this.item = createItem ? manager.ITEMS.register(name,
                () -> new ItemNameBlockItem(this.block.get(), new Item.Properties())) : null;
    }

    public BlockEntityType<BE> getEntity() {
        return entity.get();
    }

    public Block getBlock() {
        return block.get();
    }

    public BlockItem getItem() {
        return item.get();
    }
}