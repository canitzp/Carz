package de.canitzp.carz.client;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.network.PacketBuffer;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author canitzp
 */
public class PixelMesh {

    private Pixel[][] pixels;
    private String name, fileNameLoadedFrom;
    private UUID id;

    public PixelMesh(String name, int size, UUID id) {
        this.name = name;
        this.id = id;
        this.pixels = new Pixel[size][size];
        for(int i = 0; i < this.pixels.length; i++){
            this.pixels[i] = getUniqueFilledPixelArray(size);
        }
    }

    public PixelMesh(String name, int size) {
        this(name, size, UUID.randomUUID());
    }

    public PixelMesh(String name, PacketBuffer buf) {
        this.name = name;
        this.fromBytes(buf);
    }

    private Pixel[] getUniqueFilledPixelArray(int size){
        Pixel[] empty = new Pixel[size];
        Arrays.fill(empty, Pixel.EMPTY);
        return empty;
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileNameLoadedFrom() {
        return fileNameLoadedFrom;
    }

    public void setFileNameLoadedFrom(String fileNameLoadedFrom) {
        this.fileNameLoadedFrom = fileNameLoadedFrom;
    }

    private boolean isInRange(int line) {
        return line >= this.pixels.length;
    }

    private void checkRange(int line) {
        if (isInRange(line)) {
            throw new RuntimeException("You're trying to get a line of a PixelMesh that hasn't the needed size! You want: " + line + "  We have as maximum: " + (this.pixels.length - 1));
        }
    }

    public Pixel[] getLine(int line) {
        checkRange(line);
        return this.pixels[line];
    }

    public PixelMesh setLine(int line, Pixel[] pixels) {
        checkRange(line);
        if (this.pixels.length != pixels.length) {
            throw new RuntimeException("You want to set a line of Pixels to a PixelMesh, but the sizes are different! Your size: " + pixels.length + "  Size of the Mesh: " + this.pixels.length);
        }
        this.pixels[line] = pixels;
        return this;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(this.getPixels());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(this.id);
        buf.writeInt(this.pixels.length);
        for (Pixel[] px : this.pixels) {
            for (Pixel p : px) {
                p.toBytes(buf);
            }
        }
    }

    public void fromBytes(PacketBuffer buf) {
        this.id = buf.readUniqueId();
        int amount = buf.readInt();
        this.pixels = new Pixel[amount][amount];
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                this.pixels[i][j] = new Pixel(buf);
            }
        }
    }

    public void render(int x, int y){
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Pixel[][] pixels1 = this.pixels;
        for (int row = 0; row < pixels1.length; row++) {
            Pixel[] pixels = pixels1[row];
            for (int column = 0; column < pixels.length; column++) {
                Pixel pixel = pixels[column];
                if(pixel.isValid()){
                    pixel.render(x + column, y + row);
                }
            }
        }
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

}
