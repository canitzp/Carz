package de.canitzp.carz.client.gui;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.PixelMeshScrollPane;
import de.canitzp.carz.network.MessageUpdatePainter;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiMeshChooser extends GuiScreen{

    public int guiLeft, guiTop, painterSlot;
    private PixelMeshScrollPane scrollPane;
    private PixelMesh currentMesh = null;
    private EntityPlayer player;

    public GuiMeshChooser(EntityPlayer player, int painterSlot) {
        this.painterSlot = painterSlot;
        this.player = player;
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - 128) / 2;
        this.guiTop = (this.height - 192) / 2;
        this.scrollPane = new PixelMeshScrollPane(this, this.guiLeft, this.guiTop, 128, 192, 128, 192);
        if(this.currentMesh != null){
            this.scrollPane.setCurrentMesh(this.currentMesh);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.scrollPane.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        super.handleMouseInput();
        if(this.scrollPane != null){
            this.scrollPane.handleMouseInput(mouseX, mouseY);
        }
    }



    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        if(this.scrollPane.getCurrentMesh() != null){
            NetworkHandler.net.sendToServer(new MessageUpdatePainter(this.painterSlot, this.player.getEntityId(), this.scrollPane.getCurrentMesh().getId()));
        }
        super.onGuiClosed();
    }

    public void setCurrentMesh(PixelMesh currentMesh) {
        this.currentMesh = currentMesh;
    }
}
