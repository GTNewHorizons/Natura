package mods.natura.plugins.nei;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import mantle.pulsar.pulse.Handler;
import mantle.pulsar.pulse.Pulse;
import mods.natura.Natura;

@Pulse(id = "Natura NEI Compatibility", modsRequired = NEIPulse.modId)
public class NEIPulse {

    public static final String modId = "NotEnoughItems";

    @Handler
    public void init(FMLInitializationEvent evt) {
        if (FMLCommonHandler.instance().getSide().isServer()) return;

        try {
            Natura.logger.debug("[NEI] Registering Natura NEI plugin.");
            NotEnoughItems.registerNEICompat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
