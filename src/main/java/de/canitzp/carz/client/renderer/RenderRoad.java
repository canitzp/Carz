package de.canitzp.carz.client.renderer;

import com.google.common.collect.ImmutableSet;
import de.canitzp.carz.Carz;
import de.canitzp.carz.client.models.ModelRoad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * @author canitzp
 */
public class RenderRoad implements IModel {

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new ModelRoad(state, format, bakedTextureGetter);
    }


    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableSet.of(new ResourceLocation(Carz.MODID, "blocks/roads/default"));
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

}
