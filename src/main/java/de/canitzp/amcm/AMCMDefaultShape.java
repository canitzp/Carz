package de.canitzp.amcm;

import javafx.collections.transformation.SortedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author canitzp
 */
public abstract class AMCMDefaultShape<T extends AMCMDefaultShape> implements IAMCMShapes<T> {

    public int width, height, depth;
    public Map<EnumFacing, int[]> textureOffsets = new HashMap<>();
    public AMCMTexture texture = AMCMTexture.MISSING.setForceTexture(false);
    private List<IAMCMBuffer> buffer = new ArrayList<>();
    private int displayList = -1;
    public AMCMShapeVar<Float> rotationPoint = new AMCMShapeVar<>(0F, 0F, 0F);
    public AMCMShapeVar<Float> rotationAngle = new AMCMShapeVar<>(0F, 0F, 0F);
    public AMCMShapeVar<Float> offset = new AMCMShapeVar<>(0F, 0F, 0F);
    public String name;
    private String flags = "";

    public AMCMDefaultShape(int width, int height, int depth, Collection<IAMCMBuffer> buffers){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.buffer.addAll(buffers);
    }

    protected void compileList(BufferBuilder builder, float scale){
        double width = this.width * scale;
        double height = this.height * scale;
        double depth = this.depth * scale;
        for(IAMCMBuffer buffer : this.buffer){
            builder.begin(buffer.getGLMode(), buffer.getFormat());
            for(EnumFacing facing : EnumFacing.values()){
                if(buffer.isValid(facing)){
                    double[] off = this.calculateTextureOffset(facing, buffer.getAdditionalTextureOffset(facing));
                    buffer.buffer(builder, scale, width, height, depth, facing, off[0], off[1], off[2], off[3]);
                }
            }
            Tessellator.getInstance().draw();
        }

    }

    private double[] calculateTextureOffset(EnumFacing facing, int[] additionalOffset){
        int[] texts = this.textureOffsets.getOrDefault(facing, new int[]{0, 0});
        int textOffX = texts[0] + additionalOffset[0];
        int textOffY = texts[1] + additionalOffset[1];
        double texLeft = textOffX / (this.texture.getTextureWidth() * 1.0);
        double texRight = (textOffX + this.width) / (this.texture.getTextureWidth() * 1.0);
        double texTop = textOffY / (this.texture.getTextureHeight() * 1.0);
        double texBottom = (textOffY + this.height) / (this.texture.getTextureHeight() * 1.0);
        return new double[]{texLeft, texRight, texTop, texBottom};
    }

    public T setTexture(AMCMTexture texture){
        if(texture != null){
            this.texture = texture;
        }
        return (T) this;
    }

    public T setTextureOffset(EnumFacing facing, int textureOffsetX, int textureOffsetY){
        this.textureOffsets.put(facing, new int[]{textureOffsetX, textureOffsetY});
        return (T) this;
    }

    protected AMCMTexture getTexture() {
        return this.texture;
    }

    public T setDimension(int width, int height, int depth){
        this.width = width;
        this.height = height;
        this.depth = depth;
        return (T) this;
    }

    public T setOffset(float x, float y, float z){
        this.offset = new AMCMShapeVar<>(x, y, z);
        return (T) this;
    }

    public T setRotationPoint(float x, float y, float z){
        this.rotationPoint = new AMCMShapeVar<>(x, y, z);
        return (T) this;
    }

    public T setRotateAngle(float x, float y, float z){
        this.rotationAngle = new AMCMShapeVar<>(x, y, z);
        return (T) this;
    }

    public T setName(String name){
        this.name = name;
        return (T) this;
    }

    public T setFlags(String flags){
        this.flags = flags;
        return (T) this;
    }

    public boolean hasFlags(){
        return !StringUtils.isNullOrEmpty(this.flags);
    }

    public String getFlags(){
        return this.flags;
    }

    public String getTextureStringForFile(EnumFacing... facings){
        StringBuilder builder = new StringBuilder();
        for(EnumFacing facing : facings){
            System.out.println(facing);
            int[] offs = this.textureOffsets.getOrDefault(facing, new int[]{0, 0});
            builder.append(String.valueOf(offs[0]));
            builder.append(";");
            builder.append(String.valueOf(offs[1]));
            builder.append(";");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
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

        GlStateManager.translate(this.offset.x * scale, this.offset.y * scale, this.offset.z * scale);
        this.getTexture().bind();
        if (this.rotationAngle.allZero()) {
            if (this.rotationPoint.allZero()) {
                GlStateManager.callList(this.displayList);
            } else {
                GlStateManager.translate(this.rotationPoint.x * scale, this.rotationPoint.y * scale, this.rotationPoint.z * scale);
                GlStateManager.callList(this.displayList);
                GlStateManager.translate(-this.rotationPoint.x * scale, -this.rotationPoint.y * scale, -this.rotationPoint.z * scale);
            }
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPoint.x * scale, this.rotationPoint.y * scale, this.rotationPoint.z * scale);
            if (this.rotationAngle.z != 0.0F) {
                GlStateManager.rotate(this.rotationAngle.z * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
            }
            if (this.rotationAngle.y != 0.0F) {
                GlStateManager.rotate(this.rotationAngle.y * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }
            if (this.rotationAngle.x != 0.0F) {
                GlStateManager.rotate(this.rotationAngle.x * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            }
            GlStateManager.callList(this.displayList);
            GlStateManager.popMatrix();
        }
        GlStateManager.translate(-this.offset.x * scale, -this.offset.y * scale, -this.offset.z * scale);
        return (T) this;
    }

    @Override
    public void refreshResources() {
        this.displayList = -1;
    }

    @Override
    public String toString() {
        return String.format("%s:{name=%s, width=%f, height=%f, depth=%f, offset=%s, rotationPoint=%s, rotationAngle=%s}",
                this.getClass().getSimpleName(), this.name, this.width, this.height, this.depth, this.offset.toString(), this.rotationPoint.toString(), this.rotationAngle.toString());
    }
}
