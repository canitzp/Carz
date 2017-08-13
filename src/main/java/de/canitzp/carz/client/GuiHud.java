package de.canitzp.carz.client;

import de.canitzp.carz.Carz;
import de.canitzp.carz.api.EntitySteerableBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiHud extends Gui {

    private static final ResourceLocation HUD_RES = new ResourceLocation(Carz.MODID, "textures/gui/gui_car_hud.png");

    public void render(ScaledResolution res, EntitySteerableBase steerableBase) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float f = 0.25F * res.getScaleFactor();
        GlStateManager.translate(2 * f, res.getScaledHeight() - 66 * f, 0);
        GlStateManager.scale(f, f, f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(HUD_RES);
        this.drawTexturedModalRect(0, 0, 0, 0, 224, 64);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        String speed = String.format("%dkm/h", Math.round(steerableBase.speedSqAbs * 3.6D * 100.0D));
        f = 0.24F * res.getScaleFactor();
        GlStateManager.translate(53 * f, res.getScaledHeight() - 17 * f, 0);
        GlStateManager.scale(f, f, f);
        fontRenderer.drawString(speed, 0, 0, 0xFFFFFF);
        GlStateManager.popMatrix();
    }

}
