package com.ave.simplestationscore.resources;

import com.ave.simplestationscore.handlers.CommonItemHandler;

public class OptionalItemResource extends ItemResource {
    public OptionalItemResource(CommonItemHandler inventory, int slot, int usage) {
        super(inventory, slot, usage);
    }

    @Override
    public int getRequired() {
        return 0;
    }
}
