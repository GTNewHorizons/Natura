package mods.natura.plugins.minefactoryreloaded.plantables;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import mods.natura.common.NContent;

public class PlantableNaturaBerry extends PlantableStandard {

    public PlantableNaturaBerry(Item sourceId, Block plantedBlockId) {
        super(sourceId, plantedBlockId);
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        Block groundBlock = world.getBlock(x, y - 1, z);

        return (groundBlock != null
                && groundBlock.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (IPlantable) NContent.berryBush)
                && world.isAirBlock(x, y, z));
    }

    @Override
    public int getMeta(ItemStack stack) {
        return stack.getItemDamage() % 4;
    }
}
