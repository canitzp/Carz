package de.canitzp.carz.network;

import de.canitzp.carz.api.EntityPartedBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
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
    private short handIndex;
    private int partIndex;

    public MessageCarPartInteract() {
    }

    public MessageCarPartInteract(int entityID, EnumHand hand, int partIndex) {
        this.entityID = entityID;
        this.handIndex = (short) hand.ordinal();
        this.partIndex = partIndex;
    }

    public MessageCarPartInteract(int entityID, short handIndex, int partIndex) {
        this.entityID = entityID;
        this.handIndex = handIndex;
        this.partIndex = partIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.handIndex = buf.readShort();
        this.partIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeShort(handIndex);
        buf.writeInt(partIndex);
    }

    @Override
    public IMessage onMessage(MessageCarPartInteract message, MessageContext ctx) {
        if (ctx.side != Side.SERVER)
            return null;

        EntityPlayer entityPlayer = ctx.getServerHandler().player;
        Entity e = entityPlayer.world.getEntityByID(message.entityID);
        if (e instanceof EntityPartedBase) {
            if (message.handIndex == 0 || message.handIndex == 1)
                ((EntityPartedBase) e).processInitialInteract(entityPlayer, EnumHand.values()[message.handIndex], message.partIndex);
            else if (message.handIndex == -1)
                ((EntityPartedBase) e).attackEntityFrom(DamageSource.GENERIC, 42, message.partIndex);
        }
        return null;
    }
}
