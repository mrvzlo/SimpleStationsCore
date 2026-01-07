package com.ave.simplestationscore.partblock;

import com.ave.simplestationscore.mainblock.BaseStationBlock;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PartBlock extends Block implements EntityBlock {
    public static final MapCodec<BlockPos> CONTROLLER_POS_CODEC = BlockPos.CODEC.fieldOf("controller");

    public PartBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PartBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return InteractionResult.SUCCESS;

        BlockPos ctrlPos = part.getControllerPos();
        BaseStationBlock ctrl = (BaseStationBlock) level.getBlockState(ctrlPos).getBlock();
        ctrl.use(state, level, ctrlPos, player, hand, hit);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return;

        BlockPos controllerPos = part.getControllerPos();
        level.destroyBlock(controllerPos, !player.isCreative());
    }
}
