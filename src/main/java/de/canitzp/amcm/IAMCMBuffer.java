package de.canitzp.amcm;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

/**
 * @author canitzp
 */
public interface IAMCMBuffer {

    default int getGLMode(){
        return GL11.GL_QUADS;
    }

    default VertexFormat getFormat(){
        return DefaultVertexFormats.POSITION_TEX_NORMAL;
    }

    default boolean isValid(EnumFacing facing){
        return true;
    }

    default int[] getAdditionalTextureOffset(EnumFacing facing){
        return new int[]{0, 0};
    }

    void buffer(BufferBuilder builder, float scale, double width, double height, double depth, EnumFacing facing, double textureOffsetLeft, double textureOffsetRight, double textureOffsetUp, double textureOffsetDown);

}
