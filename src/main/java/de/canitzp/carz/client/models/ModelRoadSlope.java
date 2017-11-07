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

import static de.canitzp.carz.util.QuadUtil.createQuad;
import static de.canitzp.carz.util.QuadUtil.createTriangle;

/**
 * Please don't ask me for help with such shit in other mods,
 * the only thing I do here is changing numbers up to a point where it works
 * like I want it.
 * @author canitzp
 */
public class ModelRoadSlope implements IBakedModel {

    public static final ModelResourceLocation BAKED_MODEL = new ModelResourceLocation(Carz.MODID + ":road_slope_");
    public static final double ANGLE = Math.atan(.25);

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
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 0.25, 0, 1, 0, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 0, 1, 0, 0.25, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 0.25, 0, 0, 0.25, 0, 0, 0, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 0.5, 0, 1, 0.25, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 0.25, 1, 0, 0.5, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0.25, 1, 1, 0.5, 0, 0, 0.5, 0, 0, 0.25, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 0.75, 0, 1, 0.5, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 0.5, 1, 0, 0.75, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0.5, 1, 1, 0.75, 0, 0, 0.75, 0, 0, 0.5, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 1, 0, 1, 0.75, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 0.75, 1, 0, 1, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0.75, 1, 1, 1, 0, 0, 1, 0, 0, 0.75, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0)); // Back
                            return quads;
                        }
                    }
                }
                case SOUTH: {
                    switch (state.getValue(BlockProps.SLOPE_NUMBER)) {
                        case 0: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 0, 0, 1, 0.25, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 0.25, 1, 0, 0, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0.25, 1, 1, 0, 0, 0, 0, 0, 0, 0.25, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 0.25, 0, 1, 0.5, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 0.5, 1, 0, 0.25, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0.5, 1, 1, 0.25, 0, 0, 0.25, 0, 0, 0.5, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 0.5, 0, 1, 0.75, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 0.75, 1, 0, 0.5, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0.75, 1, 1, 0.5, 0, 0, 0.5, 0, 0, 0.75, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 0.75, 0, 1, 1, 1, 1, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 1, 1, 0, 0.75, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 1, 1, 1, 0.75, 0, 0, 0.75, 0, 0, 1, 1)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            quads.add(createQuad(format, sprite, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1)); // Back
                            return quads;
                        }
                    }
                }
                case EAST:
                    switch (state.getValue(BlockProps.SLOPE_NUMBER)) {
                        case 0: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 0, 0, 1, 0.25, 0, 1, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 0.25, 1, 0, 0, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 0, 1, 1, 0.25, 1, 1, 0.25, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 0.25, 0, 1, 0.5, 0, 1, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 0.5, 1, 0, 0.25, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0.25, 0, 0, 0.25, 1, 1, 0.5, 1, 1, 0.5, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 0.5, 0, 1, 0.75, 0, 1, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 0.75, 1, 0, 0.5, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0.5, 0, 0, 0.5, 1, 1, 0.75, 1, 1, 0.75, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 0.75, 0, 1, 1, 0, 1, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 1, 1, 0, 0.75, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1)); // Back
                            quads.add(createQuad(format, sprite, 0, 0.75, 0, 0, 0.75, 1, 1, 1, 1, 1, 1, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                    }
                case WEST: {
                    switch (state.getValue(BlockProps.SLOPE_NUMBER)) {
                        case 0: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createTriangle(format, sprite, 0, 0, 0, 0, 0.25, 0, 1, 0, 0));
                            quads.add(createTriangle(format, sprite, 1, 0, 1, 0, 0.25, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0.25, 0, 0, 0.25, 1, 1, 0, 1, 1, 0, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 1: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 0.5, 0, 1, 0.25, 0, 1, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 0.25, 1, 0, 0.5, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0.5, 0, 0, 0.5, 1, 1, 0.25, 1, 1, 0.25, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 2: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 0.75, 0, 1, 0.5, 0, 1, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 0.5, 1, 0, 0.75, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0.75, 0, 0, 0.75, 1, 1, 0.5, 1, 1, 0.5, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
                            return quads;
                        }
                        case 3: {
                            List<BakedQuad> quads = new ArrayList<>();
                            quads.add(createQuad(format, sprite, 0, 0, 0, 0, 1, 0, 1, 0.75, 0, 1, 0, 0));
                            quads.add(createQuad(format, sprite, 1, 0, 1, 1, 0.75, 1, 0, 1, 1, 0, 0, 1));
                            quads.add(createQuad(format, sprite, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0));
                            quads.add(createQuad(format, sprite, 0, 1, 0, 0, 1, 1, 1, 0.75, 1, 1, 0.75, 0)); // Top
                            quads.add(createQuad(format, sprite, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1)); // Bottom
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
}