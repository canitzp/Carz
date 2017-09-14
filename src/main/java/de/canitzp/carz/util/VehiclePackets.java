package de.canitzp.carz.util;

import de.canitzp.carz.api.EntityWorldInteractionBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * This class should be a util class,
 * where we can find all possible packets
 * to be send to the server or client
 *
 * @author canitzp
 */
public class VehiclePackets {

    /**
     * This send the page index update from the client to the server,
     * but it also sets it at the client.
     * @param vehicle The vehicle with inventory
     * @param newPage The new page index
     */
    @SideOnly(Side.CLIENT)
    public static void sendInventoryPageToServer(@Nonnull EntityWorldInteractionBase vehicle, int newPage){
        vehicle.setInventoryPageIndex(newPage); // Set it at the client
        // TODO send a packet to the server to set it there too
    }

}
