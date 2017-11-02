package de.canitzp.carz.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by MisterErwin on 13.08.2017.
 * In case you need it, ask me ;)
 */
public class MessageCarStatus implements IMessage, IMessageHandler<MessageCarStatus, IMessage> {
    private int entityID;
    private STATUS status;

    public MessageCarStatus() {
    }

    public MessageCarStatus(int entityID, STATUS status) {
        this.entityID = entityID;
        this.status = status;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.status = STATUS.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeInt(status.ordinal());
    }

    @Override
    public IMessage onMessage(MessageCarStatus message, MessageContext ctx) {

        Entity e = ctx.getServerHandler().player.world.getEntityByID(message.entityID);
        switch (message.status) {
            case AI_CONTROLLED_START:
                if (ctx.side != Side.CLIENT)
                    return null;
        }
        return null;
    }

    public enum STATUS {
        AI_CONTROLLED_START,
        AI_CONTROLLED_STOP,
    }
}
