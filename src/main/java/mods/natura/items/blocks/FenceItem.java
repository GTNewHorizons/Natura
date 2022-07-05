package mods.natura.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mantle.blocks.abstracts.MultiItemBlock;
import mods.natura.common.NContent;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class FenceItem extends MultiItemBlock {
    public FenceItem(Block block) {
        super(block, block.getUnlocalizedName(), NContent.woodTextureNames);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return NContent.planks.getIcon(0, meta);
    }
}
