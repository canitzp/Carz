package de.canitzp.voxeler;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RenderVoxel<T extends Entity & IVoxelRenderEntity<E>, E extends VoxelBase> extends Render<T> implements IResourceManagerReloadListener {

    private E model;
    private ResourceLocation texture;

    public RenderVoxel(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull T entity) {
        return this.texture;
    }

    @Override
    public final void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (this.model == null) {
            this.model = entity.getVoxelModel();
            //this.texture = entity.getTexture();
            this.texture = this.model.getTexture();
        }
        GlStateManager.pushMatrix();
        this.setup(entity, x, y, z, entityYaw, partialTicks);
        if(this.texture != null){
            this.bindTexture(this.texture);
        } else {
            GlStateManager.bindTexture(TextureUtil.MISSING_TEXTURE.getGlTextureId());
        }
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.renderModel(entity, x, y, z, entityYaw, partialTicks);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
    }

    protected void setup(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks){
        entity.setupGL(x, y, z, entityYaw, partialTicks);
    }

    protected void renderModel(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks){
        this.model.render(entity, partialTicks, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.model = null;
        this.texture = null;
    }
}
