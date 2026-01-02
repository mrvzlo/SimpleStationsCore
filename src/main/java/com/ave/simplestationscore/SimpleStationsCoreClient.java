package com.ave.simplestationscore;

import com.ave.simplestationscore.partblock.PartBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(value = SimpleStationsCore.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = SimpleStationsCore.MODID, value = Dist.CLIENT)
public class SimpleStationsCoreClient {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        PartBlockEntity.registerCaps(event);
    }
}
