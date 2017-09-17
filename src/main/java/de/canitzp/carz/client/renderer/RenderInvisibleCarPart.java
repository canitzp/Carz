package de.canitzp.carz.client.renderer;

import de.canitzp.carz.entity.EntityInvisibleCarPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

/**
 * Created by MisterErwin on 14.08.2017.
 *
 * @author MisterErwin
 */
public class RenderInvisibleCarPart extends Render<EntityInvisibleCarPart> {
    private final Minecraft minecraft;

    public RenderInvisibleCarPart(RenderManager renderManager) {
        super(renderManager);
        minecraft = Minecraft.getMinecraft();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityInvisibleCarPart entity) {
        return null;
    }

    @Override
    public void doRender(EntityInvisibleCarPart entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!entity.colliding)
            return;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        EntityPlayer player = minecraft.player;

        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
        double renderPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double renderPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double renderPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

        if (entity.onGround) {
            RenderGlobal.drawSelectionBoundingBox(axisalignedbb.grow(0.002D).offset(-renderPosX, -renderPosY, -renderPosZ), 0.2F, 0.2F, 1.0F, 1.0F);
        } else {
            RenderGlobal.drawSelectionBoundingBox(axisalignedbb.grow(0.002D).offset(-renderPosX, -renderPosY, -renderPosZ), 0.2F, 0.2F, 0.2F, 1.0F);
        }


        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public static class RenderInvisibleCarPartFactory implements IRenderFactory<EntityInvisibleCarPart> {
        @Override
        public Render<EntityInvisibleCarPart> createRenderFor(RenderManager manager) {
            return new RenderInvisibleCarPart(manager);
        }
    }

}
