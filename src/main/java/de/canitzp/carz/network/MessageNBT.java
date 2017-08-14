package de.canitzp.carz.network;

import de.canitzp.carz.tile.TileSign;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

/**
 * @author canitzp
 */
public class MessageNBT implements IMessage, IMessageHandler<MessageNBT, MessageNBT>{

    private NBTTagCompound nbt;
    private BlockPos pos;

    public MessageNBT(){}

    public MessageNBT(NBTTagCompound nbt, BlockPos pos){
        this.nbt = nbt;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            PacketBuffer buffer = new PacketBuffer(buf);
            this.nbt = buffer.readCompoundTag();
            this.pos = BlockPos.fromLong(buf.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeCompoundTag(this.nbt);
        buffer.writeLong(this.pos.toLong());
    }

    @Override
    public MessageNBT onMessage(MessageNBT message, MessageContext ctx) {
        World world = Minecraft.getMinecraft().world;
        TileEntity tile = world.getTileEntity(message.pos);
        if(tile != null && tile instanceof TileSign){
            System.out.println(message.nbt);
            ((TileSign) tile).handleUpdate(message.nbt);
        }
        return null;
    }

}
