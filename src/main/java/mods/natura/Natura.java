package mods.natura;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import mantle.lib.TabTools;
import mantle.pulsar.control.PulseManager;
import mods.natura.common.NContent;
import mods.natura.common.NProxyCommon;
import mods.natura.common.NaturaTab;
import mods.natura.common.PHNatura;
import mods.natura.dimension.NetheriteWorldProvider;
import mods.natura.gui.NGuiHandler;
import mods.natura.plugins.PluginController;
import mods.natura.worldgen.BaseCloudWorldgen;
import mods.natura.worldgen.BaseCropWorldgen;
import mods.natura.worldgen.BaseTreeWorldgen;
import mods.natura.worldgen.retro.TickHandlerWorld;
import mods.natura.worldgen.retro.WorldHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = "Natura",
        name = "Natura",
        version = "GRADLETOKEN_VERSION",
        acceptedMinecraftVersions = "[1.7.10]",
        dependencies = "required-after:Mantle;after:TConstruct")
public class Natura {
    /* Proxies for sides, used for graphics processing */
    @SidedProxy(clientSide = "mods.natura.client.NProxyClient", serverSide = "mods.natura.common.NProxyCommon")
    public static NProxyCommon proxy;

    public static final String modID = "Natura";
    /* Instance of this mod, used for grabbing prototype fields */
    @Instance(modID)
    public static Natura instance;

    public static Material cloud = new CloudMaterial();

    public static Logger logger = LogManager.getLogger(modID);

    public static final PulseManager pulsar = new PulseManager(modID, "Natura-Dynamic");

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {

        MinecraftForge.EVENT_BUS.register(this);

        PluginController.registerBuiltins();

        PHNatura.initProps(evt.getSuggestedConfigurationFile());
        NaturaTab.tab = new TabTools("natura.plants");

        content = new NContent();
        content.preInit();
        content.addOredictSupport();
        NaturaTab.tab.init(new ItemStack(NContent.floraSapling, 1, 3));

        pulsar.preInit(evt);
    }

    public static BaseCropWorldgen crops;
    public static BaseCloudWorldgen clouds;
    public static BaseTreeWorldgen trees;

    public static final int DIM_WORLDGEN_CROP_BIT = 1;
    public static final int DIM_WORLDGEN_CLOUD_BIT = 2;
    public static final int DIM_WORLDGEN_TREE_BIT = 4;
    private static final Map<Integer, Integer> dimensionWorldgenOverrides = new HashMap<>();

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        if (PHNatura.enableBerryBushes | PHNatura.enableNetherBerryBushes)
            GameRegistry.registerWorldGenerator(
                    crops = new BaseCropWorldgen(), 20); // TODO 1.7 Find correct weight (param 2)
        GameRegistry.registerWorldGenerator(
                clouds = new BaseCloudWorldgen(), 20); // TODO 1.7 Find correct weight (param 2)
        GameRegistry.registerWorldGenerator(
                trees = new BaseTreeWorldgen(), 20); // TODO 1.7 Find correct weight (param 2)

        proxy.registerRenderer();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new NGuiHandler());

        GameRegistry.registerFuelHandler(content);

        if (PHNatura.overrideNether) {
            DimensionManager.unregisterProviderType(-1);
            DimensionManager.registerProviderType(-1, NetheriteWorldProvider.class, true);
        }
        MinecraftForge.EVENT_BUS.register(WorldHandler.instance);
        FMLCommonHandler.instance().bus().register(this);

        if (retrogen) {
            FMLCommonHandler.instance().bus().register(new TickHandlerWorld());
        }

        OreDictionary.registerOre("cropVine", new ItemStack(NContent.thornVines));
        random.setSeed(2 ^ 16 + 2 ^ 8 + (4 * 3 * 271));

        pulsar.init(evt);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        content.createEntities();
        content.modIntegration();

        pulsar.postInit(evt);
        imcHandler();
    }

    /**
     * Runtime IMC Handler
     *
     * Message tag: set-worldgen-overrides
     * Message NBT data:
     *  dimensions: int[] - array of dimension IDs to update
     *  settings: int[] - array of settings to set for the corresponding dimension IDs
     *  Both arrays must be of the same length
     *  Settings format: integer with bitfields, enable bits: 1 = crops (berry bushes), 2 = clouds, 4 = trees
     */
    @SubscribeEvent
    public void tickEvent(TickEvent.ServerTickEvent event) {
        if (event.side.isServer() && event.phase.equals(TickEvent.Phase.START)) {
            imcHandler();
        }
    }

    private void imcHandler() {
        List<FMLInterModComms.IMCMessage> imc = FMLInterModComms.fetchRuntimeMessages(this);
        if (imc != null && !imc.isEmpty()) {
            for (FMLInterModComms.IMCMessage message : imc) {
                try {
                    if (message.key.equalsIgnoreCase("set-worldgen-overrides") && message.isNBTMessage()) {
                        NBTTagCompound tag = message.getNBTValue();
                        int[] dimensions = tag.getIntArray("dimensions");
                        int[] settings = tag.getIntArray("settings");
                        if (dimensions == null || settings == null || dimensions.length != settings.length) {
                            FMLLog.warning("Invalid Natura IMC format, mismatched array lengths");
                            return;
                        }
                        synchronized (dimensionWorldgenOverrides) {
                            for (int i = 0; i < dimensions.length; i++) {
                                dimensionWorldgenOverrides.put(dimensions[i], settings[i]);
                            }
                        }
                    }
                } catch (Exception e) {
                    FMLLog.warning("Exception while handling a Natura IMC message `{}`", message.key, e);
                }
            }
        }
    }

    public static int getDimensionWorldgenOverrides(int dimId) {
        Integer val = dimensionWorldgenOverrides.get(dimId);
        return (val != null) ? val : Integer.MAX_VALUE;
    }

    @SubscribeEvent
    public void bonemealEvent(BonemealEvent event) {
        if (!event.world.isRemote && !event.isCanceled() && event.getResult() != Result.ALLOW) {
            if (event.block == NContent.glowshroom) {
                if (NContent.glowshroom.fertilizeMushroom(event.world, event.x, event.y, event.z, event.world.rand))
                    event.setResult(Result.ALLOW);
            } else if (PHNatura.enableBerryBushes && event.block == NContent.berryBush) {
                if (NContent.berryBush.boneFertilize(event.world, event.x, event.y, event.z, event.world.rand))
                    event.setResult(Result.ALLOW);
            } else if (PHNatura.enableNetherBerryBushes && event.block == NContent.netherBerryBush) {
                if (NContent.netherBerryBush.boneFertilize(event.world, event.x, event.y, event.z, event.world.rand))
                    event.setResult(Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void interactEvent(EntityInteractEvent event) {
        // if (event.target == null)
        if (event.target instanceof EntityCow || event.target instanceof EntitySheep) {
            ItemStack equipped = event.entityPlayer.getCurrentEquippedItem();
            EntityAnimal creature = (EntityAnimal) event.target;
            if (equipped != null
                    && equipped.getItem() == NContent.plantItem
                    && equipped.getItemDamage() == 0
                    && creature.getGrowingAge() == 0
                    && !creature.isInLove()) {
                EntityPlayer player = event.entityPlayer;
                if (!player.capabilities.isCreativeMode) {
                    --equipped.stackSize;

                    if (equipped.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                }

                creature.func_146082_f(event.entityPlayer);
            }
        }
    }

    @SubscribeEvent
    public void spawnEvent(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityCow || event.entity instanceof EntitySheep) {
            ((EntityLiving) event.entity)
                    .tasks.addTask(
                            3, new EntityAITempt((EntityCreature) event.entity, 0.25F, NContent.plantItem, false));
        } else if (event.entity instanceof EntityChicken) {
            ((EntityLiving) event.entity)
                    .tasks.addTask(3, new EntityAITempt((EntityCreature) event.entity, 0.25F, NContent.seeds, false));
        }
    }

    public static boolean retrogen;

    @SubscribeEvent
    public void chunkDataSave(ChunkDataEvent.Save event) {
        event.getData().setBoolean("Natura.Retrogen", true);
    }

    NContent content;
    public static Random random = new Random();
}
