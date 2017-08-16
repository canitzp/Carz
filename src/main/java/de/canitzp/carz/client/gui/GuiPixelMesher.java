package de.canitzp.carz.client.gui;

import de.canitzp.carz.Carz;
import de.canitzp.carz.client.Pixel;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import de.canitzp.carz.network.MessageSendPixelMeshesToServer;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author canitzp
 */
public class GuiPixelMesher extends GuiScreen {

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_pixel_mesher.png");

    private PixelMesh currentMesh;
    public int guiLeft, guiTop, currentColor = 0xFFFFFFFF;
    public GuiTextField textField;
    private Robot robot;

    public GuiPixelMesher(){
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - 256) / 2;
        this.guiTop = (this.height - 256) / 2;
        this.textField = new GuiTextField(0, this.mc.fontRenderer, this.guiLeft + 157, this.guiTop + 35, 92, 10);
        this.textField.setMaxStringLength(25);
        if(this.currentMesh != null){
            this.textField.setText(this.currentMesh.getName());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 256, 256);
        String textNew = "New", textOpen = "Open", textSave = "Save"; //TODO localize
        this.mc.fontRenderer.drawString(textNew, (int) (this.guiLeft + 178 - (this.mc.fontRenderer.getStringWidth(textNew) / 2.0F)), this.guiTop + 9, 0xFFFFFF);
        this.mc.fontRenderer.drawString(textOpen, (int) (this.guiLeft + 228 - (this.mc.fontRenderer.getStringWidth(textOpen) / 2.0F)), this.guiTop + 9, 0xFFFFFF);
        this.mc.fontRenderer.drawString(textSave, (int) (this.guiLeft + 203 - (this.mc.fontRenderer.getStringWidth(textSave) / 2.0F)), this.guiTop + 23, 0xFFFFFF);
        this.textField.drawTextBox();
        Gui.drawRect(this.guiLeft + 157, this.guiTop + 52, this.guiLeft + 249, this.guiTop + 57, this.currentColor);

        if(this.currentMesh == null){
            Gui.drawRect(this.guiLeft + 7, this.guiTop + 7, this.guiLeft + 151, this.guiTop + 151, 0xF0000000);
            if(mouseX >= this.guiLeft + 6 && mouseY >= this.guiTop + 6 && mouseX <= this.guiLeft + 152 && mouseY <= this.guiTop + 152){
                this.drawHoveringText("You first have to create a mesh file to draw.", mouseX, mouseY);
            }
        } else {
            Pixel[][] pixels1 = this.currentMesh.getPixels();
            for (int row = 0; row < pixels1.length; row++) {
                Pixel[] pixels = this.currentMesh.getLine(row);
                for (int column = 0; column < pixels.length; column++) {
                    Pixel pixel = pixels[column];
                    if(pixel.isValid()){
                        int x = this.guiLeft + 8 + (column * 9);
                        int y = this.guiTop + 8 + (row * 9);
                        Gui.drawRect(x, y, x + 8, y + 8, 0x80000000);
                    }
                }
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton == 0){
            if(mouseX >= this.guiLeft + 156 && mouseY >= this.guiTop + 6 && mouseX <= this.guiLeft + 200 && mouseY <= this.guiTop + 18){
                if(!this.textField.getText().isEmpty()){
                    this.currentMesh = new PixelMesh(this.textField.getText(), 16);
                }
            } else if(mouseX >= this.guiLeft + 205 && mouseY >= this.guiTop + 6 && mouseX <= this.guiLeft + 249 && mouseY <= this.guiTop + 18){
                System.out.println("open");
            } else if(mouseX >= this.guiLeft + 156 && mouseY >= this.guiTop + 20 && mouseX <= this.guiLeft + 249 && mouseY <= this.guiTop + 32){
                if(this.currentMesh != null){
                    if(!this.textField.getText().isEmpty()){
                        this.currentMesh.setName(this.textField.getText());
                    }
                    NetworkHandler.net.sendToServer(new MessageSendPixelMeshesToServer(this.currentMesh));
                }
            }
            if(mouseX >= this.guiLeft + 156 && mouseY >= this.guiTop + 35 && mouseX <= this.guiLeft + 249 && mouseY <= this.guiTop + 45){
                this.textField.setFocused(true);
            } else {
                this.textField.setFocused(false);
            }
            if(this.currentMesh != null && mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 151 && mouseY <= this.guiTop + 151){
                updateMesh(mouseX, mouseY);
            }
            if(mouseX >= this.guiLeft + 157 && mouseY >= this.guiTop + 58 && mouseX <= this.guiLeft + 248 && mouseY <= this.guiTop + 149){
                updateColor();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(clickedMouseButton == 0){
            if(this.currentMesh != null && mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 151 && mouseY <= this.guiTop + 151){
                updateMesh(mouseX, mouseY);
            }
            if(mouseX >= this.guiLeft + 157 && mouseY >= this.guiTop + 58 && mouseX <= this.guiLeft + 248 && mouseY <= this.guiTop + 149){
                updateColor();
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.textField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    private void updateMesh(int mouseX, int mouseY){
        int column = (mouseX - this.guiLeft) / 9 - 1;
        int row = (mouseY - this.guiTop) / 9 - 1;
        if(column >= 0 && row >= 0){
            this.currentMesh.getPixels()[row][column] = new Pixel(this.currentColor);
        }
    }

    private void updateColor(){
        Point point = MouseInfo.getPointerInfo().getLocation();
        this.currentColor = this.robot.getPixelColor(point.x, point.y).getRGB();
    }

}
