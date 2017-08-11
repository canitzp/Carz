package de.canitzp.carz.client.renderer;

import de.canitzp.carz.entity.EntityTestCar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class RenderTestCar extends Render<EntityTestCar> {

    public RenderTestCar(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTestCar entity) {
        //return new ResourceLocation("textures/entity/boat/boat_oak.png");
        return null;
    }

    @Override
    public void doRender(EntityTestCar car, double x, double y, double z, float entityYaw, float partialTicks) {
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
