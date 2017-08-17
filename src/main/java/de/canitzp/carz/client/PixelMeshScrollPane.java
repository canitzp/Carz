package de.canitzp.carz.client;

import de.canitzp.carz.events.WorldEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author canitzp
 */
public class PixelMeshScrollPane extends GuiScrollingList {

    private List<PixelMesh> meshList = new ArrayList<>(WorldEvents.MESHES_LOADED_INTO_WORLD.values());

    public PixelMeshScrollPane(GuiScreen parent, int x, int y, int width, int height, int entryHeight, int xSize, int ySize) {
        super(parent.mc, width, height, y, y + height, x, entryHeight, xSize, ySize);
    }

    @Override
    protected int getSize() {
        return this.meshList.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        PixelMesh mesh = this.meshList.get(slotIdx);
        font.drawString(mesh.getName(), this.left + 1, slotTop + 1, 0xFFFFFF);
    }
}
