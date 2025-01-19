package mods.natura.blocks;

import net.minecraft.block.BlockWoodSlab;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NContent;
import mods.natura.common.NaturaTab;

public abstract class NSlabBase extends BlockWoodSlab {

    public static String[] woodNames = new String[] { "eucalyptus", "sakura", "ghost", "redwood", "blood", "bush",
            "maple", "silverbell", "purpleheart", "tiger", "willow", "darkwood", "fusewood" };
    // group 1 is the first 8 types, group 2 is the next 8, etc.
    // slabs are max of 8 per group due to vanilla use of metadata, so this variable
    // maps slabs to the overall wood metadata used elsewhere, such as for textures and flammability
    private final int group;

    public NSlabBase(boolean isDoubleSlab, int grp) {
        super(isDoubleSlab);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.useNeighborBrightness = true;
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
    public String func_150002_b(int meta) {
        // unlocalized name
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

    private int getWoodMeta(int meta) {
        meta = (meta & 7) + (group - 1) * 8;
        if (meta < 0 || meta >= woodNames.length) meta = 0;
        return meta;
    }
}
