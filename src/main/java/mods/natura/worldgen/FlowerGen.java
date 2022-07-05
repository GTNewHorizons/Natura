package mods.natura.worldgen;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FlowerGen extends WorldGenerator {
    /** The ID of the plant block used in this plant generator. */
    public final Block flower;

    public final int metadata;
    public int chances = 64;

    public FlowerGen(Block flowerBlock, int flowerMeta) {
        this.flower = flowerBlock;
        this.metadata = flowerMeta;
    }

    public FlowerGen(Block flowerBlock, int flowerMeta, int count) {
        this.flower = flowerBlock;
        this.metadata = flowerMeta;
        this.chances = count;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        for (int i = 0; i < chances; ++i) {
            int posX = x + random.nextInt(8) - random.nextInt(8);
            int posY = y + random.nextInt(4) - random.nextInt(4);
            int posZ = z + random.nextInt(8) - random.nextInt(8);

            if (posY < 127
                    && world.isAirBlock(posX, posY, posZ)
                    && !world.provider.hasNoSky
                    && flower.canBlockStay(world, posX, posY, posZ)) {
                world.setBlock(posX, posY, posZ, flower, metadata, 2);
            }
        }
        return true;
    }
}
