package com.ave.simplestationscore.partblock;

import net.minecraft.core.BlockPos;

public interface PartialEntity {
    public void setControllerPos(BlockPos pos);

    public BlockPos getControllerPos();
}
