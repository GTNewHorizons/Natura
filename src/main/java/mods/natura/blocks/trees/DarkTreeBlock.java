package mods.natura.blocks.trees;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NaturaTab;
import mods.natura.util.Util;

public class DarkTreeBlock extends Block {

    public IIcon[] icons;
    public String[] textureNames = new String[] { "darkwood_bark", "darkwood_heart", "fusewood_bark",
            "fusewood_heart" };

    public DarkTreeBlock() {
        super(Material.wood);
        this.setHardness(3.5F);
        this.setResistance(40F);
        this.setStepSound(Block.soundTypeWood);
        this.setCreativeTab(NaturaTab.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        int tex = (metadata % 4) * 2;
        int orientation = metadata / 4;

        switch (orientation)
        // Ends of logs
        {
            case 0:
                if (side == 0 || side == 1) {
                    return Util.getWithFallback(icons, tex + 1);
                }
                break;
            case 1:
                if (side == 4 || side == 5) {
                    return Util.getWithFallback(icons, tex + 1);
                }
                break;
            case 2:
                if (side == 2 || side == 3) {
                    return Util.getWithFallback(icons, tex + 1);
                }
                break;
        }

        return Util.getWithFallback(icons, tex);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[textureNames.length];

        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = iconRegister.registerIcon("natura:" + textureNames[i]);
        }
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        byte b0 = 4;
        int j1 = b0 + 1;

        if (worldIn.checkChunksExist(x - j1, y - j1, z - j1, x + j1, y + j1, z + j1)) {
            for (int k1 = -b0; k1 <= b0; ++k1) {
                for (int l1 = -b0; l1 <= b0; ++l1) {
                    for (int i2 = -b0; i2 <= b0; ++i2) {
                        Block j2 = worldIn.getBlock(x + k1, y + l1, z + i2);

                        if (j2 != null) {
                            j2.beginLeavesDecay(worldIn, x + k1, y + l1, z + i2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7,
            float par8, int par9) {
        int j1 = par9 & 3;
        byte b0 = 0;

        switch (par5) {
            case 0:
            case 1:
                b0 = 0;
                break;
            case 2:
            case 3:
                b0 = 8;
                break;
            case 4:
            case 5:
                b0 = 4;
        }

        return j1 | b0;
    }

    @Override
    public int damageDropped(int par1) {
        return par1 & 3;
    }

    /**
     * returns a number between 0 and 3
     */
    public static int limitToValidMetadata(int par0) {
        return par0 & 3;
    }

    @Override
    protected ItemStack createStackedBlock(int par1) {
        return new ItemStack(this, 1, limitToValidMetadata(par1));
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        if (meta % 4 == 1) {
            if (world.difficultySetting.getDifficultyId() > 2) world.createExplosion(null, x, y, z, 1.75f, false);
            else world.createExplosion(null, x, y, z, 2f, false);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < icons.length / 2; i++) par3List.add(new ItemStack(par1, 1, i));
    }

    @Override
    public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isWood(IBlockAccess world, int x, int y, int z) {
        return true;
    }
}
