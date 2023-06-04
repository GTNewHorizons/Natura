package mods.natura.plugins.waila;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mods.natura.blocks.crops.CropBlock;
import mods.natura.blocks.trees.NLeaves;
import mods.natura.blocks.trees.NLeavesDark;
import mods.natura.blocks.trees.NLeavesNocolor;
import mods.natura.blocks.trees.OverworldLeaves;

public class WailaRegistrar {

    public static void wailaCallback(IWailaRegistrar registrar) {
        IWailaDataProvider cropProvider = new NaturaCropDataProvider();
        IWailaDataProvider leavesProvider = new NaturaLeavesDataProvider();

        registrar.registerStackProvider(cropProvider, CropBlock.class);
        registrar.registerBodyProvider(cropProvider, CropBlock.class);

        registrar.registerStackProvider(leavesProvider, NLeaves.class);
        registrar.registerStackProvider(leavesProvider, NLeavesNocolor.class);
        registrar.registerStackProvider(leavesProvider, NLeavesDark.class);
        registrar.registerStackProvider(leavesProvider, OverworldLeaves.class);
    }
}
