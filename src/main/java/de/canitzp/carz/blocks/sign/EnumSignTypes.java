package de.canitzp.carz.blocks.sign;

import de.canitzp.carz.blocks.BlockRoadSign;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.models.signs.ModelRoadSign;
import de.canitzp.carz.client.models.signs.ModelRoadSignTriangle;
import de.canitzp.carz.client.renderer.RenderRoadSign;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public enum EnumSignTypes {

    TRIANGLE(new ModelRoadSignTriangle(), MapColor.RED, BlockRoadSign.SIGN_DEFAULT_BOTTOM, BlockRoadSign.SIGN_DEFAULT_TOP){
        @SideOnly(Side.CLIENT)
        @Override
        public void render(RenderRoadSign renderer, TileSign tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
            PixelMesh upper = tile.getUpperMesh();
            if(upper != null){
                float f = 1/20F;
                GlStateManager.pushMatrix();
                GlStateManager.translate(-(f*8) + (upper.getOffsetX() * f), -1.8F + (upper.getOffsetY() * f), -0.14F);
                GlStateManager.scale(f, f, f);
                upper.render(0, 0);
                GlStateManager.popMatrix();
            }
            PixelMesh lower = tile.getLowerMesh();
            if(lower != null){
                float f = 1/9.815F;
                GlStateManager.pushMatrix();
                GlStateManager.translate(-(f*8) + (lower.getOffsetX() * f), -0.55F + (lower.getOffsetY() * f), -0.14F);
                GlStateManager.scale(f, f, f);
                lower.render(0, 0);
                GlStateManager.popMatrix();
            }
        }
    };

    private ModelRoadSign model;
    private MapColor color;
    private AxisAlignedBB bottomBB, topBB;

    EnumSignTypes(ModelRoadSign model, MapColor color, AxisAlignedBB bottomBB, AxisAlignedBB topBB) {
        this.model = model;
        this.color = color;
        this.bottomBB = bottomBB;
        this.topBB = topBB;
    }

    public ModelRoadSign getModel() {
        return model;
    }

    public MapColor getColor() {
        return color;
    }

    public AxisAlignedBB getBottomBoundingBox() {
        return bottomBB;
    }

    public AxisAlignedBB getTopBoundingBox() {
        return topBB;
    }

    public List<AxisAlignedBB> getBottomHitBoxes() {
        return Collections.singletonList(this.getBottomBoundingBox());
    }

    public List<AxisAlignedBB> getTopHitBoxes() {
        return Collections.singletonList(this.getTopBoundingBox());
    }

    @SideOnly(Side.CLIENT)
    public void render(RenderRoadSign renderer, TileSign tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha){ }

}
