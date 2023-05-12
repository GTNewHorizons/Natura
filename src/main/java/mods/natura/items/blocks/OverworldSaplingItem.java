package mods.natura.items.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mantle.blocks.abstracts.MultiItemBlock;
import mods.natura.common.NContent;

public class OverworldSaplingItem extends MultiItemBlock {

    public static final String blockType[] = { "maple", "silverbell", "purpleheart", "tiger", "willow" };

    public OverworldSaplingItem(Block b) {
        super(b, "block.sapling", blockType);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        return NContent.rareSapling.getIcon(0, i);
    }

    /*
     * @Override public String getUnlocalizedName (ItemStack itemstack) { int i =
     * MathHelper.clamp_int(itemstack.getItemDamage(), 0, 4); return (new
     * StringBuilder()).append("block.sapling.").append(blockType[i]).toString(); }
     */

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        switch (stack.getItemDamage()) {
            case 0:
                list.add(StatCollector.translateToLocal("tooltip.tree7"));
                break;
            case 1:
                list.add(StatCollector.translateToLocal("tooltip.tree8"));
                break;
            case 2:
                list.add(StatCollector.translateToLocal("tooltip.tree9"));
                break;
            case 3:
                list.add(StatCollector.translateToLocal("tooltip.tree10"));
                break;
            case 4:
                list.add(StatCollector.translateToLocal("tooltip.tree11"));
                break;
        }
    }
}
