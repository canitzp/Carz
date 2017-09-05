package de.canitzp.carz.client.models;

import de.canitzp.carz.Carz;
import de.canitzp.carz.util.BlockProps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Please don't ask me for help with such shit in other mods,
 * the only thing I do here is changing numbers up to a point where it works
 * like I want it.
 * @author canitzp
 */
public class ModelRoadSlope implements IBakedModel {

    public static final ModelResourceLocation BAKED_MODEL = new ModelResourceLocation(Carz.MODID + ":road_slope");

    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public ModelRoadSlope(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.format = format;
        this.sprite = bakedTextureGetter.apply(new ResourceLocation(Carz.MODID, "blocks/road"));
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state != null) {
            switch (state.getValue(BlockProps.FACING)) {
                case NORTH: {
                    switch (state.getValue(BlockProps.SLOPE_NUMBER)) {
                        case 0: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 0.25, 0, 1, 0, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 0, 1, 0, 0.25, 0, 0, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 0.25, 0, 0, 0.25, 0, 0, 0, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 0.5, 0, 1, 0.25, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 0.25, 1, 0, 0.5, 0, 0, 0, 0));
                            quads.add(createQuad(1, 0.25, 1, 1, 0.5, 0, 0, 0.5, 0, 0, 0.25, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 0.75, 0, 1, 0.5, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 0.5, 1, 0, 0.75, 0, 0, 0, 0));
                            quads.add(createQuad(1, 0.5, 1, 1, 0.75, 0, 0, 0.75, 0, 0, 0.5, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 1, 0, 1, 0.75, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 0.75, 1, 0, 1, 0, 0, 0, 0));
                            quads.add(createQuad(1, 0.75, 1, 1, 1, 0, 0, 1, 0, 0, 0.75, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            quads.add(createQuad(0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0)); // Back
                            return quads;
                        }
                    }
                }
                case SOUTH: {
                    switch (state.getValue(BlockProps.SLOPE_NUMBER)) {
                        case 0: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 0, 0, 1, 0.25, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 0.25, 1, 0, 0, 0, 0, 0, 0));
                            quads.add(createQuad(1, 0.25, 1, 1, 0, 0, 0, 0, 0, 0, 0.25, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 0.25, 0, 1, 0.5, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 0.5, 1, 0, 0.25, 0, 0, 0, 0));
                            quads.add(createQuad(1, 0.5, 1, 1, 0.25, 0, 0, 0.25, 0, 0, 0.5, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 0.5, 0, 1, 0.75, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 0.75, 1, 0, 0.5, 0, 0, 0, 0));
                            quads.add(createQuad(1, 0.75, 1, 1, 0.5, 0, 0, 0.5, 0, 0, 0.75, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(1, 0, 0, 1, 0.75, 0, 1, 1, 1, 1, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 1, 1, 0, 0.75, 0, 0, 0, 0));
                            quads.add(createQuad(1, 1, 1, 1, 0.75, 0, 0, 0.75, 0, 0, 1, 1)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            quads.add(createQuad(1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1)); // Back
                            return quads;
                        }
                    }
                }
                case EAST:
                    switch (state.getValue(BlockProps.SLOPE_NUMBER)) {
                        case 0: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(0, 0, 0, 0, 0, 0, 1, 0.25, 0, 1, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 0.25, 1, 0, 0, 1, 0, 0, 1));
                            quads.add(createQuad(0, 0, 0, 0, 0, 1, 1, 0.25, 1, 1, 0.25, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(0, 0, 0, 0, 0.25, 0, 1, 0.5, 0, 1, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 0.5, 1, 0, 0.25, 1, 0, 0, 1));
                            quads.add(createQuad(0, 0.25, 0, 0, 0.25, 1, 1, 0.5, 1, 1, 0.5, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(0, 0, 0, 0, 0.5, 0, 1, 0.75, 0, 1, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 0.75, 1, 0, 0.5, 1, 0, 0, 1));
                            quads.add(createQuad(0, 0.5, 0, 0, 0.5, 1, 1, 0.75, 1, 1, 0.75, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(0, 0, 0, 0, 0.75, 0, 1, 1, 0, 1, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 1, 1, 0, 0.75, 1, 0, 0, 1));
                            quads.add(createQuad(1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1)); // Back
                            quads.add(createQuad(0, 0.75, 0, 0, 0.75, 1, 1, 1, 1, 1, 1, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                    }
                case WEST: {
                    switch (state.getValue(BlockProps.SLOPE_NUMBER)) {
                        case 0: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createTriangle(0, 0, 0, 0, 0.25, 0, 1, 0, 0));
                            quads.add(createTriangle(1, 0, 1, 0, 0.25, 1, 0, 0, 1));
                            quads.add(createQuad(0, 0.25, 0, 0, 0.25, 1, 1, 0, 1, 1, 0, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(0, 0, 0, 0, 0.5, 0, 1, 0.25, 0, 1, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 0.25, 1, 0, 0.5, 1, 0, 0, 1));
                            quads.add(createQuad(0, 0.5, 0, 0, 0.5, 1, 1, 0.25, 1, 1, 0.25, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(0, 0, 0, 0, 0.75, 0, 1, 0.5, 0, 1, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 0.5, 1, 0, 0.75, 1, 0, 0, 1));
                            quads.add(createQuad(0, 0.75, 0, 0, 0.75, 1, 1, 0.5, 1, 1, 0.5, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(0, 0, 0, 0, 1, 0, 1, 0.75, 0, 1, 0, 0));
                            quads.add(createQuad(1, 0, 1, 1, 0.75, 1, 0, 1, 1, 0, 0, 1));
                            quads.add(createQuad(0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0));
                            quads.add(createQuad(0, 1, 0, 0, 1, 1, 1, 0.75, 1, 1, 0.75, 0)); // Top
                            quads.add(createQuad(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                    }
                }
                default:
                    break;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.sprite;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    private BakedQuad createTriangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3){
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        Vec3d normal = new Vec3d(0, 0, 0);
        putVertex(builder, normal, x1, y1, z1, 16, 16);
        putVertex(builder, normal, x2, y2, z2, 16, 0);
        putVertex(builder, normal, x3, y3, z3, 0, 16);
        putVertex(builder, normal, x3, y3, z3, 0, 0);
        return builder.build();
    }

    private BakedQuad createQuad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4){
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        Vec3d normal = new Vec3d(0, 0, 0);
        putVertex(builder, normal, x1, y1, z1, 0, 0);
        putVertex(builder, normal, x2, y2, z2, 0, 16);
        putVertex(builder, normal, x3, y3, z3, 16, 16);
        putVertex(builder, normal, x4, y4, z4, 16, 0);
        return builder.build();
    }

    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
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