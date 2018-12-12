package de.canitzp.carz.gen;

import de.canitzp.carz.Registry;
import de.canitzp.carz.blocks.BlockRubberLog;
import de.canitzp.carz.config.ConfigCarz;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author canitzp
 */
public class WorldGenRubberTree implements IWorldGenerator {

    private int minTreeHeight = 5;
    private IBlockState log = Registry.blockLog.getDefaultState();
    private IBlockState leaves = Registry.blockRubberLeaves.getDefaultState();
    private int populationValue = ConfigCarz.RUBBERTREES_POPULATION; // 1 = one tree per chunk
    private final List<String> BIOME_WHITELIST = Arrays.asList(ConfigCarz.RUBBERTREE_BIOME_WHITELIST);

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int dim = world.provider.getDimension();
        if(populationValue >= 1 && dim != -1 && dim != 1 && world.getWorldType() != WorldType.FLAT && rand.nextInt(populationValue - 1) == 0){
            int x = chunkX * 16 + rand.nextInt(16) + 8;
            int z = chunkZ * 16 + rand.nextInt(16) + 8;
            generateAt(rand, world, world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)));
        }
    }

    private void generateAt(Random rand, World world, BlockPos position){
        if(world.getBlockState(position.down()).getMaterial() != Material.GRASS || !BIOME_WHITELIST.contains(Biome.REGISTRY.getNameForObject(world.getBiome(position)).getPath())){
            return;
        }
        int i = rand.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + i + 1 <= world.getHeight()) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 1;

                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2) {
                    k = 2;
                }

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < world.getHeight()) {
                            BlockPos pos = new BlockPos(l, j, i1);
                            if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (flag) {
                IBlockState state = world.getBlockState(position.down());

                if (state.getBlock().canSustainPlant(state, world, position.down(), net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling) Blocks.SAPLING) && position.getY() < world.getHeight() - i - 1) {
                    state.getBlock().onPlantGrow(state, world, position.down(), position);
                    int k2 = 3;
                    int l2 = 0;

                    for (int i3 = position.getY() - 3 + i; i3 <= position.getY() + i; ++i3) {
                        int i4 = i3 - (position.getY() + i);
                        int j1 = 1 - i4 / 2;

                        for (int k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1) {
                            int l1 = k1 - position.getX();

                            for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2) {
                                int j2 = i2 - position.getZ();

                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i4 != 0) {
                                    BlockPos blockpos = new BlockPos(k1, i3, i2);
                                    state = world.getBlockState(blockpos);

                                    if (state.getBlock().isAir(state, world, blockpos) || state.getBlock().isLeaves(state, world, blockpos) || state.getMaterial() == Material.VINE) {
                                        world.setBlockState(blockpos, this.leaves, 3);
                                    }
                                }
                            }
                        }
                    }

                    for (int j3 = 0; j3 < i; ++j3) {
                        BlockPos upN = position.up(j3);
                        state = world.getBlockState(upN);

                        if (state.getBlock().isAir(state, world, upN) || state.getBlock().isLeaves(state, world, upN) || state.getMaterial() == Material.VINE) {
                            if(world.rand.nextInt(5) == 0){
                                world.setBlockState(position.up(j3), this.log.withProperty(BlockRubberLog.RUBBER, true).withProperty(BlockRubberLog.FACING, EnumFacing.byHorizontalIndex(new Random().nextInt(3))).withProperty(BlockRubberLog.CURRENT_RUBBER, true));
                            } else {
                                world.setBlockState(position.up(j3), this.log);
                            }
                        }
                    }
                }
            }
        }
    }
}
