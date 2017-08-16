package de.canitzp.carz.client.gui;

import de.canitzp.carz.Carz;
import de.canitzp.carz.client.PixelMesh;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class GuiPixelMesher extends GuiScreen {

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_pixel_mesher.png");

    private PixelMesh currentMesh;

    public GuiPixelMesher(){
        this.width = this.height = 256;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
