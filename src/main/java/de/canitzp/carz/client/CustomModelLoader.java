package de.canitzp.carz.client;

import de.canitzp.carz.Carz;
import de.canitzp.carz.client.renderer.RenderRoad;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

/**
 * @author canitzp
 */
public class CustomModelLoader implements ICustomModelLoader {

    public static final RenderRoad RENDER_ROAD = new RenderRoad();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Carz.MODID) && "baked_road".equals(modelLocation.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return RENDER_ROAD;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
