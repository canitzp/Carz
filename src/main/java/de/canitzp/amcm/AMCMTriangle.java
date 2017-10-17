package de.canitzp.amcm;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

/**
 * @author canitzp
 */
public class AMCMTriangle extends AMCMDefaultShape<AMCMTriangle> {

    public AMCMTriangle(int width, int height, int depth, AMCMShapeVar<Integer> offset) {
        super(width, height, depth,
                Lists.newArrayList(new IAMCMBuffer() {
                    @Override
                    public void buffer(BufferBuilder builder, float scale, double width1, double height1, double depth1, EnumFacing facing, double textureOffsetLeft, double textureOffsetRight, double textureOffsetUp, double textureOffsetDown) {
                        AMCMShapeVar<Float> off = offset.toFloat(scale);
                        switch (facing) {
                            case NORTH: {
                                builder.pos(off.x, off.y, off.z).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                                builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                                builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).endVertex();
                                builder.pos(width1 + off.x, off.y, off.z).tex(textureOffsetRight, textureOffsetUp).endVertex();
                                break;
                            }
                            case SOUTH: {
                                builder.pos(width1 + off.x, off.y, off.z).tex(textureOffsetRight, textureOffsetUp).endVertex();
                                builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).endVertex();
                                builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                                builder.pos(off.x, off.y, off.z).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                                break;
                            }
                            case DOWN: {
                                builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                                builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                                builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).endVertex();
                                builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).endVertex();
                                break;
                            }
                        }
                    }

                    @Override
                    public boolean isValid(EnumFacing facing) {
                        return facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH || facing == EnumFacing.DOWN;
                    }

                }, new IAMCMBuffer() {
                    @Override
                    public void buffer(BufferBuilder builder, float scale, double width1, double height1, double depth1, EnumFacing facing, double textureOffsetLeft, double textureOffsetRight, double textureOffsetUp, double textureOffsetDown) {
                        AMCMShapeVar<Float> off = offset.toFloat(scale);
                        switch (facing) {
                            case WEST: {
                                builder.pos(off.x, off.y, off.z).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                                builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                                builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                                break;
                            }
                            case EAST: {
                                builder.pos(width1 + off.x, off.y, off.z).tex(textureOffsetRight, textureOffsetUp).endVertex();
                                builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).endVertex();
                                builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).endVertex();
                                break;
                            }
                        }
                    }

                    @Override
                    public int getGLMode() {
                        return GL11.GL_TRIANGLES;
                    }

                    @Override
                    public boolean isValid(EnumFacing facing) {
                        return facing == EnumFacing.WEST || facing == EnumFacing.EAST;
                    }
                }));
    }

}