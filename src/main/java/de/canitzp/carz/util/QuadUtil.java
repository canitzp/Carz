package de.canitzp.carz.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class QuadUtil {
    public static BakedQuad createTriangle(VertexFormat format, TextureAtlasSprite sprite, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3){
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        Vec3d normal = new Vec3d(0, 0, 0);
        putVertex(format, sprite, builder, normal, x1, y1, z1, 16, 16, 1, 1, 1);
        putVertex(format, sprite, builder, normal, x2, y2, z2, 16, 0, 1, 1, 1);
        putVertex(format, sprite, builder, normal, x3, y3, z3, 0, 16, 1, 1, 1);
        putVertex(format, sprite, builder, normal, x3, y3, z3, 0, 0, 1, 1, 1);
        return builder.build();
    }

    public static BakedQuad createQuad(VertexFormat format, TextureAtlasSprite sprite, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4){
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        Vec3d normal = new Vec3d(0, 0, 0);
        putVertex(format, sprite, builder, normal, x1, y1, z1, 0, 0, 1, 1, 1);
        putVertex(format, sprite, builder, normal, x2, y2, z2, 0, 16, 1, 1, 1);
        putVertex(format, sprite, builder, normal, x3, y3, z3, 16, 16, 1, 1, 1);
        putVertex(format, sprite, builder, normal, x4, y4, z4, 16, 0, 1, 1, 1);
        return builder.build();
    }

    public static BakedQuad createQuad(VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4,
                                       float r, float g, float b){
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(ModelLoader.White.INSTANCE);
        Vec3d normal = new Vec3d(0, 0, 0);
        putVertex(format, ModelLoader.White.INSTANCE, builder, normal, x1, y1, z1, 0, 0, r, g, b);
        putVertex(format, ModelLoader.White.INSTANCE, builder, normal, x2, y2, z2, 0, 16, r, g, b);
        putVertex(format, ModelLoader.White.INSTANCE, builder, normal, x3, y3, z3, 16, 16, r, g, b);
        putVertex(format, ModelLoader.White.INSTANCE, builder, normal, x4, y4, z4, 16, 0, r, g, b);
        return builder.build();
    }

    public static void putVertex(VertexFormat format, TextureAtlasSprite sprite, UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v, float r, float g, float b) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, r, g, b);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = sprite.getInterpolatedU(u);
                        v = sprite.getInterpolatedV(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }
}
