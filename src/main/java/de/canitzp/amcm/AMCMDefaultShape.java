package de.canitzp.amcm;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public abstract class AMCMDefaultShape<T extends AMCMDefaultShape> implements IAMCMShapes<T> {

    private int displayList = -1;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public String name;

    protected abstract void compileList(BufferBuilder builder, float scale);

    @Nullable
    protected AMCMTexture getTexture(){
        return null;
    }

    public T setOffset(float x, float y, float z){
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return (T) this;
    }

    public T setRotationPoint(float x, float y, float z){
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
        return (T) this;
    }

    public T setRotateAngle(float x, float y, float z){
        this.rotateAngleX = x;
        this.rotateAngleY = y;
        this.rotateAngleZ = z;
        return (T) this;
    }

    public T setName(String name){
        this.name = name;
        return (T) this;
    }

    @Override
    public T render(float scale) {
        if(displayList == -1){
            this.displayList = GLAllocation.generateDisplayLists(1);
            GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);
            BufferBuilder builder = Tessellator.getInstance().getBuffer();
            this.compileList(builder, scale);
            GlStateManager.glEndList();
        }

        GlStateManager.translate(this.offsetX * scale, this.offsetY * scale, this.offsetZ * scale);
        //if(this.getTexture() != null){
            //this.getTexture().bind();
        //}
        //AMCMTexture.MISSING.setForceTexture(true).bind();
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
        GlStateManager.translate(-this.offsetX * scale, -this.offsetY * scale, -this.offsetZ * scale);
        return (T) this;
    }

    @Override
    public void refreshResources() {
        this.displayList = -1;
    }
}
