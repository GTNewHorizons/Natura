package mods.natura.blocks.trees;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NContent;

public class NLeavesDark extends NLeaves {

    public NLeavesDark() {
        super();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        final String[] textureNames = new String[] { "darkwood", "darkwood_flowering", "darkwood_fruit", "fusewood" };
        field_150129_M[0] = new IIcon[textureNames.length];
        field_150129_M[1] = new IIcon[textureNames.length];
        for (int i = 0; i < textureNames.length; ++i) {
            field_150129_M[0][i] = iconRegister.registerIcon("natura:" + textureNames[i] + "_leaves_fancy");
            field_150129_M[1][i] = iconRegister.registerIcon("natura:" + textureNames[i] + "_leaves_fast");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return 16777215;
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int par1) {
        return 16777215;
    }

    /**
     * Returns an integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        return 16777215;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        if (metadata % 4 == 2) return NContent.potashApple;
        return Item.getItemFromBlock(NContent.floraSapling);
    }

    @Override
    public int damageDropped(int par1) {
        if (par1 % 4 == 2) return 0;
        if (par1 % 4 == 3) return 7;
        return 6;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        if (meta % 4 == 2) return 1;
        return quantityDroppedWithBonus(fortune, random);
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return this.getLightOpacity();
    }

}
