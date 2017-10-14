package de.canitzp.amcm;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class AMCMBox extends AMCMDefaultShape<AMCMBox>{

    public static final AMCMBox STONE_BLOCK = new AMCMBox(1.0F, 1.0F, 1.0F).setTexture(new AMCMTexture(new ResourceLocation("minecraft:textures/blocks/stone.png"), 16, 16).setForceTexture(true));

    public float width, height, depth;
    public Map<EnumFacing, int[]> textureOffsets = new HashMap<>();
    public AMCMTexture texture = AMCMTexture.MISSING;

    public AMCMBox(float width, float height, float depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public AMCMBox setTexture(AMCMTexture texture){
        if(texture != null){
            this.texture = texture;
        }
        return this;
    }

    public AMCMBox setTextureOffset(Map<EnumFacing, int[]> textureOffsets){
        this.textureOffsets = textureOffsets;
        return this;
    }

    @Nullable
    @Override
    protected AMCMTexture getTexture() {
        return this.texture;
    }

    @Override
    public void compileList(BufferBuilder builder, float scale) {
        double width = this.width * scale;
        double height = this.height * scale;
        double depth = this.depth * scale;
        //this.texture.bind();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        //north
        double[] off = this.offset(EnumFacing.NORTH);
        builder.pos(0.0D, 0.0D, 0.0D).tex(off[0], off[2]).endVertex();
        builder.pos(0.0D, height, 0.0D).tex(off[0], off[3]).endVertex();
        builder.pos(width, height, 0.0D).tex(off[1], off[3]).endVertex();
        builder.pos(width, 0.0D, 0.0D).tex(off[1], off[2]).endVertex();

        //south
        off = this.offset(EnumFacing.SOUTH);
        builder.pos(width, 0.0D, depth).tex(off[1], off[2]).endVertex();
        builder.pos(width, height, depth).tex(off[1], off[3]).endVertex();
        builder.pos(0.0D, height, depth).tex(off[0], off[3]).endVertex();
        builder.pos(0.0D, 0.0D, depth).tex(off[0], off[2]).endVertex();

        //west
        off = this.offset(EnumFacing.WEST);
        builder.pos(0.0D, 0.0D, depth).tex(off[0], off[2]).endVertex();
        builder.pos(0.0D, height, depth).tex(off[0], off[3]).endVertex();
        builder.pos(0.0D, height, 0.0D).tex(off[0], off[3]).endVertex();
        builder.pos(0.0D, 0.0D, 0.0D).tex(off[0], off[2]).endVertex();

        //east
        off = this.offset(EnumFacing.EAST);
        builder.pos(width, 0.0D, 0.0D).tex(off[1], off[2]).endVertex();
        builder.pos(width, height, 0.0D).tex(off[1], off[3]).endVertex();
        builder.pos(width, height, depth).tex(off[1], off[3]).endVertex();
        builder.pos(width, 0.0D, depth).tex(off[1], off[2]).endVertex();

        //bottom
        off = this.offset(EnumFacing.DOWN);
        builder.pos(0.0D, 0.0D, depth).tex(off[0], off[2]).endVertex();
        builder.pos(0.0D, 0.0D, 0.0D).tex(off[0], off[2]).endVertex();
        builder.pos(width, 0.0D, 0.0D).tex(off[1], off[2]).endVertex();
        builder.pos(width, 0.0D, depth).tex(off[1], off[2]).endVertex();

        //top
        off = this.offset(EnumFacing.UP);
        builder.pos(0.0D, height, 0.0D).tex(off[0], off[3]).endVertex();
        builder.pos(0.0D, height, depth).tex(off[0], off[3]).endVertex();
        builder.pos(width, height, depth).tex(off[1], off[3]).endVertex();
        builder.pos(width, height, 0.0D).tex(off[1], off[3]).endVertex();

        Tessellator.getInstance().draw();
    }

    private double[] offset(EnumFacing facing){
        int[] texts = this.textureOffsets.getOrDefault(facing, new int[]{0, 0});
        int textOffX = texts[0];
        int textOffY = texts[1];
        double texLeft = textOffX / (this.texture.getTextureWidth() * 1.0);
        double texRight = (textOffX + this.width) / (this.texture.getTextureWidth() * 1.0);
        double texTop = textOffY / (this.texture.getTextureHeight() * 1.0);
        double texBottom = (textOffY + this.height) / (this.texture.getTextureHeight() * 1.0);
        return new double[]{texLeft, texRight, texTop, texBottom};
    }

}
