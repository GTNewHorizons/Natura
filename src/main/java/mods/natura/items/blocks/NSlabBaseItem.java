package mods.natura.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

import mods.natura.blocks.NSlabBase;

public class NSlabBaseItem extends ItemSlab {

    public NSlabBaseItem(Block block, NSlabBase singleSlab, NSlabBase doubleSlab) {
        super(block, singleSlab, doubleSlab, block == doubleSlab);
    }
}
