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
                            builder.pos(0.0D, 0.0D, 0.0D).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                            builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                            builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).endVertex();
                            builder.pos(width1, 0.0D, 0.0D).tex(textureOffsetRight, textureOffsetUp).endVertex();
                            break;
                        }
                        case SOUTH: {
                            builder.pos(width1, 0.0D, depth1).tex(textureOffsetRight, textureOffsetUp).endVertex();
                            builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).endVertex();
                            builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                            builder.pos(0.0D, 0.0D, depth1).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                            break;
                        }
                        case WEST: {
                            builder.pos(0.0D, 0.0D, depth1).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                            builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                            builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                            builder.pos(0.0D, 0.0D, 0.0D).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                            break;
                        }
                        case EAST: {
                            builder.pos(width1, 0.0D, 0.0D).tex(textureOffsetRight, textureOffsetUp).endVertex();
                            builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).endVertex();
                            builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).endVertex();
                            builder.pos(width1, 0.0D, depth1).tex(textureOffsetRight, textureOffsetUp).endVertex();
                            break;
                        }
                        case DOWN: {
                            builder.pos(0.0D, height1, 0.0D).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                            builder.pos(0.0D, height1, depth1).tex(textureOffsetLeft, textureOffsetDown).endVertex();
                            builder.pos(width1, height1, depth1).tex(textureOffsetRight, textureOffsetDown).endVertex();
                            builder.pos(width1, height1, 0.0D).tex(textureOffsetRight, textureOffsetDown).endVertex();
                            break;
                        }
                        case UP: {
                            builder.pos(0.0D, 0.0D, depth1).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                            builder.pos(0.0D, 0.0D, 0.0D).tex(textureOffsetLeft, textureOffsetUp).endVertex();
                            builder.pos(width1, 0.0D, 0.0D).tex(textureOffsetRight, textureOffsetUp).endVertex();
                            builder.pos(width1, 0.0D, depth1).tex(textureOffsetRight, textureOffsetUp).endVertex();
                            break;
                        }
                    }
                }));
    }

}