package mods.natura.blocks.overrides;

import net.minecraft.block.BlockLever;

import mods.natura.client.LeverRender;

public class NetherLever extends BlockLever {

    public NetherLever() {
        super();
    }

    @Override
    public int getRenderType() {
        return LeverRender.model;
    }
}
