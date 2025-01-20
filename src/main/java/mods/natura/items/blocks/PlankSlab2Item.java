package mods.natura.items.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.blocks.PlankSlab2;

public class PlankSlab2Item extends ItemSlab {

    public PlankSlab2Item(Block block, PlankSlab2 singleSlab, PlankSlab2 doubleSlab) {
        super(block, singleSlab, doubleSlab, block == doubleSlab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        switch (stack.getItemDamage()) {
            case 0:
                list.add(StatCollector.translateToLocal("tooltip.tree9"));
                break;
            case 1:
                list.add(StatCollector.translateToLocal("tooltip.tree10"));
                break;
            case 2:
                list.add(StatCollector.translateToLocal("tooltip.tree11"));
                break;
            case 3:
                list.add(StatCollector.translateToLocal("tooltip.nethertree"));
                break;
            case 4:
                list.add(StatCollector.translateToLocal("tooltip.nethertree"));
                list.add(StatCollector.translateToLocal("tooltip.fusewood.log"));
                break;
        }
    }
}
