package de.canitzp.carz.client;

import com.google.common.collect.ImmutableSet;
import de.canitzp.carz.Carz;
import de.canitzp.carz.client.models.ModelRoadSlope;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * @author canitzp
 */
public class CustomModelLoader implements ICustomModelLoader{

    private static final IModel RENDERER = new IModel() {
        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            return new ModelRoadSlope(format, bakedTextureGetter);
        }

        @Override
        public Collection<ResourceLocation> getDependencies() {
            return Collections.emptySet();
        }

        @Override
        public Collection<ResourceLocation> getTextures() {
            return ImmutableSet.of(new ResourceLocation(Carz.MODID, "blocks/road"));
        }
    };

    @Override
    public boolean accepts(@Nonnull ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Carz.MODID) && "road_slope_".equals(modelLocation.getResourcePath());
    }

    @Nonnull
    @Override
    public IModel loadModel(@Nonnull ResourceLocation modelLocation) throws Exception {
        return RENDERER;
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {

    }
}
