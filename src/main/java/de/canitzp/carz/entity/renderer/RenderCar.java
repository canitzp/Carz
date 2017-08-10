package de.canitzp.carz.entity.renderer;

import de.canitzp.carz.entity.EntityCar;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class RenderCar extends Render<EntityCar> {

    public RenderCar(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCar entity) {
        return null;
    }

    @Override
    public void doRender(EntityCar car, double x, double y, double z, float entityYaw, float partialTicks) {
        car.renderCar(x, y, z, entityYaw, partialTicks);
        super.doRender(car, x, y, z, entityYaw, partialTicks);
    }
}
