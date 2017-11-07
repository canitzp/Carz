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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
@SideOnly(Side.CLIENT)
public class GuiPixelMesher extends GuiScreen {

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_pixel_mesher.png");

    private PixelMesh currentMesh;
    public int guiLeft, guiTop, currentColor = 0xFFFFFFFF;
    public GuiTextField nameField, colorField;
    private Robot robot;
    private PixelMeshScrollPane scrollPane;
    private EntityPlayer player;

    public GuiPixelMesher(EntityPlayer player){
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        this.player = player;
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - 256) / 2;
        this.guiTop = (this.height - 256) / 2;
        this.nameField = new GuiTextField(0, this.mc.fontRenderer, this.guiLeft + 157, this.guiTop + 7, 92, 10);
        this.nameField.setMaxStringLength(25);
        if(this.currentMesh != null){
            this.nameField.setText(this.currentMesh.getName());
        }
        this.colorField = new GuiTextField(1, this.mc.fontRenderer, this.guiLeft + 157, this.guiTop + 154, 92, 10);
        this.colorField.setText("#" + Integer.toHexString(this.currentColor));
        this.colorField.setMaxStringLength(9);
        this.colorField.setValidator(input -> input != null && input.matches("^[0-9A-F]+$"));
        this.scrollPane = new PixelMeshScrollPane(this, this.guiLeft + 6, this.guiTop + 156, 133, 94, 256, 256);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontRenderer font = this.mc.fontRenderer;
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 256, 256);
        String textNew = this.currentMesh == null ? "New" : "Change Name", textOpen = "Open"; //TODO localize
        font.drawString(textNew, (int) (this.guiLeft + 203 - (font.getStringWidth(textNew) / 2.0F)), this.guiTop + 22, 0xFFFFFF);
        font.drawString(textOpen, (int) (this.guiLeft + 203 - (font.getStringWidth(textOpen) / 2.0F)), this.guiTop + 36, 0xFFFFFF);
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
            if(isMouseBetween(mouseX, mouseY, 142, 208, 11, 11)){
                this.drawHoveringText("Reset to Default", mouseX, mouseY);
            }
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
            if(isMouseBetween(mouseX, mouseY, 156, 19, 94, 13)){ // New / Change Name
                if(this.currentMesh != null){ // Change Name
                    if(!this.nameField.getText().isEmpty()){
                        this.currentMesh.setName(this.nameField.getText());
                    }
                    this.initGui();
                } else { // New
                    if(!this.nameField.getText().isEmpty()){
                        this.setCurrentMesh(new PixelMesh(this.nameField.getText(), 16, this.player.getGameProfile().getId()));
                    }
                }
            } else if(isMouseBetween(mouseX, mouseY, 156, 33, 94, 13)){ // Open
                if(this.scrollPane.getCurrentMesh() != null){
                    if(this.scrollPane.getCurrentMesh().canBeEditedBy(this.player)){
                        this.setCurrentMesh(this.scrollPane.getCurrentMesh());
                        this.nameField.setText(this.currentMesh.getName());
                    }
                }
            }
            if(isMouseBetween(mouseX, mouseY, 156, 6, 94, 12)){
                this.nameField.setFocused(true);
            } else {
                this.nameField.setFocused(false);
            }
            /*
            if(mouseX >= this.guiLeft + 157 && mouseY >= this.guiTop + 48 && mouseX <= this.guiLeft + 248 && mouseY <= this.guiTop + 57){
                this.colorField.setFocused(true);
            } else {
                this.colorField.setFocused(false);
            }
            */
            if(this.currentMesh != null && isMouseBetween(mouseX, mouseY, 8, 8, 143, 143)){
                updateMesh(mouseX, mouseY);
            }
            if(isMouseBetween(mouseX, mouseY, 157, 59, 92, 92)){ // Update color
                updateColor();
            }
            if(isMouseBetween(mouseX, mouseY, 142, 156, 11, 11)){ // Offset y up
                if(this.currentMesh != null){
                    this.currentMesh.offset(0, -1);
                }
            }
            if(isMouseBetween(mouseX, mouseY, 142, 169, 11, 11)){ // Offset x left
                if(this.currentMesh != null){
                    this.currentMesh.offset(-1, 0);
                }
            }
            if(isMouseBetween(mouseX, mouseY, 142, 182, 11, 11)){ // Offset x right
                if(this.currentMesh != null){
                    this.currentMesh.offset(1, 0);
                }
            }
            if(isMouseBetween(mouseX, mouseY, 142, 195, 11, 11)){ // Offset y down
                if(this.currentMesh != null){
                    this.currentMesh.offset(0, 1);
                }
            }
            if(isMouseBetween(mouseX, mouseY, 142, 208, 11, 11)){ // Offset default
                if(this.currentMesh != null){
                    this.currentMesh.setOffset(0, 0);
                }
            }
        } else if(mouseButton == 1){
            if(this.currentMesh != null && isMouseBetween(mouseX, mouseY, 8, 8, 143, 143)){
                this.setPixelUnderMouse(mouseX, mouseY, Pixel.EMPTY);
            }
        } else if(mouseButton == 2){
            if(this.currentMesh != null && isMouseBetween(mouseX, mouseY, 8, 8, 143, 143)){
                this.currentColor = this.getPixelUnderMouse(mouseX, mouseY).toHex();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(clickedMouseButton == 0){
            if(this.currentMesh != null && isMouseBetween(mouseX, mouseY, 8, 8, 143, 143)){
                updateMesh(mouseX, mouseY);
            }
            if(isMouseBetween(mouseX, mouseY, 157, 59, 92, 92)){ // Update color
                updateColor();
            }
        } else if(clickedMouseButton == 1){
            if(this.currentMesh != null && isMouseBetween(mouseX, mouseY, 8, 8, 143, 143)){
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

    @Override
    public void onGuiClosed() {
        if(this.currentMesh != null){
            NetworkHandler.net.sendToServer(new MessageSendPixelMeshesToServer(this.currentMesh));
            WorldEvents.change(this.currentMesh);
        }
        super.onGuiClosed();
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
            this.currentMesh.setPixel(row, column, pixel);
        }
    }

    private void setCurrentMesh(PixelMesh mesh){
        this.currentMesh = mesh;
    }

    private boolean isMouseBetween(int mouseX, int mouseY, int x, int y, int width, int height){
        return mouseX >= this.guiLeft + x && mouseY >= this.guiTop + y && mouseX <= this.guiLeft + x + width && mouseY <= this.guiTop + y + height;
    }

}
