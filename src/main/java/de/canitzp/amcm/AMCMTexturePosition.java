package de.canitzp.amcm;

import net.minecraft.util.EnumFacing;

/**
 * @author canitzp
 */
public class AMCMTexturePosition {

    private int textureOffsetX, textureOffsetY, textureWidth, textureHeight;
    private double textureOffsetU, textureOffsetV;
    private EnumFacing side;

    public AMCMTexturePosition(int textureOffsetX, int textureOffsetY, int textureWidth, int textureHeight, EnumFacing side) {
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.side = side;
        this.textureOffsetU = textureOffsetX / textureWidth;
        this.textureOffsetV = textureOffsetY / textureHeight;
    }

    public int getTextureOffsetX() {
        return textureOffsetX;
    }

    public int getTextureOffsetY() {
        return textureOffsetY;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public double getTextureOffsetU() {
        return textureOffsetU;
    }

    public double getTextureOffsetV() {
        return textureOffsetV;
    }

    public EnumFacing getSide() {
        return side;
    }
}
