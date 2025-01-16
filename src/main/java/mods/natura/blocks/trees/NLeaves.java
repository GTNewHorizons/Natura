package mods.natura.blocks.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NContent;
import mods.natura.common.NaturaTab;
import net.minecraftforge.common.util.ForgeDirection;

public class NLeaves extends BlockLeaves {

    public NLeaves() {
        this.setBlockName("floraLeaves");
        this.setCreativeTab(NaturaTab.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1) {
            for (int l1 = -1; l1 <= 1; ++l1) {
                int i2 = p_149720_1_.getBiomeGenForCoords(p_149720_2_ + l1, p_149720_4_ + k1)
                        .getBiomeFoliageColor(p_149720_2_ + l1, p_149720_3_, p_149720_4_ + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(NContent.floraSapling);
    }

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6,
            int par7) {
        if (!par1World.isRemote) {
            ArrayList<ItemStack> items = getDrops(par1World, par2, par3, par4, par5, par7);

            for (ItemStack item : items) {
                if (par1World.rand.nextFloat() <= par6) {
                    this.dropBlockAsItem(par1World, par2, par3, par4, item);
                }
            }
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return Blocks.leaves.isOpaqueCube();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return field_150129_M[Blocks.leaves.isOpaqueCube() ? 1 : 0][(meta % 4) % field_150129_M[0].length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        final String[] textureNames = new String[] { "redwood", "eucalyptus", "hopseed" };
        field_150129_M[0] = new IIcon[textureNames.length];
        field_150129_M[1] = new IIcon[textureNames.length];
        for (int i = 0; i < textureNames.length; ++i) {
            field_150129_M[0][i] = iconRegister.registerIcon("natura:" + textureNames[i] + "_leaves_fancy");
            field_150129_M[1][i] = iconRegister.registerIcon("natura:" + textureNames[i] + "_leaves_fast");
        }
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates. Args: blockAccess, x, y, z, side
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side) {
        return Blocks.leaves.shouldSideBeRendered(worldIn, x, y, z, side);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    public int getDamageValue(World par1World, int par2, int par3, int par4) {
        return this.damageDropped(par1World.getBlockMetadata(par2, par3, par4)) % 3;
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) % 4 == 0) {
            return 255;
        }
        return super.getLightOpacity(world, x, y, z);
    }

    @Override
    public String[] func_150125_e() {
        return null;
    }
}
