package mods.natura.blocks.crops;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import mods.natura.common.NContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LargeGlowshroom extends Block {
    private final String mushroomType;

    @SideOnly(Side.CLIENT)
    private IIcon iconSkin;

    @SideOnly(Side.CLIENT)
    private IIcon iconStem;

    @SideOnly(Side.CLIENT)
    private IIcon iconInside;

    public LargeGlowshroom(Material material, String type) {
        super(material);
        mushroomType = type;
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(0.2F);
    }

    /* From the specified side and block metadata retrieves the blocks texture. Args: side, metadata */
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return meta == 10 && side > 1
                ? this.iconStem
                : (meta >= 1 && meta <= 9 && side == 1
                        ? this.iconSkin
                        : (meta >= 1 && meta <= 3 && side == 2
                                ? this.iconSkin
                                : (meta >= 7 && meta <= 9 && side == 3
                                        ? this.iconSkin
                                        : ((meta == 1 || meta == 4 || meta == 7) && side == 4
                                                ? this.iconSkin
                                                : ((meta == 3 || meta == 6 || meta == 9) && side == 5
                                                        ? this.iconSkin
                                                        : (meta == 14
                                                                ? this.iconSkin
                                                                : (meta == 15 ? this.iconStem : this.iconInside)))))));
    }

    /* Returns the quantity of items to drop on block destruction. */
    @Override
    public int quantityDropped(Random par1Random) {
        int i = par1Random.nextInt(10) - 7;

        if (i < 0) {
            i = 0;
        }

        return i;
    }

    @Override
    public int damageDropped(int meta) {
        if (this == NContent.glowshroomBlue) return 2;
        if (this == NContent.glowshroomPurple) return 1;
        if (this == NContent.glowshroomGreen) return 0;

        return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x, int y, int z, int side) {
        return iblockaccess.getBlock(x, y, z) == this ? false : super.shouldSideBeRendered(iblockaccess, x, y, z, side);
    }

    /* Returns the ID of the items to drop on destruction. */
    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return Item.getItemFromBlock(NContent.glowshroom);
    }

    /* only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative) */
    /*@SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
    	return Item.getItemFromBlock(NContent.glowshroom);
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconregister) {
        this.iconSkin = iconregister.registerIcon("natura:mushroom_skin_" + mushroomType);
        this.iconInside = iconregister.registerIcon("natura:mushroom_inside_" + mushroomType);
        this.iconStem = iconregister.registerIcon("natura:mushroom_stem_" + mushroomType);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        if (world.getBlock(x, y + 1, z) instanceof LargeGlowshroom) {
            return null;
        } else {
            return AxisAlignedBB.getBoundingBox(x, y + 0.9375, z, x + 1.0D, (double) y + 1, z + 1.0D);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity.motionY < 0) entity.motionY *= 0.25F;
        entity.fallDistance -= 0.25f;

        if (entity.motionY == 0f) {
            entity.motionX *= 0.25f;
            entity.motionZ *= 0.25f;
        }

        if (entity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) entity;
            living.addPotionEffect(new PotionEffect(Potion.confusion.id, 100, 0));
        }
    }
}
