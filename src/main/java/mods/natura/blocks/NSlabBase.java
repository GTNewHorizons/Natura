package mods.natura.blocks;

import java.util.List;
import java.util.Random;

import mods.natura.common.NContent;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NaturaTab;
import net.minecraftforge.common.util.ForgeDirection;

public class NSlabBase extends BlockWoodSlab {

    public static String[] woodNames = new String[] { "eucalyptus", "sakura", "ghost", "redwood", "blood",
            "bush", "maple", "silverbell", "purpleheart", "tiger", "willow", "darkwood", "fusewood" };
    private final int group;

    public NSlabBase(boolean isDoubleSlab, int grp) {
        super(isDoubleSlab);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        if (!isDoubleSlab) {
            this.setCreativeTab(NaturaTab.tab);
        }
        group = grp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return NContent.planks.getIcon(side, getWoodMeta(meta));
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        // if double slab
        if (field_150004_a) {
            if (this == NContent.plankSlab1Double) return Item.getItemFromBlock(NContent.plankSlab1);
            else return Item.getItemFromBlock(NContent.plankSlab2);
        }
        else return Item.getItemFromBlock(this);
    }

    @Override
    public String func_150002_b(int meta)
    {
        return "block.wood." + woodNames[getWoodMeta(meta)] + ".slab";
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        int metadata = getWoodMeta(world.getBlockMetadata(x, y, z));
        if (metadata == 2 || metadata == 4 || metadata > 10) return 0;
        return Blocks.fire.getFlammability(this);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        int metadata = getWoodMeta(world.getBlockMetadata(x, y, z));
        if (metadata == 2 || metadata == 4 || metadata > 10) return 0;
        return Blocks.fire.getEncouragement(this);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        int metadata = getWoodMeta(world.getBlockMetadata(x, y, z));
        if (metadata == 2 || metadata == 4 || metadata > 10) return false;
        return getFlammability(world, x, y, z, face) > 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item id, CreativeTabs tab, List list) {
        if (group == 1) {
            if (id != Item.getItemFromBlock(NContent.plankSlab1Double)) {
                for (int iter = 0; iter < 8; iter++) {
                    list.add(new ItemStack(id, 1, iter));
                }
            }
        }
        else {
            if (id != Item.getItemFromBlock(NContent.plankSlab2Double)) {
                for (int iter = 0; iter < 5; iter++) {
                    list.add(new ItemStack(id, 1, iter));
                }
            }
        }
    }

    @Override
    protected ItemStack createStackedBlock(int meta)
    {
        if (group == 1) {
            return new ItemStack(Item.getItemFromBlock(NContent.plankSlab1), 2, meta & 7);
        }
        else {
            return new ItemStack(Item.getItemFromBlock(NContent.plankSlab2), 2, meta & 7);
        }
    }

    private int getWoodMeta(int meta) {
        meta = (meta & 7) + (group-1) * 8;
        if (meta < 0 || meta >= woodNames.length) meta = 0;
        return meta;
    }
}
