package de.canitzp.carz.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
                if(player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64 && tile.getUpdatePacket() != null){
                    ((EntityPlayerMP) player).connection.sendPacket(tile.getUpdatePacket());
                }
            }
        }
    }

}
