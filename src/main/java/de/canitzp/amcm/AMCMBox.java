package de.canitzp.amcm;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author canitzp
 */
public class AMCMBox extends AMCMDefaultShape<AMCMBox> {

    public static final AMCMBox STONE_BLOCK = new AMCMBox(16, 16, 16).setTexture(new AMCMTexture(new ResourceLocation("minecraft:textures/blocks/stone.png"), 16, 16).setForceTexture(true));

    public AMCMBox(int width, int height, int depth) {
        super(width, height, depth,
                Collections.singleton((builder, scale, width1, height1, depth1, facing, textureOffsetLeft, textureOffsetRight, textureOffsetUp, textureOffsetDown) -> {
                    switch (facing) {
                        case NORTH: {
                            appendToBuilder(builder, facing, 0.0D, 0.0D, 0.0D, textureOffsetLeft, textureOffsetUp);
                            appendToBuilder(builder, facing,  0.0D, height1, 0.0D, textureOffsetLeft, textureOffsetDown);
                            appendToBuilder(builder, facing,  width1, height1, 0.0D, textureOffsetRight, textureOffsetDown);
                            appendToBuilder(builder, facing,  width1, 0.0D, 0.0D, textureOffsetRight, textureOffsetUp);
                            break;
                        }
                        case SOUTH: {
                            appendToBuilder(builder, facing,  width1, 0.0D, depth1, textureOffsetRight, textureOffsetUp);
                            appendToBuilder(builder, facing,  width1, height1, depth1, textureOffsetRight, textureOffsetDown);
                            appendToBuilder(builder, facing,  0.0D, height1, depth1, textureOffsetLeft, textureOffsetDown);
                            appendToBuilder(builder, facing,  0.0D, 0.0D, depth1, textureOffsetLeft, textureOffsetUp);
                            break;
                        }
                        case WEST: {
                            builder.pos(0.0D, 0.0D, depth1).tex(textureOffsetLeft, textureOffsetUp).normal(0, 0, 0).endVertex();
                            builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).normal(0, 0, 0).endVertex();
                            builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).normal(0, 0, 0).endVertex();
                            builder.pos(0.0D, 0.0D, 0.0D).tex(textureOffsetLeft, textureOffsetUp).normal(0, 0, 0).endVertex();
                            break;
                        }
                        case EAST: {
                            builder.pos(width1, 0.0D, 0.0D).tex(textureOffsetRight, textureOffsetUp).normal(0, 0, 0).endVertex();
                            builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).normal(0, 0, 0).endVertex();
                            builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).normal(0, 0, 0).endVertex();
                            builder.pos(width1, 0.0D, depth1).tex(textureOffsetRight, textureOffsetUp).normal(0, 0, 0).endVertex();
                            break;
                        }
                        case DOWN: {
                            builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).normal(0, 0, 0).endVertex();
                            builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).normal(0, 0, 0).endVertex();
                            builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).normal(0, 0, 0).endVertex();
                            builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).normal(0, 0, 0).endVertex();
                            break;
                        }
                        case UP: {
                            builder.pos(0.0D, 0.0D, depth1).tex(textureOffsetLeft, textureOffsetUp).normal(0, 0, 0).endVertex();
                            builder.pos(0.0D, 0.0D, 0.0D).tex(textureOffsetLeft, textureOffsetUp).normal(0, 0, 0).endVertex();
                            builder.pos(width1, 0.0D, 0.0D).tex(textureOffsetRight, textureOffsetUp).normal(0, 0, 0).endVertex();
                            builder.pos(width1, 0.0D, depth1).tex(textureOffsetRight, textureOffsetUp).normal(0, 0, 0).endVertex();
                            break;
                        }
                    }
                }));
    }

}