package de.canitzp.carz.tile;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.renderer.RenderRoad;
import de.canitzp.carz.events.WorldEvents;
import de.canitzp.carz.util.BlockProps;
import de.canitzp.carz.util.TileUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class TileRoad extends TileRoadBase{

    private PixelMesh mesh = null;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasUniqueId("MeshUUID")) {
            this.mesh = WorldEvents.getMeshByUUID(compound.getUniqueId("MeshUUID"));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (this.mesh != null) {
            compound.setUniqueId("MeshUUID", this.mesh.getId());
        }
        return super.writeToNBT(compound);
    }

    public void setMesh(PixelMesh mesh) {
        this.mesh = mesh;
        TileUtil.sync(this);
    }

    public PixelMesh getMesh() {
        return mesh;
    }

    @SideOnly(Side.CLIENT)
    public void render(RenderRoad renderRoad, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(this.getMesh() != null){
            float f = 1/16F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y + 1.01F, z);
            this.preRender(renderRoad, x, y, z, partialTicks, destroyStage, alpha, f);
            this.renderMesh(renderRoad, x, y, z, partialTicks, destroyStage, alpha, f);
            GlStateManager.popMatrix();
        }
    }

    @SideOnly(Side.CLIENT)
    protected void preRender(RenderRoad renderRoad, double x, double y, double z, float partialTicks, int destroyStage, float alpha, float scaleFactor){
        switch (this.getMeshFacing()){
            case NORTH:{
                break;
            }
            case SOUTH:{
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-1.0F, 0.0F, -1.0F);
                break;
            }
            case EAST:{
                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, -1.0F);
                break;
            }
            case WEST:{
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-1.0F, 0.0F, 0.0F);
                break;
            }
            default: break;
        }
    }

    @SideOnly(Side.CLIENT)
    protected void renderMesh(RenderRoad renderRoad, double x, double y, double z, float partialTicks, int destroyStage, float alpha, float scaleFactor){
        GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        this.getMesh().render(0, 0);
    }

}
