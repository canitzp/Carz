package de.canitzp.carz.client.renderer;

import de.canitzp.carz.Carz;
import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.api.EntityRenderedBase;
import de.canitzp.carz.api.IColorableCar;
import de.canitzp.carz.util.RenderUtil;
import de.canitzp.carz.api.EntitySteerableBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RenderCar<T extends EntityRenderedBase> extends Render<T> implements IResourceManagerReloadListener {
    private ModelBase model;
    private ResourceLocation texture, overlay;
    private int oldColor = 0;

    public RenderCar(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull T entity) {
        return null;
    }

    @Override
    public void doRender(@Nonnull T car, double x, double y, double z, float entityYaw, float partialTicks) {
        if (this.model == null) {
            this.model = car.getCarModel();
            this.texture = car.getCarTexture();
            if(car instanceof IColorableCar){
                this.overlay = ((IColorableCar) car).getOverlayTexture();
            }
        }
        GlStateManager.pushMatrix();
        car.setupGL(x, y, z, entityYaw, partialTicks);
        if (this.texture != null) {
            if(this.overlay != null){
                try {
                    int color = car instanceof IColorableCar ? ((IColorableCar) car).getCurrentColor() : 0xFFFFFF;
                    boolean calc = false;
                    if(color != this.oldColor){
                        calc = true;
                        this.oldColor = color;
                    }
                    RenderUtil.bindLayeredTexture(this.texture, this.overlay, 0xFFFFFF, color, car instanceof IColorableCar && ((IColorableCar) car).shouldRecalculateTexture() || calc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.bindTexture(this.texture);
            }
        } else {
            GlStateManager.bindTexture(TextureUtil.MISSING_TEXTURE.getGlTextureId());
        }
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(car));
        }
        //GlStateManager.disableAlpha();
        //GlStateManager.enableBlend();
        //GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.model.render(car, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();

        if (Carz.RENDER_DEBUG && car instanceof EntityPartedBase){
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);

            EntityPlayer player = Minecraft.getMinecraft().player;

            double renderPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
            double renderPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
            double renderPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;

            for (AxisAlignedBB bb : ((EntityPartedBase) car).possibleCollisions)
                RenderGlobal.renderFilledBox(bb.grow(0.002D).offset(-renderPosX, -renderPosY, -renderPosZ), 1.0F, 1.0F, 0.0F, 0.2f);

            for (AxisAlignedBB bb : ((EntityPartedBase) car).collisions)
                RenderGlobal.renderFilledBox(bb.grow(0.002D).offset(-renderPosX, -renderPosY, -renderPosZ), 1.0F, 0.2F, 0.2F, 0.2F);

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }

        super.doRender(car, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.model = null;
        this.texture = null;
    }
}
