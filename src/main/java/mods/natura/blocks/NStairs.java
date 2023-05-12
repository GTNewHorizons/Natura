package mods.natura.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

import mods.natura.common.NaturaTab;

public class NStairs extends BlockStairs {

    public NStairs(Block par2Block, int par3) {
        super(par2Block, par3);
        this.setCreativeTab(NaturaTab.tab);
        this.setLightOpacity(0);
    }
}
