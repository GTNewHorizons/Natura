package mods.natura.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import mods.natura.common.NContent;
import mods.natura.common.PHNatura;

public class RedwoodTreeGen extends WorldGenerator {

    /*
     * public boolean drawCircle (World par1World, int radius, int x, int y, int z) { int tempX = x; int tempZ = z; int
     * tempRadius = radius; int aTrig = 0; int bTrig = 0; for (int loopCount = 0; loopCount <= radius; loopCount++) { //
     * draw quadrants aTrig = (radius - loopCount); bTrig = (int) (Math.sqrt(radius * radius - aTrig * aTrig)); //if (
     * par1World.getBlockId(x + aTrig, y, z + bTrig) != 56) // top right //{ par1World.setBlock(x + aTrig, y, z + bTrig,
     * 56); for (int i = z + bTrig; i >= z; i--) { par1World.setBlock(x + aTrig, y, i, 56); } //} //if (
     * par1World.getBlockId(x - aTrig, y, z - bTrig) != 56) // bottom left //{ par1World.setBlock(x - aTrig, y, z -
     * bTrig, 56); for (int i = z - bTrig; i <= z; i++) { par1World.setBlock(x - aTrig, y, i, 56); } //} //if (
     * par1World.getBlockId(x - aTrig, y, z + bTrig) != 56) // top left //{ par1World.setBlock(x - aTrig, y, z + bTrig,
     * 56); for (int i = z + bTrig; i >= z; i--) { par1World.setBlock(x - aTrig, y, i, 56); } //} //if (
     * par1World.getBlockId(x + aTrig, y, z - bTrig) != 56) // bottom right //{ par1World.setBlock(x + aTrig, y, z -
     * bTrig, 56); for (int i = z - bTrig; i <= z; i++) { par1World.setBlock(x + aTrig, y, i, 56); } //} } return true;
     * }
     */

    // With an axis index i, find the two other axis incies by looking at indices [i] and [i+3] in this array
    static final byte[] otherCoordPairs = { 2, 0, 0, 1, 2, 1 };
    Random rand;
    World worldObj;
    // Cursed code ahead! Most of this code is badly copy-pasted from Minecrafts WorldGenBigTree class,
    // which is used to generate large Oak trees. The RedwoodTreeGen effectively spawns a lot of fake large oak trees
    // without a trunk to generate leave clusters and branches.
    // -- Below variables hold the state of the currently processed fake large Oak tree --
    int[] fakeOakBasePos = { 0, 0, 0 };
    int fakeOakHeightLimit;
    int fakeOakHeight;
    double fakeOakHeightAttenuation = 0.61799999999999999D;
    double fakeOakBranchSlope = 0.38100000000000001D;
    double fakeOakScaleWidth = 1.0D;
    double fakeOakLeafDensity = 1.0D;
    int fakeOakHeightLimitLimit = 12;
    int fakeOakLeafDistanceLimit = 4;
    int[][] fakeOakLeafNodes;
    // -- --
    Block genWoodID;
    int genWoodMetadata = 0;
    boolean useHeight;

    public RedwoodTreeGen(boolean notify, Block woodBlock) {
        super(notify);
        useHeight = notify;
        rand = new Random();
        genWoodID = woodBlock;
    }

    int findGround(World world, int x, int y, int z) {
        boolean foundGround = false;
        int height = PHNatura.seaLevel + 64;
        do {
            height--;
            Block underID = world.getBlock(x, height, z);
            if (underID == Blocks.dirt || underID == Blocks.grass || height < PHNatura.seaLevel) foundGround = true;
        } while (!foundGround);
        return height;
    }

    public boolean isValidSpawn(World world, int x, int y, int z) {
        Block bID = world.getBlock(x, y, z);
        boolean ground = bID == Blocks.dirt || bID == Blocks.grass;
        boolean transparent = !world.getBlock(x, y + 1, z).func_149730_j();
        boolean valid = ground && transparent;
        return ground && transparent;
    }

    public boolean generate(World world, Random random, int x, int yPos, int z) {
        int groundPoint = yPos;
        if (!useHeight) {
            groundPoint = findGround(world, x, yPos, z);
            if (!isValidSpawn(world, x, groundPoint, z)) return false;
        }

        int treeHeight = random.nextInt(60) + 80;
        worldObj = world;
        long ran = random.nextLong();
        rand.setSeed(ran);
        fakeOakBasePos[0] = x;
        fakeOakBasePos[1] = groundPoint;
        fakeOakBasePos[2] = z;

        fakeOakHeightLimit = 5 + rand.nextInt(fakeOakHeightLimitLimit);

        if (treeHeight > 120) {
            for (int currentHeight = 0; currentHeight < treeHeight; currentHeight++) {
                if (currentHeight < treeHeight / 10) {
                    genRing13(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 2 / 10) {
                    genRing12(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 3 / 10) {
                    genRing11(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 4 / 10) {
                    genRing10(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 5 / 10) {
                    genRing9(world, random, x, currentHeight + groundPoint, z);
                    growLowBranch(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 6 / 10) {
                    genRing8(world, random, x, currentHeight + groundPoint, z);
                    growLowBranch(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 7 / 10) {
                    genRing7(world, random, x, currentHeight + groundPoint, z);
                    growMiddleBranch(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 8 / 10) {
                    genRing6(world, random, x, currentHeight + groundPoint, z);
                    growMiddleBranch(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 9 / 10) {
                    genRing5(world, random, x, currentHeight + groundPoint, z);
                    growHighBranch(world, random, x, currentHeight + groundPoint, z);
                } else {
                    genRing3(world, random, x, currentHeight + groundPoint, z);
                    growHighBranch(world, random, x, currentHeight + groundPoint, z);
                }
            }

            growBigRoots(world, random, x, groundPoint - 1, z);
            growTop(world, random, x, treeHeight + groundPoint, z);
        } else if (treeHeight > 100) {
            for (int currentHeight = 0; currentHeight < treeHeight; currentHeight++) {
                if (currentHeight < treeHeight / 8) {
                    genRing11(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 2 / 8) {
                    genRing10(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 3 / 8) {
                    genRing9(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 4 / 8) {
                    genRing8(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 5 / 8) {
                    genRing7(world, random, x, currentHeight + groundPoint, z);
                    growMiddleBranch(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 6 / 8) {
                    genRing6(world, random, x, currentHeight + groundPoint, z);
                    growMiddleBranch(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 7 / 8) {
                    genRing5(world, random, x, currentHeight + groundPoint, z);
                    growHighBranch(world, random, x, currentHeight + groundPoint, z);
                } else {
                    genRing3(world, random, x, currentHeight + groundPoint, z);
                    growHighBranch(world, random, x, currentHeight + groundPoint, z);
                }
            }

            growMediumRoots(world, random, x, groundPoint - 1, z);
            growTop(world, random, x, treeHeight + groundPoint, z);
        } else {
            for (int currentHeight = 0; currentHeight < treeHeight; currentHeight++) {
                if (currentHeight < treeHeight / 6) {
                    genRing9(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 2 / 6) {
                    genRing8(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 3 / 6) {
                    genRing7(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 4 / 6) {
                    genRing6(world, random, x, currentHeight + groundPoint, z);
                    growMiddleBranch(world, random, x, currentHeight + groundPoint, z);
                } else if (currentHeight < treeHeight * 5 / 6) {
                    genRing5(world, random, x, currentHeight + groundPoint, z);
                    growHighBranch(world, random, x, currentHeight + groundPoint, z);
                } else {
                    genRing3(world, random, x, currentHeight + groundPoint, z);
                    growHighBranch(world, random, x, currentHeight + groundPoint, z);
                }
            }
            growSmallRoots(world, random, x, groundPoint - 1, z);
            growTop(world, random, x, treeHeight + groundPoint, z);
        }
        return true;
    }

    /*
     * else if (height > 70) { if (heightPos >= 70) { for (int j3 = currentHeight + groundPoint; j3 <= (groundPoint +
     * heightPos) - 70; j3++) { genRing11(world, random, x, currentHeight + groundPoint, z); heightPos--; currentHeight
     * + groundPoint++; } } if (heightPos >= 58) { for (int k3 = currentHeight + groundPoint; k3 <= (groundPoint +
     * heightPos) - 58; k3++) { genRing10(world, random, x, currentHeight + groundPoint, z); heightPos--; currentHeight
     * + groundPoint++; } } if (heightPos >= 35) { for (int l3 = currentHeight + groundPoint; l3 <= (groundPoint +
     * heightPos) - 35; l3++) { genRing9(world, random, x, currentHeight + groundPoint, z); heightPos--;
     * growMediumLowerBranch(world, random, x, currentHeight + groundPoint, z); currentHeight + groundPoint++; } } if
     * (heightPos >= 15) { for (int i4 = currentHeight + groundPoint; i4 <= (groundPoint + heightPos) - 15; i4++) {
     * genRing8(world, random, x, currentHeight + groundPoint, z); heightPos--; growMediumUpperBranch(world, random, x,
     * currentHeight + groundPoint, z); currentHeight + groundPoint++; } } if (heightPos >= 0) { for (int j4 =
     * currentHeight + groundPoint; j4 <= (groundPoint + heightPos) - 0; j4++) { genRing7(world, random, x,
     * currentHeight + groundPoint, z); heightPos--; growMediumUpperBranch(world, random, x, currentHeight +
     * groundPoint, z); currentHeight + groundPoint++; } } growMediumRoots(world, random, x, groundPoint - 1, z);
     * growMediumTop(world, random, x, currentHeight + groundPoint, z); } else { if (heightPos >= 50) { for (int k4 =
     * currentHeight + groundPoint; k4 <= (groundPoint + heightPos) - 50; k4++) { genRing9(world, random, x,
     * currentHeight + groundPoint, z); heightPos--; currentHeight + groundPoint++; } } if (heightPos >= 25) { for (int
     * l4 = currentHeight + groundPoint; l4 <= (groundPoint + heightPos) - 25; l4++) { genRing8(world, random, x,
     * currentHeight + groundPoint, z); heightPos--; growSmallLowerBranch(world, random, x, currentHeight + groundPoint,
     * z); currentHeight + groundPoint++; } } if (heightPos >= 0) { for (int i5 = currentHeight + groundPoint; i5 <=
     * (groundPoint + heightPos) - 0; i5++) { genRing7(world, random, x, currentHeight + groundPoint, z); heightPos--;
     * growSmallUpperBranch(world, random, x, currentHeight + groundPoint, z); currentHeight + groundPoint++; } }
     * growSmallRoots(world, random, x, groundPoint - 1, z); growSmallTop(world, random, x, currentHeight + groundPoint,
     * z); }
     */

    public boolean growTop(World world, Random random, int x, int y, int z) {
        fakeOakBasePos[0] = x;
        fakeOakBasePos[1] = y + 4;
        fakeOakBasePos[2] = z;
        generateLeafNodeList();
        generateLeaves();
        generateLeafNodeBases();
        fakeOakBasePos[0] = x;
        fakeOakBasePos[1] = y + 4;
        fakeOakBasePos[2] = z;
        generateLeafNodeList();
        generateLeaves();
        generateLeafNodeBases();
        fakeOakBasePos[0] = x;
        fakeOakBasePos[1] = y;
        fakeOakBasePos[2] = z;
        generateLeafNodeList();
        generateLeaves();
        generateLeafNodeBases();
        fakeOakBasePos[0] = x;
        fakeOakBasePos[1] = y;
        fakeOakBasePos[2] = z;
        generateLeafNodeList();
        generateLeaves();
        generateLeafNodeBases();
        return false;
    }

    public boolean growHighBranch(World world, Random random, int x, int y, int z) {
        for (int iter = 0; iter < 3; iter++) {
            fakeOakBasePos[0] = (x + random.nextInt(21)) - 10;
            fakeOakBasePos[1] = y;
            fakeOakBasePos[2] = (z + random.nextInt(21)) - 10;
            generateLeafNodeList();
            generateLeaves();
            generateLeafNodeBases();
        }
        return false;
    }

    public boolean growMiddleBranch(World world, Random random, int x, int y, int z) {
        for (int iter = 0; iter < 6; iter++) {
            fakeOakBasePos[0] = (x + random.nextInt(31)) - 15;
            fakeOakBasePos[1] = y;
            fakeOakBasePos[2] = (z + random.nextInt(31)) - 15;
            generateLeafNodeList();
            generateLeaves();
            generateLeafNodeBases();
        }

        return false;
    }

    public boolean growLowBranch(World world, Random random, int x, int y, int z) {
        fakeOakBasePos[0] = (x + random.nextInt(17)) - 8;
        fakeOakBasePos[1] = y;
        fakeOakBasePos[2] = (z + random.nextInt(17)) - 8;
        generateLeafNodeList();
        generateLeaves();
        generateLeafNodeBases();
        if (random.nextInt(2) == 0) {
            fakeOakBasePos[0] = (x + random.nextInt(17)) - 8;
            fakeOakBasePos[1] = y;
            fakeOakBasePos[2] = (z + random.nextInt(17)) - 8;
            generateLeafNodeList();
            generateLeaves();
            generateLeafNodeBases();
        }
        fakeOakBasePos[0] = (x + random.nextInt(17)) - 8;
        fakeOakBasePos[1] = y;
        fakeOakBasePos[2] = (z + random.nextInt(17)) - 8;
        generateLeafNodeList();
        generateLeaves();
        generateLeafNodeBases();
        return false;
    }

    public boolean growSmallRoots(World world, Random random, int x, int y, int z) {
        genRing9(world, random, x, y, z);
        smallRoot1(world, random, x, y - 1, z);
        smallRoot1(world, random, x, y - 2, z);
        smallRoot1(world, random, x, y - 3, z);
        smallRoot2(world, random, x, y - 4, z);
        smallRoot2(world, random, x, y - 5, z);
        smallRoot3(world, random, x, y - 6, z);
        smallRoot3(world, random, x, y - 7, z);
        smallRoot3(world, random, x, y - 8, z);
        smallRoot3(world, random, x, y - 9, z);
        smallRoot4(world, random, x, y - 10, z);
        smallRoot4(world, random, x, y - 11, z);
        return true;
    }

    public boolean growMediumRoots(World world, Random random, int x, int y, int z) {
        genRing11(world, random, x, y, z);
        mediumRoot1(world, random, x, y - 1, z);
        mediumRoot1(world, random, x, y - 2, z);
        mediumRoot1(world, random, x, y - 3, z);
        mediumRoot2(world, random, x, y - 4, z);
        mediumRoot2(world, random, x, y - 5, z);
        mediumRoot3(world, random, x, y - 6, z);
        mediumRoot3(world, random, x, y - 7, z);
        mediumRoot3(world, random, x, y - 8, z);
        mediumRoot3(world, random, x, y - 9, z);
        mediumRoot4(world, random, x, y - 10, z);
        mediumRoot4(world, random, x, y - 11, z);
        mediumRoot5(world, random, x, y - 12, z);
        mediumRoot5(world, random, x, y - 13, z);
        mediumRoot5(world, random, x, y - 14, z);
        return true;
    }

    public boolean growBigRoots(World world, Random random, int x, int j, int k) {
        genRing13(world, random, x, j, k);
        bigRoot1(world, random, x, j - 1, k);
        bigRoot1(world, random, x, j - 2, k);
        bigRoot1(world, random, x, j - 3, k);
        bigRoot2(world, random, x, j - 4, k);
        bigRoot2(world, random, x, j - 5, k);
        bigRoot3(world, random, x, j - 6, k);
        bigRoot3(world, random, x, j - 7, k);
        bigRoot3(world, random, x, j - 8, k);
        bigRoot3(world, random, x, j - 9, k);
        bigRoot4(world, random, x, j - 10, k);
        bigRoot4(world, random, x, j - 11, k);
        bigRoot5(world, random, x, j - 12, k);
        bigRoot5(world, random, x, j - 13, k);
        bigRoot5(world, random, x, j - 14, k);
        bigRoot6(world, random, x, j - 15, k);
        bigRoot6(world, random, x, j - 16, k);
        bigRoot6(world, random, x, j - 17, k);
        bigRoot6(world, random, x, j - 18, k);
        return true;
    }

    public boolean smallRoot1(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean smallRoot2(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean smallRoot3(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean smallRoot4(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean mediumRoot1(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 5, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 1, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean mediumRoot2(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean mediumRoot3(World world, Random random, int x, int j, int k) {
        if (world.getBlock(x, j, k) != Blocks.bedrock && j > 0) {
            setBlockAndNotifyAdequately(world, x - 3, j, k - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, j, k + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, j, k - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, j, k - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, j, k + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, j, k + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, j, k - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, j, k - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, j, k + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, j, k + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, j, k - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, j, k - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, j, k + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, j, k + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, j, k - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, j, k - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, j, k + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, j, k + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, j, k - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, j, k + 2, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean mediumRoot4(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean mediumRoot5(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean bigRoot1(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 6, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 6, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 6, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 6, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 6, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 6, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 6, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 6, y, z + 2, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean bigRoot2(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 5, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 6, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 1, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 4, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean bigRoot3(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean bigRoot4(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean bigRoot5(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean bigRoot6(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 2);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 2);
        }
        return true;
    }

    public boolean genRing13(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 6, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 6, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 6, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 6, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 6, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 5, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 5, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z + 2, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing12(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 6, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 6, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 6, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 5, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 5, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 6, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 5, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 6, y, z + 1, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing11(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 5, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 5, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 5, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 5, y, z + 1, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing10(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 4, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 2, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing9(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 4, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 4, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 4, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 4, y, z + 1, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing8(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 3, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 2, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing7(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 3, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 3, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 3, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 3, y, z + 1, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing6(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 2, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 2, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing5(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 2, y, z + 1, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing4(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 2, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 2, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 2, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing3s(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing3(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata + 1);
            setBlockAndNotifyAdequately(world, x, y, z + 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x + 1, y, z + 1, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing2(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x - 1, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x - 1, y, z, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z - 1, genWoodID, genWoodMetadata);
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata);
        }
        return true;
    }

    public boolean genRing1(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.bedrock && y > 0) {
            setBlockAndNotifyAdequately(world, x, y, z, genWoodID, genWoodMetadata);
        }
        return true;
    }

    void generateLeafNodeList() {
        fakeOakHeight = (int) ((double) fakeOakHeightLimit * fakeOakHeightAttenuation);
        if (fakeOakHeight >= fakeOakHeightLimit) {
            fakeOakHeight = fakeOakHeightLimit - 1;
        }
        int i = (int) (1.3819999999999999D + Math.pow((fakeOakLeafDensity * (double) fakeOakHeightLimit) / 13D, 2D));
        if (i < 1) {
            i = 1;
        }
        int[][] ai = new int[i * fakeOakHeightLimit][4];
        int j = (fakeOakBasePos[1] + fakeOakHeightLimit) - fakeOakLeafDistanceLimit;
        int k = 1;
        int l = fakeOakBasePos[1] + fakeOakHeight;
        int i1 = j - fakeOakBasePos[1];
        ai[0][0] = fakeOakBasePos[0];
        ai[0][1] = j;
        ai[0][2] = fakeOakBasePos[2];
        ai[0][3] = l;
        j--;
        while (i1 >= 0) {
            int j1 = 0;
            float f = layerSize(i1);
            if (f < 0.0F) {
                j--;
                i1--;
            } else {
                double d = 0.5D;
                for (; j1 < i; j1++) {
                    double d1 = fakeOakScaleWidth * ((double) f * ((double) rand.nextFloat() + 0.32800000000000001D));
                    double d2 = (double) rand.nextFloat() * 2D * 3.1415899999999999D;
                    int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + (double) fakeOakBasePos[0] + d);
                    int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + (double) fakeOakBasePos[2] + d);
                    int[] ai1 = { k1, j, l1 };
                    int[] ai2 = { k1, j + fakeOakLeafDistanceLimit, l1 };
                    if (checkBlockLine(ai1, ai2) != -1) {
                        continue;
                    }
                    int[] ai3 = { fakeOakBasePos[0], fakeOakBasePos[1], fakeOakBasePos[2] };
                    double d3 = Math.sqrt(
                            Math.pow(Math.abs(fakeOakBasePos[0] - ai1[0]), 2D) + Math.pow(Math.abs(fakeOakBasePos[2] - ai1[2]), 2D));
                    double d4 = d3 * fakeOakBranchSlope;
                    if ((double) ai1[1] - d4 > (double) l) {
                        ai3[1] = l;
                    } else {
                        ai3[1] = (int) ((double) ai1[1] - d4);
                    }
                    if (checkBlockLine(ai3, ai1) == -1) {
                        ai[k][0] = k1;
                        ai[k][1] = j;
                        ai[k][2] = l1;
                        ai[k][3] = ai3[1];
                        k++;
                    }
                }

                j--;
                i1--;
            }
        }
        fakeOakLeafNodes = new int[k][4];
        System.arraycopy(ai, 0, fakeOakLeafNodes, 0, k);
    }

    void generateLeafNode(int x, int y, int z, float radius, byte axis, Block block) {
        int i1 = (int) ((double) radius + 0.61799999999999999D);
        byte byte1 = otherCoordPairs[axis];
        byte byte2 = otherCoordPairs[axis + 3];
        int[] ai = { x, y, z };
        int[] ai1 = { 0, 0, 0 };
        int j1 = -i1;
        int k1 = -i1;
        ai1[axis] = ai[axis];
        for (; j1 <= i1; j1++) {
            ai1[byte1] = ai[byte1] + j1;
            for (int l1 = -i1; l1 <= i1;) {
                double d = Math
                        .sqrt(Math.pow((double) Math.abs(j1) + 0.5D, 2D) + Math.pow((double) Math.abs(l1) + 0.5D, 2D));
                if (d > (double) radius) {
                    l1++;
                } else {
                    ai1[byte2] = ai[byte2] + l1;
                    Block i2 = worldObj.getBlock(ai1[0], ai1[1], ai1[2]);
                    if (i2 != Blocks.air && i2 != Blocks.leaves) {
                        l1++;
                    } else {
                        setBlockAndNotifyAdequately(worldObj, ai1[0], ai1[1], ai1[2], block, 0);
                        l1++;
                    }
                }
            }
        }
    }

    float layerSize(int i) {
        if ((double) i < (double) (float) fakeOakHeightLimit * 0.29999999999999999D) {
            return -1.618F;
        }
        float f = (float) fakeOakHeightLimit / 2.0F;
        float f1 = (float) fakeOakHeightLimit / 2.0F - (float) i;
        float f2;
        if (f1 == 0.0F) {
            f2 = f;
        } else if (Math.abs(f1) >= f) {
            f2 = 0.0F;
        } else {
            f2 = (float) Math.sqrt(Math.pow(Math.abs(f), 2D) - Math.pow(Math.abs(f1), 2D));
        }
        f2 *= 0.5F;
        return f2;
    }

    float calculateLeafRadius(int height) {
        if (height < 0 || height >= fakeOakLeafDistanceLimit) {
            return -1F;
        } else {
            return height == 0 || height == fakeOakLeafDistanceLimit - 1 ? 2.0F : 3F;
        }
    }

    void generateLeafNode(int x, int y, int z) {
        int l = y;
        for (int i1 = y + fakeOakLeafDistanceLimit; l < i1; l++) {
            float f = calculateLeafRadius(l - y);
            generateLeafNode(x, l, z, f, (byte) 1, NContent.floraLeaves);
        }
    }

    void placeBlockLine(int[] start, int[] end, Block block) {
        int[] delta = { 0, 0, 0 };
        int longestAxis = 0;
        for (byte axis = 0; axis < 3; axis++) {
            delta[axis] = end[axis] - start[axis];
            if (Math.abs(delta[axis]) > Math.abs(delta[longestAxis])) {
                longestAxis = axis;
            }
        }

        if (delta[longestAxis] == 0) {
            return;
        }
        byte axisA = otherCoordPairs[longestAxis];
        byte axisB = otherCoordPairs[longestAxis + 3];
        byte longestAxisSign;
        if (delta[longestAxis] > 0) {
            longestAxisSign = 1;
        } else {
            longestAxisSign = -1;
        }
        double d = (double) delta[axisA] / (double) delta[longestAxis];
        double d1 = (double) delta[axisB] / (double) delta[longestAxis];
        int[] currentPos = { 0, 0, 0 };
        int k = 0;
        for (int l = delta[longestAxis] + longestAxisSign; k != l; k += longestAxisSign) {
            currentPos[longestAxis] = MathHelper.floor_double((double) (start[longestAxis] + k) + 0.5D);
            currentPos[axisA] = MathHelper.floor_double((double) start[axisA] + (double) k * d + 0.5D);
            currentPos[axisB] = MathHelper.floor_double((double) start[axisB] + (double) k * d1 + 0.5D);

            Block occupiedBy = worldObj.getBlock(currentPos[0], currentPos[1], currentPos[2]);
            if (occupiedBy != NContent.floraLeaves && occupiedBy != Blocks.air) continue;

            setBlockAndNotifyAdequately(worldObj, currentPos[0], currentPos[1], currentPos[2], block, 0);
        }
    }

    void generateLeaves() {
        for (int iter = 0; iter < this.fakeOakLeafNodes.length; iter++) {
            int posX = this.fakeOakLeafNodes[iter][0];
            int posY = this.fakeOakLeafNodes[iter][1];
            int posZ = this.fakeOakLeafNodes[iter][2];
            this.generateLeafNode(posX, posY, posZ);
        }
    }

    boolean leafNodeNeedsBase(int i) {
        return (double) i >= (double) fakeOakHeightLimit * 0.20000000000000001D;
    }

    void generateLeafNodeBases() {
        int i = 0;
        int j = fakeOakLeafNodes.length;
        int[] ai = { fakeOakBasePos[0], fakeOakBasePos[1], fakeOakBasePos[2] };
        for (; i < j; i++) {
            int[] ai1 = fakeOakLeafNodes[i];
            int[] ai2 = { ai1[0], ai1[1], ai1[2] };
            ai[1] = ai1[3];
            int k = ai[1] - fakeOakBasePos[1];
            if (leafNodeNeedsBase(k)) {
                placeBlockLine(ai, ai2, NContent.redwood);
            }
        }
    }

    int checkBlockLine(int[] start, int[] end) {
        int[] ai2 = { 0, 0, 0 };
        int i = 0;
        for (byte byte0 = 0; byte0 < 3; byte0++) {
            ai2[byte0] = end[byte0] - start[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[i])) {
                i = byte0;
            }
        }

        if (ai2[i] == 0) {
            return -1;
        }
        byte byte1 = otherCoordPairs[i];
        byte byte2 = otherCoordPairs[i + 3];
        byte byte3;
        if (ai2[i] > 0) {
            byte3 = 1;
        } else {
            byte3 = -1;
        }
        double d = (double) ai2[byte1] / (double) ai2[i];
        double d1 = (double) ai2[byte2] / (double) ai2[i];
        int[] ai3 = { 0, 0, 0 };
        int j = 0;
        int k = ai2[i] + byte3;
        do {
            if (j == k) {
                break;
            }
            ai3[i] = start[i] + j;
            ai3[byte1] = MathHelper.floor_double((double) start[byte1] + (double) j * d);
            ai3[byte2] = MathHelper.floor_double((double) start[byte2] + (double) j * d1);
            Block block = worldObj.getBlock(ai3[0], ai3[1], ai3[2]);
            if (block != Blocks.air && block != NContent.floraLeaves) {
                break;
            }
            j += byte3;
        } while (true);
        if (j == k) {
            return -1;
        } else {
            return Math.abs(j);
        }
    }
    /*
     * Unused? - jss2a98aj boolean validTreeLocation() { int ai[] = { basePos[0], basePos[1], basePos[2] }; int ai1[] =
     * { basePos[0], (basePos[1] + heightLimit) - 1, basePos[2] }; Block i = worldObj.getBlock(basePos[0], basePos[1] -
     * 1, basePos[2]); if (i != Blocks.dirt && i != Blocks.glass) { return false; } int j = checkBlockLine(ai, ai1); if
     * (j == -1) { return true; } if (j < 6) { return false; } else { heightLimit = j; return true; } } public void
     * func_517_a (double d, double d1, double d2) { heightLimitLimit = (int) (d * 12D); if (d > 0.5D) {
     * leafDistanceLimit = 5; } field_873_j = d1; field_872_k = d2; }
     */
}
