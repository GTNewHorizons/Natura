package mods.natura.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import mods.natura.common.NCraftingItem;
import mods.natura.common.NaturaTab;
import mods.natura.common.PHNatura;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class PlantItem extends NCraftingItem {
    public PlantItem() {
        super(
                new String[] {
                    "barley.plant",
                    "barley.flour",
                    "wheat.flour",
                    "cotton.plant",
                    "powder.sulfur",
                    "fletching.ghostwood",
                    "leather.imp",
                    "string.flame",
                    "dye.blue"
                },
                new String[] {
                    "barley_plant",
                    "barley_flour",
                    "wheat_flour",
                    "cotton_plant",
                    "sulfur",
                    "ghostwood_fletching",
                    "leather_imp",
                    "flamestring",
                    "dye_blue"
                });
        this.setCreativeTab(NaturaTab.tab);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return (new StringBuilder())
                .append("item.")
                .append(unlocalizedNames[itemstack.getItemDamage()])
                .toString();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        switch (stack.getItemDamage()) {
            case 0:
                list.add(StatCollector.translateToLocal("tooltip.barley"));
                break;
            case 1:
            case 2:
                list.add(StatCollector.translateToLocal("tooltip.flour1"));
                list.add(StatCollector.translateToLocal("tooltip.flour2"));
                break;
            case 3:
                list.add(StatCollector.translateToLocal("tooltip.cotton"));
                break;
            case 4:
                list.add(StatCollector.translateToLocal("tooltip.sulfur"));
                break;
            case 5:
                list.add(StatCollector.translateToLocal("tooltip.fletching"));
                break;
            case 6:
                list.add(StatCollector.translateToLocal("tooltip.imp"));
                break;
            case 7:
                list.add(StatCollector.translateToLocal("tooltip.string"));
                break;
        }
    }

    @Override
    public void getSubItems(Item id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, 0));
        list.add(new ItemStack(id, 1, 1));
        list.add(new ItemStack(id, 1, 2));
        list.add(new ItemStack(id, 1, 3));
        list.add(new ItemStack(id, 1, 4));
        list.add(new ItemStack(id, 1, 5));
        if (PHNatura.enableImps) list.add(new ItemStack(id, 1, 6));
        if (PHNatura.enableHeatscarSpiders) list.add(new ItemStack(id, 1, 7));
        list.add(new ItemStack(id, 1, 8));
    }
}
