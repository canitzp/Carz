package de.canitzp.carz.util;

import de.canitzp.carz.api.EntityMoveableBase;
import de.canitzp.carz.api.EntityWorldInteractionBase;
import de.canitzp.carz.network.MessageCarPartInteract;
import de.canitzp.carz.network.MessageCarStatus;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
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
     *
     * @param vehicle The vehicle with inventory
     * @param newPage The new page index
     */
    @SideOnly(Side.CLIENT)
    public static void sendInventoryPageToServer(@Nonnull EntityWorldInteractionBase vehicle, int newPage) {
        vehicle.setInventoryPageIndex(newPage); // Set it at the client
        // TODO send a packet to the server to set it there too
    }

    /**
     * This sends an interaction with a car part from the client to the server.
     *
     * @param entity    the base car
     * @param hand      the hand used
     * @param partIndex The index of the car part
     */
    @SideOnly(Side.CLIENT)
    public static void sendCarInteractToServer(@Nonnull Entity entity, @Nonnull EnumHand hand, int partIndex) {
        NetworkHandler.net.sendToServer(new MessageCarPartInteract(entity.getEntityId(), hand, partIndex));
    }

    /**
     * Sends the vehicle speed from the client to the server.
     * You may want to use {@link EntityMoveableBase#setSpeed(float)} to sync
     *
     * @param speed The speed of the vehicle
     * @deprecated use {@link EntityMoveableBase#setSpeed(float)}
     */
    @SideOnly(Side.CLIENT)
    public static void sendCarSpeedToServer(float speed) {
        /* just for documentation */
    }


    /**
     * This sends a car status from the client to the server.
     *
     * @param entity the base car
     * @param status the status
     */
    public static void sendCarStatusToServer(@Nonnull Entity entity, @Nonnull MessageCarStatus.STATUS status) {
        NetworkHandler.net.sendToServer(new MessageCarStatus(entity.getEntityId(), status));
    }

    /**
     * This sends a car status from the client to the server.
     *
     * @param entity the base car
     * @param status the status
     */
    public static void sendCarStatusToClients(@Nonnull Entity entity, @Nonnull MessageCarStatus.STATUS status) {
        NetworkHandler.net.sendToAll(new MessageCarStatus(entity.getEntityId(), status));
    }
}
