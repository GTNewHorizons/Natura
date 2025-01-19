package mods.natura.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NContent;

public class NSlab1 extends NSlabBase {

    public NSlab1(boolean isDoubleSlab) {
        super(isDoubleSlab, 1);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        // handles so both double and single drop the single slab version
        return Item.getItemFromBlock(NContent.plankSlab1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item id, CreativeTabs tab, List list) {
        for (int iter = 0; iter < 8; iter++) {
            list.add(new ItemStack(id, 1, iter));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z) {
        return Item.getItemFromBlock(NContent.plankSlab1);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(Item.getItemFromBlock(NContent.plankSlab1), 2, meta & 7);
    }
}
