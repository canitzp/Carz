package de.canitzp.carz.network;

import de.canitzp.carz.items.ItemPainter;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @author canitzp
 */
public class MessageUpdatePainter implements IMessage, IMessageHandler<MessageUpdatePainter, MessageUpdatePainter> {

    private int slotID, playerID;
    private UUID meshID;

    public MessageUpdatePainter(){}

    public MessageUpdatePainter(int slotID, int playerID, UUID meshID){
        this.slotID = slotID;
        this.playerID = playerID;
        this.meshID = meshID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.slotID = buffer.readInt();
        this.playerID = buffer.readInt();
        this.meshID = buffer.readUniqueId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeInt(this.slotID);
        buffer.writeInt(this.playerID);
        buffer.writeUniqueId(this.meshID);
    }

    @Override
    public MessageUpdatePainter onMessage(MessageUpdatePainter message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            if(ctx.getServerHandler().player.getEntityId() == message.playerID){
                ItemStack stack = ctx.getServerHandler().player.inventory.getStackInSlot(message.slotID);
                if(!stack.isEmpty() && stack.getItem() instanceof ItemPainter){
                    NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                    nbt.setUniqueId("PixelMeshUUID", message.meshID);
                    stack.setTagCompound(nbt);
                }
            }
        });
        return null;
    }
}
