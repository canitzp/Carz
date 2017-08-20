package de.canitzp.carz.client.gui.meshpane;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.util.GuiUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author canitzp
 */
public class PixelMeshElement {

    private PixelMesh mesh;
    private boolean isMouseOver;

    public PixelMeshElement(PixelMesh mesh){
        this.mesh = mesh;
    }

    public void draw(int x, int y, int mouseX, int mouseY){
        GuiUtil.drawCage(x, y, 18, 18, 0xFF494949);
        this.mesh.render(x + 1, y + 1);
        this.isMouseOver = mouseX >= x && mouseX <= x + 18 && mouseY >= y && mouseY <= y + 18;
    }

    public void drawMouse(GuiScreen gui, int mouseX, int mouseY){
        if(this.isMouseOver){
            gui.drawHoveringText(this.mesh.getName(), mouseX, mouseY);
        }
    }

}
