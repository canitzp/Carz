package de.canitzp.carz.gen;

import de.canitzp.carz.data.WorldData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * @author canitzp
 */
public class OilChunkGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(!world.isRemote){
            if(MathHelper.getInt(random, 0, 23) == 0){ // TODO config
                WorldData.addOilChunk(world.provider.getDimension(), chunkX, chunkZ, MathHelper.getInt(random, 1000000, 10000000));
            }
        }
    }

}
