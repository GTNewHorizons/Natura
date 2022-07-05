package mods.natura.plugins.waila;

import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mods.natura.blocks.crops.CropBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class NaturaCropDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (accessor.getBlock() instanceof CropBlock) {
            int meta = accessor.getMetadata();
            CropBlock cropBlock = (CropBlock) block;

            return new ItemStack(cropBlock.getCropItem(meta), 1, cropBlock.damageDropped(meta));
        }
        return null;
    }

    @Override
    public List<String> getWailaHead(
            ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(
            ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (config.getConfig("general.showcrop")) {
            Block block = accessor.getBlock();

            if (block instanceof CropBlock) {
                int meta = accessor.getMetadata();
                float startGrowth = ((CropBlock) block).getStartGrowth(meta);
                float maxGrowth = ((CropBlock) block).getMaxGrowth(meta) - startGrowth;
                float growthValue;

                growthValue = ((meta - startGrowth) / maxGrowth) * 100.0F;

                if (growthValue < 100.0) {
                    currenttip.add(
                            StatCollector.translateToLocalFormatted("tooltip.waila.growth.percentage", growthValue));
                } else {
                    currenttip.add(StatCollector.translateToLocal("tooltip.waila.growth.mature"));
                }
                return currenttip;
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(
            ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(
            EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
    }
}
