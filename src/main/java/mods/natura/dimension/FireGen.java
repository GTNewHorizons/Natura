package mods.natura.dimension;

import java.util.Random;
import mods.natura.common.NContent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FireGen extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        for (int l = 0; l < 64; ++l) {
            int xPos = x + random.nextInt(8) - random.nextInt(8);
            int yPos = y + random.nextInt(4) - random.nextInt(4);
            int zPos = z + random.nextInt(8) - random.nextInt(8);

            if (world.isAirBlock(xPos, yPos, zPos)) {
                Block blockID = world.getBlock(xPos, yPos - 1, zPos);
                if (blockID == Blocks.netherrack || blockID == NContent.taintedSoil)
                    world.setBlock(xPos, yPos, zPos, Blocks.fire, 0, 2);
            }
        }

        return true;
    }
}
