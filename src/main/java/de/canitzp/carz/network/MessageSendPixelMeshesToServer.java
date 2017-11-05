package de.canitzp.carz.network;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class MessageSendPixelMeshesToServer implements IMessage, IMessageHandler<MessageSendPixelMeshesToServer, MessageSendPixelMeshesToServer> {

    private PixelMesh mesh;

    public MessageSendPixelMeshesToServer() {
    }

    public MessageSendPixelMeshesToServer(PixelMesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.mesh = new PixelMesh(buffer.readString(100), buffer);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeString(this.mesh.getName());
        this.mesh.toBytes(buffer);
    }

    @Override
    public MessageSendPixelMeshesToServer onMessage(MessageSendPixelMeshesToServer message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            WorldEvents.change(message.mesh);
            NetworkHandler.net.sendToAll(new MessageSendPixelMeshesToClient(WorldEvents.getMeshes()));
        });
        return null;
    }
}
