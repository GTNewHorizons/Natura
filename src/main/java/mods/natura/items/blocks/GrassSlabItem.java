package mods.natura.items.blocks;

import mods.natura.blocks.GrassSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class GrassSlabItem extends ItemSlab {

    public GrassSlabItem(Block block, GrassSlab singleSlab, GrassSlab doubleSlab) {
        super(block, singleSlab, doubleSlab, block == doubleSlab);
    }
}
