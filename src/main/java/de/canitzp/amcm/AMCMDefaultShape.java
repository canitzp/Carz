package de.canitzp.amcm;

import javafx.collections.transformation.SortedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author canitzp
 */
public abstract class AMCMDefaultShape<T extends AMCMDefaultShape> implements IAMCMShapes<T> {

    public int width, height, depth;
    public List<AMCMTexturePosition> texturePositions = new ArrayList<>();
    public AMCMTexture texture = AMCMTexture.MISSING.setForceTexture(false);
    private List<IAMCMBuffer> buffer = new ArrayList<>();
    private int displayList = -1;
    public AMCMShapeVar<Float> rotationPoint = new AMCMShapeVar<>(0F, 0F, 0F);
    public AMCMShapeVar<Float> rotationAngle = new AMCMShapeVar<>(0F, 0F, 0F);
    public AMCMShapeVar<Float> offset = new AMCMShapeVar<>(0F, 0F, 0F);
    public String name;
    private List<String> flags = new ArrayList<>();

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
        int w = this.width;
        int h = this.height;
        switch (facing){
            case EAST: case WEST: {
                w = this.depth;
                break;
            }
            case DOWN: case UP: {
                h = this.depth;
                break;
            }
        }
        for(AMCMTexturePosition pos : this.texturePositions){
            if(pos.getSide().equals(facing)){
                double left = pos.getTextureOffsetU();
                double top = pos.getTextureOffsetV();
                double right = left + (w / this.texture.getTextureWidth());
                double down = top + (h / this.texture.getTextureWidth());
                return new double[]{left, right, top, down};
            }
        }
        return new double[]{0, 0, 0, 0};
    }

    protected static void appendToBuilder(BufferBuilder builder, EnumFacing facing, double width, double height, double depth, double textureX, double textureY){
        float normX = (float) width, normY = (float) height, normZ = (float) depth;
        switch (facing){
            case NORTH: case SOUTH: {
                normX /= 2;
                normY /= 2;
                break;
            }
            case WEST: case EAST: {
                normY /= 2;
                normZ /= 2;
                break;
            }
            case UP: case DOWN: {
                normX /= 2;
                normZ /= 2;
                break;
            }
        }
        builder.pos(width, height, depth).tex(textureX, textureY).normal(normX, normY, normZ).endVertex();
    }

    public T setTexture(AMCMTexture texture){
        if(texture != null){
            this.texture = texture;
        }
        return (T) this;
    }

    public T setTextureOffset(EnumFacing facing, int textureOffsetX, int textureOffsetY){
        this.texturePositions.add(new AMCMTexturePosition(textureOffsetX, textureOffsetY, this.texture.getTextureWidth(), this.texture.getTextureHeight(), facing));
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
        String[] sFlags = flags.split("\\|");
        for(String flag : sFlags){
            if(flag.startsWith("customTexture:")){ //.class
                flag = flag.replace("customTexture:", "");
                String res = flag.substring(0, flag.length() - 6);
                int textureWidth = Integer.parseInt(flag.substring(flag.length() - 6, flag.length() - 3));
                int textureHeight = Integer.parseInt(flag.substring(flag.length() - 3));
                this.setTexture(new AMCMTexture(new ResourceLocation(res), textureWidth, textureHeight));
            } else {
                this.flags.add(flag);
            }
        }
        return (T) this;
    }

    public boolean hasFlags(){
        return !this.flags.isEmpty();
    }

    public List<String> getFlags(){
        return this.flags;
    }

    public String getTextureStringForFile(EnumFacing... facings){
        StringBuilder builder = new StringBuilder();
        for(EnumFacing facing : facings){
            System.out.println(facing);
            //int[] offs = this.textureOffsets.getOrDefault(facing, new int[]{0, 0});
            //builder.append(String.valueOf(offs[0]));
            //builder.append(";");
            //builder.append(String.valueOf(offs[1]));
            //builder.append(";");
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
        return String.format("%s:{name=%s, width=%d, height=%d, depth=%d, offset=%s, rotationPoint=%s, rotationAngle=%s}",
                this.getClass().getSimpleName(), this.name, this.width, this.height, this.depth, this.offset.toString(), this.rotationPoint.toString(), this.rotationAngle.toString());
    }
}
