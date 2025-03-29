package mods.natura.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

import mods.natura.blocks.GrassSlab;

public class GrassSlabItem extends ItemSlab {

    public GrassSlabItem(Block block, GrassSlab singleSlab, GrassSlab doubleSlab) {
        super(block, singleSlab, doubleSlab, block == doubleSlab);
    }
}
