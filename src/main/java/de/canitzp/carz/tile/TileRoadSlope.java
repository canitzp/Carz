package de.canitzp.carz.tile;

import de.canitzp.carz.client.renderer.RenderRoad;
import de.canitzp.carz.util.BlockProps;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class TileRoadSlope extends TileRoad {

    @Override
    protected void preRender(RenderRoad renderRoad, double x, double y, double z, float partialTicks, int destroyStage, float alpha, float scaleFactor) {
        int slopeNumber = this.getWorld().getBlockState(this.getPos()).getValue(BlockProps.SLOPE_NUMBER);
        GlStateManager.translate(0.0D, -1.0D + (slopeNumber+1)*0.25, 0.0);
        super.preRender(renderRoad, x, y, z, partialTicks, destroyStage, alpha, scaleFactor);
        EnumFacing blockFacing = this.getWorld().getBlockState(this.getPos()).getValue(BlockProps.FACING);
        switch (this.getMeshFacing()){
            case NORTH:{
                switch (blockFacing){
                    case NORTH:{
                        GlStateManager.rotate(14, 1, 0, 0);
                        break;
                    }
                    case SOUTH:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(-14, 1, 0, 0);
                        break;
                    }
                    case EAST:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(14, 0, 0, 1);
                        break;
                    }
                    case WEST:{
                        GlStateManager.rotate(-14, 0, 0, 1);
                        break;
                    }
                }
                break;
            }
            case SOUTH:{
                switch (blockFacing){
                    case NORTH:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(-14, 1, 0, 0);
                        break;
                    }
                    case SOUTH:{
                        GlStateManager.rotate(14, 1, 0, 0);
                        break;
                    }
                    case EAST:{
                        GlStateManager.rotate(-14, 0, 0, 1);
                        break;
                    }
                    case WEST:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(14, 0, 0, 1);
                        break;
                    }
                }
                break;
            }
            case EAST:{
                switch (blockFacing){
                    case NORTH:{
                        GlStateManager.rotate(-14, 0, 0, 1);
                        break;
                    }
                    case SOUTH:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(14, 0, 0, 1);
                        break;
                    }
                    case EAST:{
                        GlStateManager.rotate(14, 1, 0, 0);
                        break;
                    }
                    case WEST:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(-14, 1, 0, 0);
                        break;
                    }
                }
                break;
            }
            case WEST:{
                switch (blockFacing){
                    case NORTH:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(14, 0, 0, 1);
                        break;
                    }
                    case SOUTH:{
                        GlStateManager.rotate(-14, 0, 0, 1);
                        break;
                    }
                    case EAST:{
                        GlStateManager.translate(0.0F, -0.25, 0.0F);
                        GlStateManager.rotate(-14, 1, 0, 0);
                        break;
                    }
                    case WEST:{
                        GlStateManager.rotate(14, 1, 0, 0);
                        break;
                    }
                }
                break;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void renderMesh(RenderRoad renderRoad, double x, double y, double z, float partialTicks, int destroyStage, float alpha, float scaleFactor) {
        GlStateManager.scale(1.0F, 1.0F, 1.035F);
        super.renderMesh(renderRoad, x, y, z, partialTicks, destroyStage, alpha, scaleFactor);
    }

}
