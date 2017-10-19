package de.canitzp.carz.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
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
            fluidTexture = new ResourceLocation(fluidTexture.getResourceDomain(), "textures/" + fluidTexture.getResourcePath() + ".png");
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

}
