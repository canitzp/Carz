package de.canitzp.carz.network;

import de.canitzp.carz.tile.TileBoostingRoad;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author MisterErwin
 */
public class MessageUpdateRoadConfigurator implements IMessage, IMessageHandler<MessageUpdateRoadConfigurator, MessageUpdateRoadConfigurator> {

    private int playerID;
    private int x, y, z;
    private boolean absolute;
    private int mx, my, mz;

    public MessageUpdateRoadConfigurator() {
    }

    public MessageUpdateRoadConfigurator(int playerID, int x, int y, int z, boolean absolute, int mx, int my, int mz) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.absolute = absolute;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
    }

    public MessageUpdateRoadConfigurator(int playerID, BlockPos pos, boolean absolute, int mx, int my, int mz) {
        this.playerID = playerID;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.absolute = absolute;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
    }

    public MessageUpdateRoadConfigurator(int playerID, BlockPos pos, TileBoostingRoad tile) {
        this.playerID = playerID;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.absolute = tile.isAbsolute();
        this.mx = tile.getX();
        this.my = tile.getY();
        this.mz = tile.getZ();
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.playerID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.absolute = buffer.readBoolean();
        this.mx = buffer.readInt();
        this.my = buffer.readInt();
        this.mz = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeInt(this.playerID);
        buffer.writeInt(this.x);
        buffer.writeInt(this.y);
        buffer.writeInt(this.z);
        buffer.writeBoolean(this.absolute);
        buffer.writeInt(this.mx);
        buffer.writeInt(this.my);
        buffer.writeInt(this.mz);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public MessageUpdateRoadConfigurator onMessage(MessageUpdateRoadConfigurator message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            if (ctx.getServerHandler().player.getEntityId() == message.playerID) {
                BlockPos pos = new BlockPos(message.x, message.y, message.z);
                TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(pos);
                if (tile instanceof TileBoostingRoad) {
                    ((TileBoostingRoad) tile).setAbsolute(message.absolute);
                    ((TileBoostingRoad) tile).setX(message.mx);
                    ((TileBoostingRoad) tile).setY(message.my);
                    ((TileBoostingRoad) tile).setZ(message.mz);
                }
            }
        });
        return null;
    }
}
