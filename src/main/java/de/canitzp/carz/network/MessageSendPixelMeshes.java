package de.canitzp.carz.network;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class MessageSendPixelMeshes implements IMessage, IMessageHandler<MessageSendPixelMeshes, MessageSendPixelMeshes> {

    private List<PixelMesh> meshes;

    public MessageSendPixelMeshes(){ }

    public MessageSendPixelMeshes(List<PixelMesh> meshes){
        this.meshes = meshes;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.meshes = new ArrayList<>();
        PacketBuffer buffer = new PacketBuffer(buf);
        int amount = buffer.readInt();
        for(int i = 0; i < amount; i++){
            String name = buffer.readString(1000);
            this.meshes.add(new PixelMesh(name, buffer));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeInt(this.meshes.size());
        for(PixelMesh mesh : this.meshes){
            buffer.writeString(mesh.getName());
            mesh.toBytes(buffer);
        }
    }

    @Override
    public MessageSendPixelMeshes onMessage(MessageSendPixelMeshes message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            WorldEvents.MESHES_LOADED_INTO_WORLD.clear();
            WorldEvents.MESHES_LOADED_INTO_WORLD.addAll(message.meshes);
        });
        return null;
    }
}
