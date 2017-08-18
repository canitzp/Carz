package de.canitzp.carz.client.renderer;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.models.signs.ModelRoadSign;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

/**
 * @author canitzp
 */
public class RenderRoadSign extends TileEntitySpecialRenderer<TileSign> {

    @Override
    public void render(TileSign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        //GlStateManager.enableBlend();
        //GlStateManager.disableAlpha();
        //GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        //GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.translate(x + 0.5F, y + 0.75F, z + 0.5F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        switch (te.getFacing()){
            case NORTH:{
                break;
            }
            case SOUTH:{
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                break;
            }
            case EAST:{
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                break;
            }
            case WEST:{
                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                break;
            }
            default: break;
        }

        ModelRoadSign model = te.getSignType().getModel();
        Minecraft.getMinecraft().getTextureManager().bindTexture(model.getTexture());
        model.render(1 / 16F);

        PixelMesh mesh = te.getMesh();
        if(mesh != null){
            float f = 1/20F;
            GlStateManager.translate(-(f*8), -1.8F, -0.14F);
            GlStateManager.scale(f, f, f);
            mesh.render(0, 0);
        }

        //GlStateManager.shadeModel(GL11.GL_FLAT);
        //GlStateManager.disableBlend();
        //GlStateManager.enableAlpha();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }
}
