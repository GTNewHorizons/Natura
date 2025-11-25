package mods.natura.items.blocks;

import net.minecraft.block.Block;
import java.util.List;

import mantle.blocks.abstracts.MultiItemBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GrassBlockItem extends MultiItemBlock {

    public static final String blockType[] = { "grass", "bluegrass", "autumngrass" };

    public GrassBlockItem(Block id) {
        super(id, "block.soil", blockType);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
        lines.add(StatCollector.translateToLocal("item.block.nomobspawnsonthisblock.desc"));
    }
    /*
     * @Override public String getUnlocalizedName (ItemStack itemstack) { int pos =
     * MathHelper.clamp_int(itemstack.getItemDamage(), 0, blockType.length - 1); return (new
     * StringBuilder()).append("block.soil.").append(blockType[pos]).toString(); }
     */
}
