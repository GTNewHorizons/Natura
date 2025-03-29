package mods.natura.items.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mantle.blocks.abstracts.MultiItemBlock;

public class OverworldLeavesItem extends MultiItemBlock {

    public static final String blockType[] = { "maple", "silverbell", "purpleheart", "tiger" };

    public OverworldLeavesItem(Block i) {
        super(i, "block.leaves", blockType);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta | 4;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        switch (stack.getItemDamage() % 4) {
            case 0:
                list.add(StatCollector.translateToLocal("tooltip.tree7"));
                break;
            case 1:
                list.add(StatCollector.translateToLocal("tooltip.tree8"));
                break;
            case 2:
                list.add(StatCollector.translateToLocal("tooltip.tree9"));
                break;
            case 3:
                list.add(StatCollector.translateToLocal("tooltip.tree10"));
                break;
        }
    }
}
