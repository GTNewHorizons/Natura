package mods.natura.plugins.minefactoryreloaded;

import mods.natura.common.NContent;
import mods.natura.common.PHNatura;
import mods.natura.plugins.minefactoryreloaded.fertilizables.FertilizableNaturaCrop;
import mods.natura.plugins.minefactoryreloaded.fertilizables.FertilizableSapling;
import mods.natura.plugins.minefactoryreloaded.harvestables.HarvestableNaturaBerry;
import mods.natura.plugins.minefactoryreloaded.harvestables.HarvestableNaturaCropPlant;
import mods.natura.plugins.minefactoryreloaded.harvestables.HarvestableNaturaTreeLeaves;
import mods.natura.plugins.minefactoryreloaded.harvestables.HarvestableStandard;
import mods.natura.plugins.minefactoryreloaded.plantables.PlantableNaturaBerry;
import mods.natura.plugins.minefactoryreloaded.plantables.PlantableNaturaCrop;
import mods.natura.plugins.minefactoryreloaded.plantables.PlantableNaturaNetherBerry;
import mods.natura.plugins.minefactoryreloaded.plantables.PlantableStandard;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import powercrystals.minefactoryreloaded.api.HarvestType;

public class MRFRegistering {
    public static void registerWithMFR() {
        String registerPlantable = "registerPlantable";
        String registerHarvestable = "registerHarvestable";
        String registerFertilizable = "registerFertilizable";
        String registerSludgeDrop = "registerSludgeDrop";

        // Berry Bushes
        if (PHNatura.enableNetherBerryBushes) {
            FactoryRegistry.sendMessage(
                    registerPlantable,
                    new PlantableNaturaNetherBerry(
                            Item.getItemFromBlock(NContent.netherBerryBush), NContent.netherBerryBush));
            FactoryRegistry.sendMessage(
                    registerHarvestable,
                    new HarvestableNaturaBerry(NContent.netherBerryBush, NContent.netherBerryItem));
        }
        if (PHNatura.enableBerryBushes) {
            FactoryRegistry.sendMessage(
                    registerPlantable,
                    new PlantableNaturaBerry(Item.getItemFromBlock(NContent.berryBush), NContent.berryBush));
            FactoryRegistry.sendMessage(
                    registerHarvestable, new HarvestableNaturaBerry(NContent.berryBush, NContent.berryItem));
        }

        // Crops
        FactoryRegistry.sendMessage(registerPlantable, new PlantableNaturaCrop(NContent.seeds, NContent.crops));
        FactoryRegistry.sendMessage(registerPlantable, new PlantableStandard(NContent.seedFood, NContent.saguaro));
        FactoryRegistry.sendMessage(
                registerPlantable,
                new PlantableStandard(Item.getItemFromBlock(NContent.floraSapling), NContent.floraSapling));

        // misc plants
        FactoryRegistry.sendMessage(
                registerHarvestable, new HarvestableStandard(NContent.bluebells, HarvestType.Normal));
        // glowshrooms
        FactoryRegistry.sendMessage(
                registerHarvestable, new HarvestableStandard(NContent.glowshroom, HarvestType.Normal));
        FactoryRegistry.sendMessage(
                registerHarvestable, new HarvestableStandard(NContent.glowshroomBlue, HarvestType.Normal));
        FactoryRegistry.sendMessage(
                registerHarvestable, new HarvestableStandard(NContent.glowshroomGreen, HarvestType.Normal));
        FactoryRegistry.sendMessage(
                registerHarvestable, new HarvestableStandard(NContent.glowshroomPurple, HarvestType.Normal));
        // crops
        FactoryRegistry.sendMessage(
                registerHarvestable, new HarvestableNaturaCropPlant(NContent.crops, NContent.plantItem));

        // trees
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableStandard(NContent.tree, HarvestType.Tree));
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableStandard(NContent.rareTree, HarvestType.Tree));
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableStandard(NContent.darkTree, HarvestType.Tree));
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableStandard(NContent.redwood, HarvestType.Tree));
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableStandard(NContent.willow, HarvestType.Tree));
        FactoryRegistry.sendMessage(
                registerHarvestable, new HarvestableStandard(NContent.bloodwood, HarvestType.TreeFlipped));
        // Nether plants
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableNaturaTreeLeaves(NContent.thornVines));
        // leaves
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableNaturaTreeLeaves(NContent.rareLeaves));
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableNaturaTreeLeaves(NContent.darkLeaves));
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableNaturaTreeLeaves(NContent.floraLeaves));
        FactoryRegistry.sendMessage(registerHarvestable, new HarvestableNaturaTreeLeaves(NContent.floraLeavesNoColor));

        FactoryRegistry.sendMessage(registerFertilizable, new FertilizableNaturaCrop(NContent.crops));
        FactoryRegistry.sendMessage(registerFertilizable, new FertilizableSapling(NContent.floraSapling));
        FactoryRegistry.sendMessage(registerFertilizable, new FertilizableSapling(NContent.rareSapling));

        /*
         *  The sludge boiler takes sludge from harvester machines and boils it to get soil-like items,
         *  such as dirt, sand, clay, or rarely things like soulsand and mycelium.
         */
        NBTTagCompound heatSand = new ItemStack(NContent.heatSand).writeToNBT(new NBTTagCompound());
        heatSand.setInteger("value", 5);
        FactoryRegistry.sendMessage(registerSludgeDrop, heatSand);

        NBTTagCompound taintedSoil = new ItemStack(NContent.taintedSoil).writeToNBT(new NBTTagCompound());
        taintedSoil.setInteger("value", 5);
        FactoryRegistry.sendMessage(registerSludgeDrop, taintedSoil);
    }
}
