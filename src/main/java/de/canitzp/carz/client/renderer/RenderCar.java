package de.canitzp.carz.client.renderer;

import de.canitzp.amcm.AMCMBox;
import de.canitzp.amcm.AMCMModel;
import de.canitzp.amcm.AdvancedMinecraftModel;
import de.canitzp.amcm.IAMCMShapes;
import de.canitzp.carz.Carz;
import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.api.EntityRenderedBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RenderCar<T extends EntityRenderedBase> extends Render<T> implements IResourceManagerReloadListener {
    private ModelBase model;
    private ResourceLocation texture;

    public static AMCMModel testModel;

    static {
        try {
            testModel = AdvancedMinecraftModel.loadModel(new ResourceLocation("carz", "models/amcm/bus.amcm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            this.model = car.getVoxelModel();
            this.texture = car.getTexture();
        }
        GlStateManager.pushMatrix();
        car.setupGL(x, y, z, entityYaw, partialTicks);
        if (this.texture != null) {
            //this.bindTexture(this.texture);
        } else {
            //GlStateManager.bindTexture(TextureUtil.MISSING_TEXTURE.getGlTextureId());
        }
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(car));
        }
        GlStateManager.enableCull();
        this.testModel.render(0.0625F);
        //this.model.render(car, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
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

        //super.doRender(car, x, y, z, entityYaw, partialTicks);

    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.model = null;
        this.texture = null;
    }
}
