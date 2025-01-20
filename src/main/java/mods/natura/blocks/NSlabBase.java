package mods.natura.blocks;

import mods.natura.blocks.trees.Planks;
import mods.natura.items.blocks.PlanksItem;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NContent;
import mods.natura.common.NaturaTab;

public abstract class NSlabBase extends BlockWoodSlab {
    // metaGroup 1 is the first 8 types, metaGroup 2 is the next 8, etc.
    // slabs are max of 8 per group due to vanilla use of metadata, so this variable maps
    // wooden slabs to the overall wood metadata used elsewhere, such as for textures and flammability
    private final int metaGroup;

    public NSlabBase(boolean isDoubleSlab, int grp) {
        super(isDoubleSlab);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.useNeighborBrightness = true;
        if (!isDoubleSlab) {
            this.setCreativeTab(NaturaTab.tab);
        }
        metaGroup = grp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return NContent.planks.getIcon(side, getWoodMeta(meta));
    }

    @Override
    public String func_150002_b(int meta) {
        // unlocalized name
        meta = getWoodMeta(meta);
        return "block.wood." + PlanksItem.blockType[meta] + ".slab";
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        int meta = getWoodMeta(world.getBlockMetadata(x, y, z));
        return Planks.getPlankFlammability(this, meta);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        int meta = getWoodMeta(world.getBlockMetadata(x, y, z));
        return Planks.getPlankFireSpreadSpeed(this, meta);
    }

    private int getWoodMeta(int meta) {
        meta = (meta & 7) + (metaGroup - 1) * 8;
        if (meta < 0 || meta >= PlanksItem.blockType.length) meta = 0;
        return meta;
    }
}
