package de.canitzp.carz.client.renderer;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.models.signs.ModelRoadSign;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * @author canitzp
 */
public class RenderRoadSign extends TileEntitySpecialRenderer<TileSign> {

    @Override
    public void render(TileSign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5F, y + 0.75F, z + 0.5F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);

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

        GlStateManager.popMatrix();
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }
}
