package mods.natura.plugins;

import static mods.natura.Natura.pulsar;

import mods.natura.plugins.fmp.FMPPulse;
import mods.natura.plugins.imc.BuildCraftPulse;
import mods.natura.plugins.imc.ForestryPulse;
import mods.natura.plugins.imc.TreeCapitatorPulse;
import mods.natura.plugins.minefactoryreloaded.MFRPulse;
import mods.natura.plugins.nei.NEIPulse;
import mods.natura.plugins.te4.TE4Pulse;
import mods.natura.plugins.thaumcraft.ThaumcraftPulse;
import mods.natura.plugins.waila.WailaPulse;

/**
 * Master controller for Natura compat plugins.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class PluginController {

    private PluginController() {} // Not to be instantiated.

    public static void registerBuiltins() {
        pulsar.registerPulse(new TE4Pulse());
        pulsar.registerPulse(new BuildCraftPulse());
        pulsar.registerPulse(new ForestryPulse());
        pulsar.registerPulse(new TreeCapitatorPulse());
        pulsar.registerPulse(new ThaumcraftPulse());
        pulsar.registerPulse(new FMPPulse());
        pulsar.registerPulse(new NEIPulse());
        pulsar.registerPulse(new MFRPulse());
        pulsar.registerPulse(new WailaPulse());
    }
}
