package de.canitzp.carz.network;

import de.canitzp.carz.api.EntitySteerableBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by MisterErwin on 13.08.2017.
 * In case you need it, ask me ;)
 */
public class MessageCarSpeed implements IMessage, IMessageHandler<MessageCarSpeed, IMessage> {
    private float speed;

    public MessageCarSpeed() {
    }

    public MessageCarSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.speed = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(speed);
    }

    @Override
    public IMessage onMessage(MessageCarSpeed message, MessageContext ctx) {
        if (ctx.side != Side.SERVER)
            return null;
        EntityPlayer entityPlayer = ctx.getServerHandler().player;
        Entity ridden = entityPlayer.getRidingEntity();
        if (ridden instanceof EntitySteerableBase)
            ((EntitySteerableBase) ridden).setSpeed(message.speed);
        return null;
    }
}
