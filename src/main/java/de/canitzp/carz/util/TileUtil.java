package de.canitzp.carz.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author canitzp
 */
public class TileUtil {

    public static void sync(TileEntity tile){
        for(EntityPlayer player : tile.getWorld().playerEntities){
            if(player instanceof EntityPlayerMP){
                BlockPos pos = tile.getPos();
                SPacketUpdateTileEntity packet = tile.getUpdatePacket();
                if(player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64 && packet != null){
                    ((EntityPlayerMP) player).connection.sendPacket(packet);
                }
            }
        }
    }

}
