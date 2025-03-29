package mods.natura.items.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.blocks.PlankSlab1;

public class PlankSlab1Item extends ItemSlab {

    public PlankSlab1Item(Block block, PlankSlab1 singleSlab, PlankSlab1 doubleSlab) {
        super(block, singleSlab, doubleSlab, block == doubleSlab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        switch (stack.getItemDamage()) {
            case 0:
                list.add(StatCollector.translateToLocal("tooltip.tree1"));
                break;
            case 1:
                list.add(StatCollector.translateToLocal("tooltip.tree2"));
                break;
            case 2:
                list.add(StatCollector.translateToLocal("tooltip.nethertree"));
                list.add(StatCollector.translateToLocal("tooltip.tree3"));
                break;
            case 3:
                list.add(StatCollector.translateToLocal("tooltip.tree4"));
                break;
            case 4:
                list.add(StatCollector.translateToLocal("tooltip.nethertree"));
                list.add(StatCollector.translateToLocal("tooltip.tree5"));
                break;
            case 5:
                list.add(StatCollector.translateToLocal("tooltip.tree6"));
                break;
            case 6:
                list.add(StatCollector.translateToLocal("tooltip.tree7"));
                break;
            case 7:
                list.add(StatCollector.translateToLocal("tooltip.tree8"));
                break;
        }
    }
}
