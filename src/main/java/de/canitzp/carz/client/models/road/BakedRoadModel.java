package de.canitzp.carz.client.models.road;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.client.Pixel;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.models.ModelRoadSlope;
import de.canitzp.carz.util.BlockProps;
import de.canitzp.carz.util.QuadUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static de.canitzp.carz.util.BlockProps.*;

public class BakedRoadModel implements IBakedModel {
    public static final Cache<RoadCacheKey, List<BakedQuad>> MODEL_CACHE = CacheBuilder.newBuilder().maximumSize(100)
            .expireAfterAccess(60, TimeUnit.SECONDS).build();

    private final VertexFormat format;
    private final IBakedModel base;
    private final String baseLoc;
    public BakedRoadModel(IBakedModel base, String baseLoc, VertexFormat format) {
        this.base = base;
        this.baseLoc = baseLoc;
        this.format = format;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state instanceof IExtendedBlockState&&side==null) {
            PixelMesh mesh = ((IExtendedBlockState) state).getValue(MODEL);
            EnumFacing facing = ((IExtendedBlockState) state).getValue(FACING_MESH);
            int additional = -1;
            if (state.getBlock()== Registry.blockRoadSlope) {
                additional = state.getValue(FACING).getIndex();
                additional |= state.getValue(SLOPE_NUMBER)<<3;
            }
            RoadCacheKey key = new RoadCacheKey(mesh==null?null:mesh.getId(),
                    facing, baseLoc, additional);
            try {
                return MODEL_CACHE.get(key, ()->{
                    List<BakedQuad> ret = new ArrayList<>();
                    if (mesh!=null) {
                        Matrix4f transform = new Matrix4f();
                        Vector3f tmp = new Vector3f();
                        if (state.getBlock() == Registry.blockRoadSlope) {
                            tmp.set(.5F, .25F * (state.getValue(BlockProps.SLOPE_NUMBER) + 1), .5F);
                            transform.translate(tmp);
                            tmp.set(0, 1, 0);
                            float slopeFacing = state.getValue(BlockProps.FACING).getHorizontalAngle();
                            transform.rotate((float) Math.toRadians(180 - slopeFacing), tmp);
                            tmp.set(-.5F, 0, -.5F);
                            transform.translate(tmp);
                            tmp.set(1, 0, 0);
                            transform.rotate((float) ModelRoadSlope.ANGLE, tmp);
                            float scale = (float) (1 / Math.cos(ModelRoadSlope.ANGLE));
                            tmp.set(1, 1, scale);
                            transform.scale(tmp);
                            tmp.set(.5F, 0, .5F);
                            transform.translate(tmp);
                            tmp.set(0, 1, 0);
                            transform.rotate((float) Math.toRadians(-facing.getHorizontalAngle() + slopeFacing - 90), tmp);
                            tmp.set(-.5F, 0, -.5F);
                            transform.translate(tmp);
                        } else {
                            tmp.set(.5F, 1, .5F);
                            transform.translate(tmp);
                            tmp.set(0, 1, 0);
                            transform.rotate((float) Math.toRadians(-facing.getHorizontalAngle() + 90), tmp);
                            tmp.set(-.5F, 0, -.5F);
                            transform.translate(tmp);
                        }
                        //Prevent Z-fighting by moving the pixels slightly up
                        tmp.x = 0;
                        tmp.y = 0.001F;
                        tmp.z = 0;
                        transform.translate(tmp);
                        Pixel[][] pixels = mesh.getPixels();
                        Vector4f v1 = new Vector4f(0, 0, 0, 1);
                        Vector4f v2 = new Vector4f(0, 0, 0, 1);
                        Vector4f v3 = new Vector4f(0, 0, 0, 1);
                        Vector4f v4 = new Vector4f(0, 0, 0, 1);
                        final float pixelSize = 1F / 16;
                        for (int i = 0; i < pixels.length; i++) {
                            for (int j = 0; j < pixels[i].length; j++) {
                                Pixel p = pixels[i][j];
                                if (p.isValid()) {
                                    v1.set(i * pixelSize, 0, j * pixelSize);
                                    v2.set(i * pixelSize, 0, (j + 1) * pixelSize);
                                    v3.set((i + 1) * pixelSize, 0, (j + 1) * pixelSize);
                                    v4.set((i + 1) * pixelSize, 0, j * pixelSize);
                                    Matrix4f.transform(transform, v1, v1);
                                    Matrix4f.transform(transform, v2, v2);
                                    Matrix4f.transform(transform, v3, v3);
                                    Matrix4f.transform(transform, v4, v4);
                                    ret.add(QuadUtil.createQuad(format,
                                            v1.x, v1.y, v1.z,
                                            v2.x, v2.y, v2.z,
                                            v3.x, v3.y, v3.z,
                                            v4.x, v4.y, v4.z,
                                            p.getR() / 255F, p.getG() / 255F, p.getB() / 255F));
                                }
                            }
                        }
                    }
                    ret.addAll(base.getQuads(state, null, rand));
                    return ImmutableList.copyOf(ret);
                });
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return base.getQuads(state, side, rand);
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
        return base.getParticleTexture();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return base.getOverrides();
    }

    @Nonnull
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return base.getItemCameraTransforms();
    }

    public static class RoadCacheKey {
        @Nullable
        public final UUID mesh;
        @Nullable
        public final EnumFacing dir;
        public final String base;
        // For facing+height of slopes
        final int additional;

        private RoadCacheKey(@Nullable UUID mesh, @Nullable EnumFacing dir, String base, int additional) {
            this.mesh = mesh;
            this.dir = dir;
            this.base = base;
            this.additional = additional;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RoadCacheKey that = (RoadCacheKey) o;

            if (additional != that.additional) return false;
            if (mesh != null ? !mesh.equals(that.mesh) : that.mesh != null) return false;
            if (dir != that.dir) return false;
            return base.equals(that.base);
        }

        @Override
        public int hashCode() {
            int result = mesh != null ? mesh.hashCode() : 0;
            result = 31 * result + (dir != null ? dir.hashCode() : 0);
            result = 31 * result + base.hashCode();
            result = 31 * result + additional;
            return result;
        }
    }
}
