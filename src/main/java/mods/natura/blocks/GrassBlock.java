package mods.natura.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.client.GrassColorizerAlternate;
import mods.natura.common.NaturaTab;

public class GrassBlock extends Block {

    public GrassBlock() {
        super(Material.grass);
        setHardness(0.6F);
        this.setCreativeTab(NaturaTab.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("natura:grass_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item id, CreativeTabs tab, List list) {
        for (int iter = 0; iter < 3; iter++) {
            list.add(new ItemStack(id, 1, iter));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerGrass.getGrassColor(d0, d1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        /*
         * if (par1 % 8 == 0) return this.getBlockColor(); return 0xFFFFFF;
         */
        double d0 = 0.5D;
        double d1 = 1.0D;
        switch (meta) {
            case 1:
                return GrassColorizerAlternate.getBlueGrassColor(d0, 0.5D);
            case 2:
                return GrassColorizerAlternate.getOrangeGrassColor(1.0D, 1.0D);
            default:
                return ColorizerGrass.getGrassColor(d0, d1);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int meta = world.getBlockMetadata(x, y, z);

        for (int k1 = -1; k1 <= 1; ++k1) {
            for (int l1 = -1; l1 <= 1; ++l1) {
                BiomeGenBase biome = world.getBiomeGenForCoords(x + l1, z + k1);
                int grassColor = 0;
                double temp = 0d;
                double rainfall = 0d;
                switch (meta) {
                    case 1:
                        temp = MathHelper.clamp_float(biome.getFloatTemperature(x, y, z), 0.0F, 1.0F);
                        rainfall = MathHelper.clamp_float(biome.getFloatRainfall(), 0.0F, 1.0F);
                        grassColor = GrassColorizerAlternate.getBlueGrassColor(temp, rainfall);
                        break;
                    case 2:
                        temp = MathHelper.clamp_float(biome.getFloatTemperature(x, y, z), 0.0F, 1.0F);
                        rainfall = MathHelper.clamp_float(biome.getFloatRainfall(), 0.0F, 1.0F);
                        grassColor = GrassColorizerAlternate.getOrangeGrassColor(temp, rainfall);
                        break;
                    default:
                        grassColor = biome.getBiomeGrassColor(x, y, z);
                }
                l += (grassColor & 16711680) >> 16;
                i1 += (grassColor & 65280) >> 8;
                j1 += grassColor & 255;
            }
        }
        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction,
            IPlantable plant) {
        EnumPlantType plantType = plant.getPlantType(world, x, y + 1, z);
        return plantType == EnumPlantType.Plains && plant.getPlant(world, x, y + 1, z) != Blocks.tallgrass;
    }
}
