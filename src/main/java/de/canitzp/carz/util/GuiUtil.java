package de.canitzp.carz.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * @author canitzp
 */
public class GuiUtil {

    public static void drawCage(int x, int y, int width, int height, int color){
        Gui.drawRect(x, y, x + width, y + 1, color);                         // ===
        Gui.drawRect(x, y, x + 1, y + height, color);                        // =
        Gui.drawRect(x + width - 1, y, x + width, y + height, color); //   =
        Gui.drawRect(x, y + height - 1, x + width, y + height, color); // ===
    }

    public static boolean isMouseBetween(int guiLeft, int guiTop, int mouseX, int mouseY, int x, int y, int width, int height){
        return mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX <= guiLeft + x + width && mouseY <= guiTop + y + height;
    }

    public static void drawFluid(FluidTank tank, int x, int y, int width, int height) {
        if (tank != null && tank.getFluid() != null && tank.getFluid().getFluid() != null) {
            ResourceLocation fluidTexture = tank.getFluid().getFluid().getStill();
            fluidTexture = new ResourceLocation(fluidTexture.getNamespace(), "textures/" + fluidTexture.getPath() + ".png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(fluidTexture);
            int factor = tank.getFluidAmount() * height / tank.getCapacity();
            Gui.drawModalRectWithCustomSizedTexture(x, y + height - factor, 0, 0, width, factor, 16, 512);
        }
    }

    public static void drawCenteredString(String text, int center, int y, int color, boolean dropShadow){
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int width = fontRenderer.getStringWidth(text);
        fontRenderer.drawString(text, center - Math.round(width / 2.0F), y, color, dropShadow);
    }

    public static String getTextWithFormatting(String text, TextFormatting... formats){
        StringBuilder builder = new StringBuilder();
        for(TextFormatting format : formats){
            builder.append(format.toString());
        }
        return builder.append(text).append(TextFormatting.RESET.toString()).toString();
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), 0.0D).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), 0.0D).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), 0.0D).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

}
