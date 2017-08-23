package de.canitzp.carz.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author canitzp
 */
public class WorldData extends WorldSavedData {

    public static final String NAME = "carz_data";

    private static final Map<Integer, List<Triple<Integer, Integer, Integer>>> OIL_CHUNKS = new HashMap<>();

    public WorldData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        OIL_CHUNKS.clear();
        for(String key : nbt.getKeySet()){
            if(key.startsWith("OilChunk")){
                int dimId = Integer.parseInt(key.replaceFirst("OilChunk", ""));
                NBTTagList dimList = nbt.getTagList(key, Constants.NBT.TAG_COMPOUND);
                for (NBTBase aDimList : dimList) {
                    NBTTagCompound chunkTag = (NBTTagCompound) aDimList;
                    addOilChunk(dimId, chunkTag.getInteger("ChunkX"), chunkTag.getInteger("ChunkZ"), chunkTag.getInteger("ChunkOil"));
                }
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        for(Map.Entry<Integer, List<Triple<Integer, Integer, Integer>>> entry : OIL_CHUNKS.entrySet()){
            NBTTagList dimList = new NBTTagList();
            for(Triple<Integer, Integer, Integer> coords : entry.getValue()){
                NBTTagCompound chunkTag = new NBTTagCompound();
                chunkTag.setInteger("ChunkX", coords.getLeft());
                chunkTag.setInteger("ChunkZ", coords.getMiddle());
                chunkTag.setInteger("ChunkOil", coords.getRight());
                dimList.appendTag(chunkTag);
            }
            compound.setTag("OilChunk" + String.valueOf(entry.getKey()), dimList);
        }
        return compound;
    }

    public static void addOilChunk(int dim, int x, int z, int oil){
        //System.out.println("Oil chunk at: " + x + " " + z + " with " + oil + "mB oil");
        List<Triple<Integer, Integer, Integer>> chunks = OIL_CHUNKS.getOrDefault(dim, new ArrayList<>());
        chunks.add(Triple.of(x, z, oil));
        OIL_CHUNKS.put(dim, chunks);
    }

    public static void addOilChunk(@Nonnull Chunk chunk, int oil){
        addOilChunk(chunk.getWorld().provider.getDimension(), chunk.x, chunk.z, oil);
    }

    public static int getOilInChunk(@Nonnull World world, int x, int z){
        List<Triple<Integer, Integer, Integer>> chunks = OIL_CHUNKS.getOrDefault(world.provider.getDimension(), new ArrayList<>());
        for(Triple<Integer, Integer, Integer> coords : chunks){
            if(coords.getLeft() == x && coords.getMiddle() == z){
                return coords.getRight();
            }
        }
        return 0;
    }

    public static boolean hasChunkOil(@Nonnull World world, int x, int z){
        return getOilInChunk(world, x, z) > 0;
    }
}
