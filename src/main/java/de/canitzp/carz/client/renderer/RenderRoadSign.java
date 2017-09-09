package de.canitzp.carz.client.renderer;

import de.canitzp.carz.blocks.sign.EnumSignTypes;
import de.canitzp.carz.client.models.signs.ModelRoadSign;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RenderRoadSign extends TileEntitySpecialRenderer<TileSign> {

    @Override
    public void render(TileSign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.setLightmapDisabled(false);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5F, y + 0.75F, z + 0.5F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float yRot = 0.0F;
        switch (te.getFacing()){
            case NORTH:{
                break;
            }
            case SOUTH:{
                yRot = 180.0F;
                break;
            }
            case EAST:{
                yRot = 90.0F;
                break;
            }
            case WEST:{
                yRot = -90.0F;
                break;
            }
            default: break;
        }
        GlStateManager.rotate(yRot, 0.0F, 1.0F, 0.0F);

        EnumSignTypes signType = te.getSignType();
        ModelRoadSign model = signType.getModel();
        this.bindTexture(model.getTexture());
        model.render(te, 1 / 16F);

        this.setLightmapDisabled(true);
        signType.render(this, te, x, y, z, partialTicks, destroyStage, alpha);

        //RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }

}
