package de.canitzp.carz.client.gui;

import de.canitzp.carz.Carz;
import de.canitzp.carz.client.Pixel;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.PixelMeshScrollPane;
import de.canitzp.carz.events.WorldEvents;
import de.canitzp.carz.network.MessageSendPixelMeshesToServer;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;

/**
 * @author canitzp
 */
public class GuiPixelMesher extends GuiScreen {

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_pixel_mesher.png");

    private PixelMesh currentMesh;
    public int guiLeft, guiTop, currentColor = 0xFFFFFFFF;
    public GuiTextField nameField, colorField;
    private Robot robot;
    private PixelMeshScrollPane scrollPane;

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
        this.nameField = new GuiTextField(0, this.mc.fontRenderer, this.guiLeft + 157, this.guiTop + 35, 92, 10);
        this.nameField.setMaxStringLength(25);
        if(this.currentMesh != null){
            this.nameField.setText(this.currentMesh.getName());
        }
        this.colorField = new GuiTextField(1, this.mc.fontRenderer, this.guiLeft + 157, this.guiTop + 154, 92, 10);
        this.colorField.setText("#" + Integer.toHexString(this.currentColor));
        this.colorField.setMaxStringLength(9);
        this.colorField.setValidator(input -> input != null && input.matches("^[0-9A-F]+$"));
        this.scrollPane = new PixelMeshScrollPane(this, this.guiLeft + 6, this.guiTop + 156, 147, 94, 10, 256, 256);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontRenderer font = this.mc.fontRenderer;
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 256, 256);
        String textNew = "New", textOpen = "Open", textSave = "Save"; //TODO localize
        font.drawString(textNew, (int) (this.guiLeft + 178 - (font.getStringWidth(textNew) / 2.0F)), this.guiTop + 9, 0xFFFFFF);
        font.drawString(textOpen, (int) (this.guiLeft + 228 - (font.getStringWidth(textOpen) / 2.0F)), this.guiTop + 9, 0xFFFFFF);
        font.drawString(textSave, (int) (this.guiLeft + 203 - (font.getStringWidth(textSave) / 2.0F)), this.guiTop + 23, 0xFFFFFF);
        this.nameField.drawTextBox();
        this.scrollPane.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawRect(this.guiLeft + 157, this.guiTop + 48, this.guiLeft + 249, this.guiTop + 58, this.currentColor);
        String hexText = "#" + Integer.toHexString(this.currentColor);
        font.drawString(hexText, (int) (this.guiLeft + 203 - (font.getStringWidth(hexText) / 2.0F)), this.guiTop + 49, ((0xFFFFFF - this.currentColor)));

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
                        Gui.drawRect(x, y, x + 8, y + 8, pixel.toHex());
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
                if(!this.nameField.getText().isEmpty()){
                    this.currentMesh = new PixelMesh(this.nameField.getText(), 16);
                }
            } else if(mouseX >= this.guiLeft + 205 && mouseY >= this.guiTop + 6 && mouseX <= this.guiLeft + 249 && mouseY <= this.guiTop + 18){
                if(this.scrollPane.getCurrentMesh() != null){
                    this.currentMesh = this.scrollPane.getCurrentMesh();
                    this.nameField.setText(this.currentMesh.getName());
                }
            } else if(mouseX >= this.guiLeft + 156 && mouseY >= this.guiTop + 20 && mouseX <= this.guiLeft + 249 && mouseY <= this.guiTop + 32){
                if(this.currentMesh != null){
                    if(!this.nameField.getText().isEmpty()){
                        this.currentMesh.setName(this.nameField.getText());
                    }
                    NetworkHandler.net.sendToServer(new MessageSendPixelMeshesToServer(this.currentMesh));
                    WorldEvents.change(this.currentMesh);
                    this.initGui();
                }
            }
            if(mouseX >= this.guiLeft + 156 && mouseY >= this.guiTop + 35 && mouseX <= this.guiLeft + 249 && mouseY <= this.guiTop + 45){
                this.nameField.setFocused(true);
            } else {
                this.nameField.setFocused(false);
            }
            if(mouseX >= this.guiLeft + 157 && mouseY >= this.guiTop + 48 && mouseX <= this.guiLeft + 248 && mouseY <= this.guiTop + 57){
                this.colorField.setFocused(true);
            } else {
                this.colorField.setFocused(false);
            }
            if(this.currentMesh != null && mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 151 && mouseY <= this.guiTop + 151){
                updateMesh(mouseX, mouseY);
            }
            if(mouseX >= this.guiLeft + 157 && mouseY >= this.guiTop + 59 && mouseX <= this.guiLeft + 248 && mouseY <= this.guiTop + 150){
                updateColor();
            }
        } else if(mouseButton == 1){
            if(this.currentMesh != null && mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 151 && mouseY <= this.guiTop + 151){
                this.setPixelUnderMouse(mouseX, mouseY, Pixel.EMPTY);
            }
        } else if(mouseButton == 2){
            if(this.currentMesh != null && mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 151 && mouseY <= this.guiTop + 151){
                this.currentColor = this.getPixelUnderMouse(mouseX, mouseY).toHex();
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
        } else if(clickedMouseButton == 1){
            if(this.currentMesh != null && mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 151 && mouseY <= this.guiTop + 151){
                this.setPixelUnderMouse(mouseX, mouseY, Pixel.EMPTY);
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        this.colorField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
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

    private void updateMesh(int mouseX, int mouseY){
        this.setPixelUnderMouse(mouseX, mouseY, new Pixel(this.currentColor));
    }

    private void updateColor(){
        Point point = MouseInfo.getPointerInfo().getLocation();
        this.currentColor = this.robot.getPixelColor(point.x, point.y).getRGB();
    }

    @Nonnull
    private Pixel getPixelUnderMouse(int mouseX, int mouseY){
        int column = (mouseX - this.guiLeft) / 9 - 1;
        int row = (mouseY - this.guiTop) / 9 - 1;
        if(column >= 0 && row >= 0){
            return this.currentMesh.getPixels()[row][column];
        }
        return Pixel.EMPTY;
    }

    private void setPixelUnderMouse(int mouseX, int mouseY, Pixel pixel){
        int column = (mouseX - this.guiLeft) / 9 - 1;
        int row = (mouseY - this.guiTop) / 9 - 1;
        if(column >= 0 && row >= 0){
            this.currentMesh.getPixels()[row][column] = pixel;
        }
    }

}
