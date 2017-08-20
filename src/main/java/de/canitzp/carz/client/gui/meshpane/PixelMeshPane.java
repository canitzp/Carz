package de.canitzp.carz.client.gui.meshpane;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class PixelMeshPane {

    private int x, y, width, height, elementsPerRow;
    private GuiScreen parent;
    private List<PixelMeshElement> elements = new ArrayList<>();

    public PixelMeshPane(GuiScreen parent, int x, int y, int width, int height){
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.elementsPerRow = width / 20;
        this.elements.clear();
        for(PixelMesh mesh : WorldEvents.MESHES_LOADED_INTO_WORLD.values()){
            this.elements.add(new PixelMeshElement(mesh));
        }
    }

    public void draw(int mouseX, int mouseY){
        for (int i = 0; i < this.elements.size(); i++) {
            int j = i / this.elementsPerRow;
            int y = this.y + 20*j + 2;
            int x = this.x + 20*i + 2 - (j*this.width - j*2);
            this.elements.get(i).draw(x, y, mouseX, mouseY);
        }
        for(PixelMeshElement element : this.elements){
            element.drawMouse(this.parent, mouseX, mouseY);
        }
    }

}
