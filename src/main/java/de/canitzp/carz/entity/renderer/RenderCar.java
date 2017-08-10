package de.canitzp.carz.entity.renderer;

import de.canitzp.carz.entity.EntityCar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class RenderCar extends Render<EntityCar> {

    public static final ModelCar MODEL_CAR = new ModelCar();

    public RenderCar(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCar entity) {
        //return new ResourceLocation("textures/entity/boat/boat_oak.png");
        return null;
    }

    @Override
    public void doRender(EntityCar car, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        //this.setupRotation(entity, entityYaw, partialTicks);
        this.bindEntityTexture(car);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(car));
        }

        car.renderCar(x, y, z, entityYaw, partialTicks);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        super.doRender(car, x, y, z, entityYaw, partialTicks);
    }
}
