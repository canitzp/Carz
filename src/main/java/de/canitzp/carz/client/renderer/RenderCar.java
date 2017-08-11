package de.canitzp.carz.client.renderer;

import de.canitzp.carz.api.EntityMoveableBase;
import de.canitzp.carz.client.models.ModelCar;
import de.canitzp.carz.entity.EntityCar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RenderCar<T extends EntityMoveableBase> extends Render<T> implements IResourceManagerReloadListener {

    public static final ModelCar MODEL_CAR = new ModelCar();

    private ModelCar model;
    private ResourceLocation texture;

    public RenderCar(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }

    @Override
    public void doRender(T car, double x, double y, double z, float entityYaw, float partialTicks) {
        if (this.model == null) {
            this.model = car.getCarModel();
            this.texture = car.getCarTexture();
        }
        GlStateManager.pushMatrix();
        car.setupGL(x, y, z, entityYaw, partialTicks);
        if (this.texture != null) {
            this.bindTexture(this.texture);
        }

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(car));
        }

        this.model.render(car, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        super.doRender(car, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.model = null;
        this.texture = null;
    }
}
