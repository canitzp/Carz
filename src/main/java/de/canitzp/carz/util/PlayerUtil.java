package de.canitzp.carz.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;

/**
 * @author canitzp
 */
public class PlayerUtil {

    public static boolean isOperator(EntityPlayer player){
        return player.world instanceof WorldServer && player.world.getMinecraftServer().getPlayerList().getOppedPlayers().getPermissionLevel(player.getGameProfile()) == 4;
    }

}
