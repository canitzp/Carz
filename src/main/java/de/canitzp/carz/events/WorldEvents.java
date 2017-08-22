package de.canitzp.carz.events;

import de.canitzp.carz.client.Pixel;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.PixelMeshParser;
import de.canitzp.carz.data.WorldData;
import de.canitzp.carz.network.MessageSendPixelMeshesToClient;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class WorldEvents {

    public static final Map<UUID, PixelMesh> MESHES_LOADED_INTO_WORLD = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        try {
            World world = event.getWorld();
            if(!world.isRemote){
                MapStorage storage = world.getMapStorage();
                if(storage != null){
                    WorldSavedData data = storage.getOrLoadData(WorldData.class, WorldData.NAME);
                    if(data instanceof WorldData){
                        data.markDirty();
                    } else {
                        data = new WorldData(WorldData.NAME);
                        data.markDirty();
                        storage.setData(WorldData.NAME, data);
                    }
                }
            }
            if (!world.isRemote && world.provider.getDimension() == 0) {
                File meshDir = new File(world.getSaveHandler().getWorldDirectory(), "carz" + File.separator + "pixel meshes");
                if (!meshDir.exists()) {
                    meshDir.mkdirs();
                } else {
                    MESHES_LOADED_INTO_WORLD.putAll(PixelMeshParser.readMeshFile(meshDir.listFiles((dir, name) -> name.endsWith(".mesh"))));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        try {
            World world = event.getWorld();
            if (!world.isRemote && world.provider.getDimension() == 0) {
                File meshDir = new File(world.getSaveHandler().getWorldDirectory(), "carz" + File.separator + "pixel meshes");
                if (!meshDir.exists()) {
                    meshDir.mkdirs();
                }
                PixelMeshParser.writeMeshFile(meshDir, MESHES_LOADED_INTO_WORLD.values());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event){
        World world = event.getWorld();
        if(!world.isRemote){
            MapStorage storage = world.getMapStorage();
            if(storage != null){
                WorldSavedData data = storage.getOrLoadData(WorldData.class, WorldData.NAME);
                if(data instanceof WorldData){
                    data.markDirty();
                } else {
                    data = new WorldData(WorldData.NAME);
                    data.markDirty();
                    storage.setData(WorldData.NAME, data);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            NetworkHandler.net.sendTo(new MessageSendPixelMeshesToClient(MESHES_LOADED_INTO_WORLD.values()), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    public static void onChunkGenerate(ChunkGeneratorEvent.ReplaceBiomeBlocks event){ // TODO use another event, since this gets called for nearly every block instead of chunk
        World world = event.getWorld();
        if(world != null && !world.isRemote){
            // Don't use the World::rand here, otherwise the amount of oil would be the same if the coordinates are the same
            if(MathHelper.getInt(new Random(), 0, 23) == 0){ // TODO config
                WorldData.addOilChunk(world.provider.getDimension(), event.getX(), event.getZ(), MathHelper.getInt(new Random(), 1000000, 10000000));
            }
        }
    }

    @Nullable
    public static PixelMesh getMeshByUUID(UUID id) {
        return MESHES_LOADED_INTO_WORLD.getOrDefault(id, null);
    }

    public static void change(PixelMesh mesh){
        MESHES_LOADED_INTO_WORLD.put(mesh.getId(), mesh);
    }

}
