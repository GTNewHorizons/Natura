package mods.natura.common;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Loader;
import mods.natura.Natura;

public class PHNatura {

    public static void initProps(File confFile) {
        // Category names
        final String catDisabler = "disabler";
        final String catWorldgen = "worldgen";
        final String catMobChanges = "mob changes";
        final String catGeneral = "general";

        final String catWoodProducts = "wood products";
        final String catOverworldProducts = "overworld products";
        final String catNetherProducts = "nether products";

        /* [Forge] Configuration class, used as config method */
        Configuration config = new Configuration(confFile);
        /* Load the configuration file */
        config.load();

        Natura.retrogen = config.get("Retrogen", "Retroactive Generation", false).getBoolean(false);

        boolean BoP = false;
        if (Loader.isModLoaded("BiomesOPlenty")) {
            BoP = true;
        }

        babyHeatscarMinimum = config.get(catMobChanges, "Minimum Baby Heatscar Spiders on Spider Death", 2).getInt(2);
        if (babyHeatscarMinimum < 0) babyHeatscarMinimum = 0;
        babyHeatscarMaximum = config.get(catMobChanges, "Maximum Baby Heatscar Spiders on Spider Death", 4).getInt(4);
        if (babyHeatscarMaximum < 0) babyHeatscarMaximum = 0;
        overrideNether = config.get(catDisabler, "Override Nether", !BoP).getBoolean(!BoP);
        canRespawnInNether = config.get(catDisabler, "Obelisks let players respawn in the Nether", true)
                .getBoolean(true);

        generateRedwood = config.get(catDisabler, "Generate Redwood Trees", true).getBoolean(true);
        generateSakura = config.get(catDisabler, "Generate Sakura Trees", true).getBoolean(true);
        generateSmallEucalyptus = config.get(catDisabler, "Generate Small Eucalyptus Trees", true).getBoolean(true);
        generateBush = config.get(catDisabler, "Generate Hopseed Trees", true).getBoolean(true);
        generateBloodwood = config.get(catDisabler, "Generate Bloodwood Trees", true).getBoolean(true);
        generateGhost = config.get(catDisabler, "Generate Ghost Trees", true).getBoolean(true);
        generateSaguaro = config.get(catDisabler, "Generate Saguaro Cactus", true).getBoolean(true);

        generateOverworldClouds = config.get(catDisabler, "Generate Overworld Clouds", true).getBoolean(true);
        generateSulfurClouds = config.get(catDisabler, "Generate Sulfur Clouds", true).getBoolean(true);
        generateAshClouds = config.get(catDisabler, "Generate Ash Clouds", true).getBoolean(true);
        generateDarkClouds = config.get(catDisabler, "Generate Dark Clouds", true).getBoolean(true);

        generatePurpleheart = config.get(catDisabler, "Generate Amaranth Trees", true).getBoolean(true);
        generateWillow = config.get(catDisabler, "Generate Willow Trees", true).getBoolean(true);
        generateTiger = config.get(catDisabler, "Generate Tigerwood Trees", true).getBoolean(true);
        generateSilverbell = config.get(catDisabler, "Generate Silverbell Trees", true).getBoolean(true);
        generateMaple = config.get(catDisabler, "Generate Maple Trees", true).getBoolean(true);

        generateDarkwood = config.get(catDisabler, "Generate Darkwood Trees", true).getBoolean(true);
        generateFusewood = config.get(catDisabler, "Generate Fusewood Trees", true).getBoolean(true);
        generateThornvines = config.get(catDisabler, "Generate Thornvines", true).getBoolean(true);

        generateBarley = config.get(catDisabler, "Generate Barley Crops", true).getBoolean(true);
        generateCotton = config.get(catDisabler, "Generate Cotton Crops", true).getBoolean(true);
        generateBluebells = config.get(catDisabler, "Generate Bluebell Flowers", true).getBoolean(true);

        generateBlueberries = config.get(catDisabler, "Generate Blueberry Bushes", true).getBoolean(true);
        generateBlackberries = config.get(catDisabler, "Generate Blackberry Bushes", true).getBoolean(true);
        generateRaspberries = config.get(catDisabler, "Generate Raspberry Bushes", true).getBoolean(true);
        generateMaloberries = config.get(catDisabler, "Generate Maloberry Bushes", true).getBoolean(true);

        generateBlightberries = config.get(catDisabler, "Generate Blightberry Bushes", true).getBoolean(true);
        generateDuskberries = config.get(catDisabler, "Generate Duskberry Bushes", true).getBoolean(true);
        generateSkyberries = config.get(catDisabler, "Generate Skyberry Bushes", true).getBoolean(true);
        generateStingberries = config.get(catDisabler, "Generate Stingberry Bushes", true).getBoolean(true);

        generateGreenglowshroom = config.get(catDisabler, "Generate Green Glowshroom", true).getBoolean(true);
        generatePurpleglowshroom = config.get(catDisabler, "Generate Purple Glowshroom", true).getBoolean(true);
        generateBlueglowshroom = config.get(catDisabler, "Generate Blue Glowshroom", true).getBoolean(true);
        generateGlowshroomtree = config.get(catDisabler, "Generate Glowshroom Trees", true).getBoolean(true);
        dropCotton = config.get(catDisabler, "Drop cotton seeds from grass", true).getBoolean(true);
        dropBarley = config.get(catDisabler, "Drop barley seeds from grass", true).getBoolean(true);

        enableNitroCreepers = config.get(catDisabler, "Enable Nitro Creepers", true).getBoolean(true);
        enableImps = config.get(catDisabler, "Enable Imps", true).getBoolean(true);
        enableHeatscarSpiders = config.get(catDisabler, "Enable Heatscar Spiders", true).getBoolean(true);
        if (enableNitroCreepers | enableImps | enableHeatscarSpiders) anyMobsEnabled = true;

        try {
            Class.forName("chococraft.common.ModChocoCraft");
            enableWheatRecipe = config.get(catDisabler, "Enable wheat to flour recipe", false).getBoolean(false);
        } catch (Exception e) {
            enableWheatRecipe = config.get(catDisabler, "Enable wheat to flour recipe", true).getBoolean(true);
        }

        redwoodSpawnRarity = config.get(catWorldgen, "Redwood Tree Spawn Rarity", 150).getInt(150);
        bloodSpawnRarity = config.get(catWorldgen, "Blood Tree Spawn Rarity", 14).getInt(14);
        eucalyptusShortSpawnRarity = config.get(catWorldgen, "Small Eucalyptus Tree Spawn Rarity", 25).getInt(25);
        eucalyptusShortSpawnRange = config.get(catWorldgen, "Small Eucalyptus Tree Spawn Range", 32).getInt(32);
        sakuraSpawnRarity = config.get(catWorldgen, "Sakura Tree Spawn Rarity", 10).getInt(10);
        sakuraSpawnRange = config.get(catWorldgen, "Sakura Tree Spawn Range", 32).getInt(32);
        ghostSpawnRarity = config.get(catWorldgen, "Ghostwood Tree Spawn Rarity", 10).getInt(10);
        bushSpawnRarity = config.get(catWorldgen, "Bush Tree Spawn Rarity", 10).getInt(10);
        bushSpawnRange = config.get(catWorldgen, "Bush Tree Spawn Range", 20).getInt(20);

        willowRarity = config.get(catWorldgen, "Willow Tree Spawn Rarity", 10).getInt(10);
        purpleheartRarity = config.get(catWorldgen, "Amaranth Tree Spawn Rarity", 1).getInt(1);
        mapleRarity = config.get(catWorldgen, "Maple Tree Spawn Rarity", 34).getInt(34);
        tigerRarity = config.get(catWorldgen, "Tigerwood Tree Spawn Rarity", 30).getInt(30);
        silverbellRarity = config.get(catWorldgen, "Silverbell Tree Spawn Rarity", 70).getInt(70);

        darkSpawnRarity = config.get(catWorldgen, "Darkwood Tree Spawn Rarity", 10).getInt(10);
        fuseSpawnRarity = config.get(catWorldgen, "Fusewood Tree Spawn Rarity", 50).getInt(50);

        saguaroSpawnRarity = config.get(catWorldgen, "Saguaro Cactus Spawn Rarity", 5).getInt(5);

        cloudSpawnRarity = config.get(catWorldgen, "Cloud Spawn Rarity", 10).getInt(10);
        cloudSpawnHeight = config.get(catWorldgen, "Cloud Spawn Height", 192).getInt(192);
        cloudSpawnRange = config.get(catWorldgen, "Cloud Spawn Range", 48).getInt(48);
        darkCloudSpawnRarity = config.get(catWorldgen, "Dark Cloud Spawn Density", 10).getInt(10);
        darkCloudSpawnHeight = config.get(catWorldgen, "Dark Cloud Spawn MinX", 0).getInt(64);
        darkCloudSpawnRange = config.get(catWorldgen, "Dark Cloud Spawn Range", 256).getInt(256);
        sulfurSpawnRarity = config.get(catWorldgen, "Sulfur Cloud Spawn Rarity", 8).getInt(8);
        sulfurSpawnHeight = config.get(catWorldgen, "Sulfur Cloud Spawn Height", 40).getInt(40);
        sulfurSpawnRange = config.get(catWorldgen, "Sulfur Cloud Spawn Range", 78).getInt(78);
        ashSpawnRarity = config.get(catWorldgen, "Ash Cloud Spawn Rarity", 8).getInt(8);
        ashSpawnHeight = config.get(catWorldgen, "Ash Cloud Spawn Height", 40).getInt(40);
        ashSpawnRange = config.get(catWorldgen, "Ash Cloud Spawn Range", 78).getInt(78);

        raspSpawnRarity = config.get(catWorldgen, "Raspberry Spawn Rarity", 30).getInt(30);
        raspSpawnRange = config.get(catWorldgen, "Raspberry Spawn Range", 64).getInt(64);
        blueSpawnRarity = config.get(catWorldgen, "Blueberry Spawn Rarity", 34).getInt(34);
        blueSpawnRange = config.get(catWorldgen, "Blueberry Spawn Range", 64).getInt(64);
        blackSpawnRarity = config.get(catWorldgen, "Blackberry Spawn Rarity", 48).getInt(48);
        blackSpawnRange = config.get(catWorldgen, "Blackberry Spawn Range", 64).getInt(64);
        geoSpawnRarity = config.get(catWorldgen, "Maloberry Spawn Rarity", 40).getInt(40);
        geoSpawnRange = config.get(catWorldgen, "Maloberry Spawn Range", 64).getInt(64);

        blightSpawnRarity = config.get(catWorldgen, "Blightberry Spawn Rarity", 18).getInt(18);
        blightSpawnRange = config.get(catWorldgen, "Blightberry Spawn Range", 100).getInt(100);
        duskSpawnRarity = config.get(catWorldgen, "Duskberry Spawn Rarity", 18).getInt(18);
        duskSpawnRange = config.get(catWorldgen, "Duskberry Spawn Range", 100).getInt(100);
        skySpawnRarity = config.get(catWorldgen, "Skyberry Spawn Rarity", 18).getInt(18);
        skySpawnRange = config.get(catWorldgen, "Skyberry Spawn Range", 100).getInt(100);
        stingSpawnRarity = config.get(catWorldgen, "Stingberry Spawn Rarity", 18).getInt(18);
        stingSpawnRange = config.get(catWorldgen, "Stingberry Spawn Range", 100).getInt(100);

        thornSpawnRarity = config.get(catWorldgen, "Thornvines Spawn Rarity", 40).getInt(40);
        darkCloudBlacklist = config.get(catWorldgen, "dimension blacklist(dark clouds)", new int[] {}).getIntList();
        cloudBlacklist = config.get(catWorldgen, "dimension blacklist(clouds)", new int[] {}).getIntList();
        sulfurCloudBlacklist = config.get(catWorldgen, "dimension blacklist(sulfur clouds)", new int[] {}).getIntList();

        enableWoodenBookshelves = config.get(catWoodProducts, "Enable Wooden Bookshevles", true).getBoolean(true);
        enableWoodenButtons = config.get(catWoodProducts, "Enable Wooden Buttons", true).getBoolean(true);
        enableWoodenDoors = config.get(catWoodProducts, "Enable Wooden Doors", true).getBoolean(true);
        enableWoodenFenceGates = config.get(catWoodProducts, "Enable Wooden Fence Gates", true).getBoolean(true);
        enableWoodenFences = config.get(catWoodProducts, "Enable Wooden Fences", true).getBoolean(true);
        enableWoodenPressurePlates = config.get(catWoodProducts, "Enable Wooden Pressure Plates", true)
                .getBoolean(true);
        enableWoodenSlabs = config.get(catWoodProducts, "Enable Wooden Slabs", true).getBoolean(true);
        enableWoodenStairs = config.get(catWoodProducts, "Enable Wooden Stairs", true).getBoolean(true);
        enableWoodenTrapdoors = config.get(catWoodProducts, "Enable Wooden Trapdoors", true).getBoolean(true);
        enableWoodenWorkbenches = config.get(catWoodProducts, "Enable Wooden Workbenches", true).getBoolean(true);

        enableBerryBushes = config.get(catOverworldProducts, "Enable Berries and Berry Bushes", true).getBoolean(true);
        enableBonemealBags = config.get(catOverworldProducts, "Enable Bonemeal Bags", true).getBoolean(true);
        enableCactusJuice = config.get(catOverworldProducts, "Enable Cactus Juice", true).getBoolean(true);
        enableSeedBags = config.get(catOverworldProducts, "Enable Seed Bags", true).getBoolean(true);

        enableBlazeHoppers = config.get(catNetherProducts, "Enable Blaze Hoppers", true).getBoolean(true);
        enableBlazeRails = config.get(catNetherProducts, "Enable Blaze Rails", true).getBoolean(true);
        enableFlintAndBlaze = config.get(catNetherProducts, "Enable Flint And Blaze", true).getBoolean(true);
        enableNetherBerryBushes = config.get(catNetherProducts, "Enable Nether Berries and Berry Bushes", true)
                .getBoolean(true);
        enableNetherButtons = config.get(catNetherProducts, "Enable Netherack Buttons", true).getBoolean(true);
        enableNetherFurnaces = config.get(catNetherProducts, "Enable Netherack Furnaces", true).getBoolean(true);
        enableNetherGlass = config.get(catNetherProducts, "Enable Nether Glass", true).getBoolean(true);
        enableNetherWoodTools = config.get(catNetherProducts, "Enable Nether Wood Tools", true).getBoolean(true);
        enableNetherLevers = config.get(catNetherProducts, "Enable Netherack Levers", true).getBoolean(true);
        enableNetherPressurePlates = config.get(catNetherProducts, "Enable Netherack Pressure Plates", true)
                .getBoolean(true);
        enableNetherWartBags = config.get(catNetherProducts, "Enable Nether Wart Bags", true).getBoolean(true);
        enableObelisks = config.get(catNetherProducts, "Enable Obelisks", true).getBoolean(true);
        enableQuartzTools = config.get(catNetherProducts, "Enable Quartz Tools", true).getBoolean(true);

        seaLevel = config.get(catGeneral, "Sea level", 64).getInt(64);

        /* Save the configuration file */
        if (config.hasChanged()) config.save();
    }

    public static int[] darkCloudBlacklist;
    public static int[] cloudBlacklist;
    public static int[] sulfurCloudBlacklist;
    /* Prototype fields, used elsewhere */

    public static int seaLevel;

    // Overworld

    public static boolean generateBarley;
    public static boolean generateCotton;
    public static boolean generateBluebells;
    public static boolean generateBlueberries;
    public static boolean generateBlackberries;
    public static boolean generateRaspberries;
    public static boolean generateMaloberries;

    public static boolean generateBlightberries;
    public static boolean generateDuskberries;
    public static boolean generateSkyberries;
    public static boolean generateStingberries;

    public static boolean generateGreenglowshroom;
    public static boolean generatePurpleglowshroom;
    public static boolean generateBlueglowshroom;
    public static boolean generateGlowshroomtree;

    public static int saguaroSpawnRarity;

    public static int raspSpawnRarity;
    public static int raspSpawnRange;
    public static int blueSpawnRarity;
    public static int blueSpawnRange;
    public static int blackSpawnRarity;
    public static int blackSpawnRange;
    public static int geoSpawnRarity;
    public static int geoSpawnRange;

    public static int blightSpawnRarity;
    public static int blightSpawnRange;
    public static int duskSpawnRarity;
    public static int duskSpawnRange;
    public static int skySpawnRarity;
    public static int skySpawnRange;
    public static int stingSpawnRarity;
    public static int stingSpawnRange;

    public static int thornSpawnRarity;

    // Clouds

    public static int cloudSpawnRarity;
    public static int cloudSpawnHeight;
    public static int cloudSpawnRange;
    public static int darkCloudSpawnRarity;
    public static int darkCloudSpawnHeight;
    public static int darkCloudSpawnRange;
    public static int sulfurSpawnRarity;
    public static int sulfurSpawnHeight;
    public static int sulfurSpawnRange;
    public static int ashSpawnRarity;
    public static int ashSpawnHeight;
    public static int ashSpawnRange;

    // Trees

    public static boolean generateRedwood;
    public static boolean generateSakura;
    public static boolean generateSmallEucalyptus;
    public static boolean generateBloodwood;
    public static boolean generateGhost;
    public static boolean generateBush;
    public static boolean generateSaguaro;

    public static boolean generatePurpleheart;
    public static boolean generateWillow;
    public static boolean generateTiger;
    public static boolean generateSilverbell;
    public static boolean generateMaple;

    public static boolean generateDarkwood;
    public static boolean generateFusewood;

    public static boolean generateThornvines;

    public static boolean generateOverworldClouds;
    public static boolean generateSulfurClouds;
    public static boolean generateAshClouds;
    public static boolean generateDarkClouds;
    public static boolean enableWheatRecipe;
    public static boolean dropBarley;
    public static boolean dropCotton;

    public static boolean overrideNether;
    public static boolean canRespawnInNether;

    public static int redwoodSpawnRarity;
    public static int bloodSpawnRarity;
    public static int eucalyptusShortSpawnRarity;
    public static int eucalyptusShortSpawnRange;
    public static int sakuraSpawnRarity;
    public static int sakuraSpawnRange;
    public static int ghostSpawnRarity;
    public static int bushSpawnRarity;
    public static int bushSpawnRange;
    public static int darkSpawnRarity;
    public static int fuseSpawnRarity;
    public static int purpleheartRarity;
    public static int mapleRarity;
    public static int willowRarity;
    public static int tigerRarity;
    public static int silverbellRarity;

    // Mobs

    public static int babyHeatscarMinimum;
    public static int babyHeatscarMaximum;

    public static boolean enableNitroCreepers;
    public static boolean enableImps;
    public static boolean enableHeatscarSpiders;
    public static boolean anyMobsEnabled = false;

    // Wood products

    public static boolean enableWoodenBookshelves;
    public static boolean enableWoodenButtons;
    public static boolean enableWoodenDoors;
    public static boolean enableWoodenFenceGates;
    public static boolean enableWoodenFences;
    public static boolean enableWoodenPressurePlates;
    public static boolean enableWoodenSlabs;
    public static boolean enableWoodenStairs;
    public static boolean enableWoodenTrapdoors;
    public static boolean enableWoodenWorkbenches;

    // Overworld products

    public static boolean enableBerryBushes;
    public static boolean enableBonemealBags;
    public static boolean enableCactusJuice;
    public static boolean enableSeedBags;

    // Nether products

    public static boolean enableBlazeHoppers;
    public static boolean enableBlazeRails;
    public static boolean enableFlintAndBlaze;
    public static boolean enableNetherBerryBushes;
    public static boolean enableNetherButtons;
    public static boolean enableNetherFurnaces;
    public static boolean enableNetherGlass;
    public static boolean enableNetherWoodTools;
    public static boolean enableNetherLevers;
    public static boolean enableNetherPressurePlates;
    public static boolean enableNetherWartBags;
    public static boolean enableObelisks;
    public static boolean enableQuartzTools;
}
