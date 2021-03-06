package de.canitzp.carz.network;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.models.road.BakedRoadModel;
import de.canitzp.carz.events.WorldEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author canitzp
 */
public class MessageSendPixelMeshesToClient implements IMessage, IMessageHandler<MessageSendPixelMeshesToClient, MessageSendPixelMeshesToClient> {

    private Collection<PixelMesh> meshes;

    public MessageSendPixelMeshesToClient() {
    }

    public MessageSendPixelMeshesToClient(Collection<PixelMesh> meshes) {
        this.meshes = meshes;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.meshes = new ArrayList<>();
        PacketBuffer buffer = new PacketBuffer(buf);
        int amount = buffer.readInt();
        for (int i = 0; i < amount; i++) {
            String name = buffer.readString(1000);
            this.meshes.add(new PixelMesh(name, buffer));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeInt(this.meshes.size());
        for (PixelMesh mesh : this.meshes) {
            buffer.writeString(mesh.getName());
            mesh.toBytes(buffer);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MessageSendPixelMeshesToClient onMessage(MessageSendPixelMeshesToClient message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            //Remove PixelMeshes no longer used (remember: the are cached by the tile entities)
            Collection<UUID> meshIDs = message.meshes.stream().map(PixelMesh::getId).collect(Collectors.toCollection(HashSet::new));
            WorldEvents.MESHES_LOADED_INTO_WORLD.entrySet().removeIf(e -> !meshIDs.contains(e.getKey()));
            // Remove any baked models for this mesh
            BakedRoadModel.MODEL_CACHE.asMap().entrySet().removeIf((v)->v.getKey().mesh!=null);
            for (PixelMesh mesh : message.meshes) {
                PixelMesh loadedMesh = WorldEvents.MESHES_LOADED_INTO_WORLD.get(mesh.getId());
                if (loadedMesh != null) {
                    loadedMesh.loadFrom(mesh);
                } else {
                    WorldEvents.MESHES_LOADED_INTO_WORLD.put(mesh.getId(), mesh);
                }
            }
        });
        return null;
    }
}
