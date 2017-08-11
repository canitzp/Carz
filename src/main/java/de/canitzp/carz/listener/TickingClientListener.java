package de.canitzp.carz.listener;

import de.canitzp.carz.entity.car.EntityRideableBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Place drivers in 3rd person.
 * @author MisterErwin
 */
@SideOnly(Side.CLIENT)
public class TickingClientListener {
    private Minecraft minecraft = Minecraft.getMinecraft();

    @SubscribeEvent()
    public void onPlayerTick(TickEvent.PlayerTickEvent event){
        if (event.side!=Side.CLIENT || event.player != minecraft.player)
            return;
        Entity riding = minecraft.player.getRidingEntity();
        if (!(riding instanceof EntityRideableBase))
            return;
        minecraft.gameSettings.thirdPersonView = 1;
    }
}
