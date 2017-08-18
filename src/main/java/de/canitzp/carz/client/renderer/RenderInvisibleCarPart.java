package de.canitzp.carz.client.renderer;

import de.canitzp.carz.entity.EntityInvisibleCarPart;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

/**
 * Created by MisterErwin on 14.08.2017.
 * @author MisterErwin
 */
public class RenderInvisibleCarPart extends Render<EntityInvisibleCarPart> {
    public RenderInvisibleCarPart(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityInvisibleCarPart entity) {
        return null;
    }

    public static class RenderInvisibleCarPartFactory implements IRenderFactory<EntityInvisibleCarPart> {
        @Override
        public Render<EntityInvisibleCarPart> createRenderFor(RenderManager manager) {
            return new RenderInvisibleCarPart(manager);
        }
    }

}
