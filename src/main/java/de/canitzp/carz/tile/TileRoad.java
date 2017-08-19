package de.canitzp.carz.tile;

import de.canitzp.carz.blocks.sign.EnumSignTypes;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.renderer.RenderRoad;
import de.canitzp.carz.events.WorldEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author canitzp
 */
public class TileRoad extends TileEntity{

    private PixelMesh mesh = null;
    private EnumFacing meshFacing = EnumFacing.NORTH;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.meshFacing = EnumFacing.values()[compound.getInteger("MeshFacing")];
        if (compound.hasUniqueId("MeshUUID")) {
            this.mesh = WorldEvents.getMeshByUUID(compound.getUniqueId("MeshUUID"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("MeshFacing", this.meshFacing.ordinal());
        if (this.mesh != null) {
            compound.setUniqueId("MeshUUID", this.mesh.getId());
        }
        return super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    public void setMesh(PixelMesh mesh) {
        this.mesh = mesh;
    }

    public PixelMesh getMesh() {
        return mesh;
    }

    public void setMeshFacing(EnumFacing meshFacing) {
        this.meshFacing = meshFacing;
    }

    public EnumFacing getMeshFacing() {
        return meshFacing;
    }

    @SideOnly(Side.CLIENT)
    public void render(RenderRoad renderRoad, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(mesh != null){
            float f = 1/16F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y + 1.01F, z);

            switch (meshFacing){
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
            GlStateManager.scale(f, f, f);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            mesh.render(0, 0);

            GlStateManager.popMatrix();
        }
    }

}
