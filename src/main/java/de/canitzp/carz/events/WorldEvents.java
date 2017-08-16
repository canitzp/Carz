package de.canitzp.carz.events;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.PixelMeshParser;
import de.canitzp.carz.network.MessageSendPixelMeshes;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class WorldEvents {

    public static final List<PixelMesh> MESHES_LOADED_INTO_WORLD = new ArrayList<>();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            File meshDir = new File(world.getSaveHandler().getWorldDirectory(), "carz" + File.separator + "pixel meshes");
            if (!meshDir.exists()) {
                meshDir.mkdirs();
            } else {
                File[] files = meshDir.listFiles();
                if (files != null) {
                    try {
                        MESHES_LOADED_INTO_WORLD.addAll(new PixelMeshParser(files).parseFiles());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            NetworkHandler.net.sendTo(new MessageSendPixelMeshes(MESHES_LOADED_INTO_WORLD), (EntityPlayerMP) player);
        }
    }

    public static PixelMesh getMeshByUUID(UUID id) {
        for (PixelMesh mesh : MESHES_LOADED_INTO_WORLD) {
            if (id.equals(mesh.getId())) {
                return mesh;
            }
        }
        return null;
    }

}
