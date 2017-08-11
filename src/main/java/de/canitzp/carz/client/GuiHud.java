package de.canitzp.carz.client;

import de.canitzp.carz.api.EntitySteerableBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiHud extends Gui {

    //private static final ResourceLocation HUD_RES = new ResourceLocation("");

    public void render(ScaledResolution res, EntitySteerableBase steerableBase){
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        //Minecraft.getMinecraft().getTextureManager().bindTexture(HUD_RES);
        int width = res.getScaledWidth() / 2;

        double speed = steerableBase.speedSqAbs * 3.6D;
        String text = String.format("Speed: %dkm/h", Math.round(speed * 100.0D));

        fontRenderer.drawString(text, width - (fontRenderer.getStringWidth(text) / 2), res.getScaledHeight() - 32, 0xFFFFFF);
    }

}
