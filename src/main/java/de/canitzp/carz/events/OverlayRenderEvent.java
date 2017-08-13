package de.canitzp.carz.events;

import de.canitzp.carz.Carz;
import de.canitzp.carz.api.EntitySteerableBase;
import de.canitzp.carz.client.GuiHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Carz.MODID)
public class OverlayRenderEvent {

    private static GuiHud hud;

    @SubscribeEvent
    public static void overlayRenderEvent(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player.isRiding() && player.getRidingEntity() instanceof EntitySteerableBase) {
                if (hud == null) {
                    hud = new GuiHud();
                }
                hud.render(event.getResolution(), (EntitySteerableBase) player.getRidingEntity());
            } else {
                hud = null;
            }
        }
    }

}
