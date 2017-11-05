package de.canitzp.carz.client;

import de.canitzp.carz.events.WorldEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class PixelMeshScrollPane extends GuiScrollingList {

    private List<PixelMesh> meshList = new ArrayList<>(WorldEvents.getMeshes());
    private PixelMesh currentMesh = null;
    private int selected = -1;

    public PixelMeshScrollPane(GuiScreen parent, int x, int y, int width, int height, int xSize, int ySize) {
        super(parent.mc, width, height, y, y + height, x, 22, xSize, ySize);
    }

    @Override
    protected int getSize() {
        return this.meshList.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.currentMesh = this.meshList.get(index);
        this.selected = index;
    }

    @Override
    protected boolean isSelected(int index) {
        return index == this.selected;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        PixelMesh mesh = this.meshList.get(slotIdx);
        mesh.render(this.left + 3, slotTop + 1);
        font.drawString(mesh.getName(), this.left + 22, slotTop + 5, 0xFFFFFF);
    }

    public void setCurrentMesh(@Nullable PixelMesh currentMesh) {
        this.currentMesh = currentMesh;
    }

    @Nullable
    public PixelMesh getCurrentMesh() {
        return currentMesh;
    }

}
