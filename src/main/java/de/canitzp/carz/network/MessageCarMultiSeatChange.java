package de.canitzp.carz.network;

import de.canitzp.carz.entity.EntityBus;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Package to sync the seating-position to the clients
 * @author MisterErwin
 */
public class MessageCarMultiSeatChange implements IMessage, IMessageHandler<MessageCarMultiSeatChange, IMessage> {
    private int carID;
    private int seatID;
    private int entityID;

    public MessageCarMultiSeatChange() {
    }

    public MessageCarMultiSeatChange(int carID, int seatID, int entityID) {
        this.carID = carID;
        this.seatID = seatID;
        this.entityID = entityID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.carID = buf.readInt();
        this.seatID = buf.readInt();
        this.entityID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(carID);
        buf.writeInt(seatID);
        buf.writeInt(entityID);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageCarMultiSeatChange message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            EntityPlayer player = Minecraft.getMinecraft().player;
            Entity vehicle = player.world.getEntityByID(message.carID);
            if (vehicle instanceof EntityBus) { //TODO: Generify
                if (message.entityID == -1)
                    ((EntityBus) vehicle).passengerSeats.remove(message.seatID);
                else if (message.seatID == -1) {
                    Entity e = player.world.getEntityByID(message.entityID);
                    ((EntityBus) vehicle).passengerSeats.inverse().remove(e);
                } else {
                    Entity e = player.world.getEntityByID(message.entityID);
                    ((EntityBus) vehicle).passengerSeats.put(message.seatID, e);
                }
            }
        });
        return null;
    }
}
