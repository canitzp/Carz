package de.canitzp.carz.events;

import de.canitzp.carz.client.Pixel;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.PixelMeshParser;
import de.canitzp.carz.network.MessageSendPixelMeshesToClient;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
            if (!world.isRemote && world.provider.getDimension() == 0) {
                File meshDir = new File(world.getSaveHandler().getWorldDirectory(), "carz" + File.separator + "pixel meshes");
                if (!meshDir.exists()) {
                    meshDir.mkdirs();
                } else {
                    MESHES_LOADED_INTO_WORLD.putAll(PixelMeshParser.readMeshFile(meshDir.listFiles()));
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
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            NetworkHandler.net.sendTo(new MessageSendPixelMeshesToClient(MESHES_LOADED_INTO_WORLD.values()), (EntityPlayerMP) player);
        }
    }

    public static PixelMesh getMeshByUUID(UUID id) {
        return MESHES_LOADED_INTO_WORLD.getOrDefault(id, null);
    }

    public static void change(PixelMesh mesh){
        MESHES_LOADED_INTO_WORLD.put(mesh.getId(), mesh);
    }

}
