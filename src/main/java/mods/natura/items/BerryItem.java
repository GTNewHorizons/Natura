package mods.natura.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.natura.common.NaturaTab;

public class BerryItem extends ItemFood {

    public IIcon[] icons;
    public String[] textureNames = new String[] { "rasp", "blue", "black", "geo" };

    public BerryItem(int healAmount) {
        super(healAmount, 0.4F, false);
        setHasSubtypes(true);
        setMaxDamage(0);
        this.setCreativeTab(NaturaTab.tab);
        this.setAlwaysEdible();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.canEat(true) && player.getFoodStats().getSaturationLevel() < 18F) {
            player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }
        return itemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 16;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= icons.length ? icons[0] : icons[meta];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[textureNames.length];

        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = iconRegister.registerIcon("natura:berry_" + textureNames[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add(StatCollector.translateToLocal("tooltip.berry"));
    }

    /* Name override */
    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return "item.berry."
                + textureNames[itemstack.getItemDamage() >= textureNames.length ? 0 : itemstack.getItemDamage()];
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < 4; ++i) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }
}
