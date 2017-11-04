package de.canitzp.carz.events;

import de.canitzp.carz.Carz;
import de.canitzp.carz.api.EntitySteerableBase;
import de.canitzp.carz.client.GuiHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.util.text.TextFormatting.*;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Carz.MODID)
public class OverlayRenderEvent {

    private static GuiHud hud;
    private static final String pocText = GRAY.toString() + BOLD + Carz.MODNAME + RESET + ITALIC +  GRAY + " " + Carz.MODVERSION + " Build-Date: " + Carz.BUILDDATE + RESET;

    @SubscribeEvent
    public static void overlayRenderEvent(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player.isRiding() && player.getRidingEntity() instanceof EntitySteerableBase && player.getRidingEntity().getControllingPassenger() == player) {
                if (hud == null) {
                    hud = new GuiHud();
                }
                hud.render(event.getResolution(), (EntitySteerableBase) player.getRidingEntity());
            } else {
                hud = null;
            }
        } else if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
            /*if(Carz.MODVERSION.contains("Proof of Concept")){
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                Minecraft.getMinecraft().fontRenderer.drawString(pocText, 2, 2, 0xFFFFFF);
                GlStateManager.popMatrix();
            }*/
        }
    }

}
