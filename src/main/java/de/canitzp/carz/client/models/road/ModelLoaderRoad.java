package de.canitzp.carz.client.models.road;

import com.google.common.collect.ImmutableList;
import de.canitzp.carz.Carz;
import de.canitzp.carz.client.models.ModelRoadSlope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Function;

public class ModelLoaderRoad implements ICustomModelLoader {
    private static final String KEY = "models/block/roadmodel/";
    @Override
    public boolean accepts(@Nonnull ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Carz.MODID)&&modelLocation.getResourcePath().startsWith(KEY);
    }

    @Nonnull
    @Override
    public IModel loadModel(@Nonnull ResourceLocation modelLocation) throws Exception {
        return new RawRoadModel(modelLocation.getResourcePath().substring(KEY.length()));
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        BakedRoadModel.MODEL_CACHE.invalidateAll();
    }

    class RawRoadModel implements IModel {
        ResourceLocation base;
        public RawRoadModel(String loc) {
            switch (loc) {
                case "road":
                case "road_boost":
                    base = new ResourceLocation(Carz.MODID, "block/road_default");
                    break;
                case "slope":
                    base = ModelRoadSlope.BAKED_MODEL;
                    break;
                default:
                    base = new ResourceLocation("carz", "missing");
            }
        }
        @Nonnull
        @Override
        public Collection<ResourceLocation> getDependencies() {
            return ImmutableList.of(base);
        }

        @Nonnull
        @Override
        public Collection<ResourceLocation> getTextures() {
            return ImmutableList.of();
        }

        @Nonnull
        @Override
        public IBakedModel bake(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            try {
                return new BakedRoadModel(ModelLoaderRegistry.getModel(base).bake(state, format, bakedTextureGetter), base.toString(), format);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
