package de.canitzp.voxeler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class VoxelPart {

    public List<VoxelBox> cubes = new ArrayList<>();
    /** The size of the texture file's width in pixels. */
    public int textureWidth;
    /** The size of the texture file's height in pixels. */
    public int textureHeight;
    /** The X offset into the texture used for displaying this model */
    public int textureOffsetX;
    /** The Y offset into the texture used for displaying this model */
    public int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean compiled;
    /** The GL display list rendered by the Tessellator for this model */
    private int displayList;
    public boolean mirror;
    public boolean showModel;
    /** Hides the model. */
    public boolean isHidden;
    public String boxName;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public ResourceLocation texture;

    public VoxelPart(String name, int textureOffsetX, int textureOffsetY, int textureWidth, int textureHeight){
        this.boxName = name;
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public VoxelPart addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth, float scale){
        this.cubes.add(VoxelBox.from(this, this.boxName + "." + partName, offX, offY, offZ, width, height, depth, scale));
        return this;
    }

    public void render(float scale){
        if(!this.isHidden && this.showModel){
            if(!this.compiled){
                this.compile(scale);
            }
            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
            if(this.texture != null){
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
            }
            if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                    GlStateManager.callList(this.displayList);
                } else {
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    GlStateManager.callList(this.displayList);
                    GlStateManager.translate(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
                }
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                }
                GlStateManager.callList(this.displayList);
                GlStateManager.popMatrix();
            }
            GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
        }
    }

    private void compile(float scale){
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        for(VoxelBox box : this.cubes){
            box.render(bufferbuilder, scale);
        }
        GlStateManager.glEndList();
        this.compiled = true;
    }

    public boolean hasCustomTexture(){
        return this.texture != null;
    }

    public VoxelPart setOffset(float offsetX, float offsetY, float offsetZ){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        return this;
    }

}
