package mods.natura.blocks.overrides;

import java.util.List;

import mods.natura.blocks.trees.Planks;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NContent;

public class AlternateBookshelf extends BlockBookshelf {

    IIcon[] icons;

    public AlternateBookshelf() {
        super();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (side == 0 || side == 1) return NContent.planks.getIcon(side, metadata);
        return icons[metadata];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        this.icons = new IIcon[NContent.woodTextureNames.length];

        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = iconRegister.registerIcon("natura:" + NContent.woodTextureNames[i] + "_bookshelf");
        }
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        int meta = world.getBlockMetadata(x, y, z);
        return Planks.getPlankFlammability(this, meta);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        int meta = world.getBlockMetadata(x, y, z);
        return Planks.getPlankFireSpreadSpeed(this, meta);
    }

    @Override
    public float getEnchantPowerBonus(World world, int x, int y, int z) {
        return 1f;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < icons.length; i++) list.add(new ItemStack(item, 1, i));
    }
}
