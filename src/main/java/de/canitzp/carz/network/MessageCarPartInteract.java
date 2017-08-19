package de.canitzp.carz.network;

import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.api.EntitySteerableBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by MisterErwin on 13.08.2017.
 * In case you need it, ask me ;)
 */
public class MessageCarPartInteract implements IMessage, IMessageHandler<MessageCarPartInteract, IMessage> {
    private int entityID;
    private EnumHand hand;
    private int partIndex;

    public MessageCarPartInteract() {
    }

    public MessageCarPartInteract(int entityID, EnumHand hand, int partIndex) {
        this.entityID = entityID;
        this.hand = hand;
        this.partIndex = partIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.hand = EnumHand.values()[buf.readShort()];
        this.partIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeShort(hand.ordinal());
        buf.writeInt(partIndex);
    }

    @Override
    public IMessage onMessage(MessageCarPartInteract message, MessageContext ctx) {
        if (ctx.side != Side.SERVER)
            return null;

        EntityPlayer entityPlayer = ctx.getServerHandler().player;
        Entity e = entityPlayer.world.getEntityByID(message.entityID);
        if (e instanceof EntityPartedBase) {
            ((EntityPartedBase) e).processInitialInteract(entityPlayer, message.hand, message.partIndex);
        }
        return null;
    }
}
