package mods.natura.items.blocks;

import mods.natura.blocks.NSlabBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class NSlabBaseItem extends ItemSlab {

    public NSlabBaseItem(Block block, NSlabBase singleSlab, NSlabBase doubleSlab) {
        super(block, singleSlab, doubleSlab, block == doubleSlab);
    }
}
