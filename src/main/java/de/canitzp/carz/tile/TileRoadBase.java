package de.canitzp.carz.tile;

import de.canitzp.carz.client.renderer.RenderRoad;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public abstract class TileRoadBase extends TileEntity {

    private EnumFacing meshFacing = EnumFacing.NORTH;

    public void setMeshFacing(EnumFacing meshFacing) {
        this.meshFacing = meshFacing;
    }

    public EnumFacing getMeshFacing() {
        return meshFacing;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setMeshFacing(EnumFacing.values()[compound.getInteger("MeshFacing")]);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("MeshFacing", this.getMeshFacing().ordinal());
        return super.writeToNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @SideOnly(Side.CLIENT)
    public abstract void render(RenderRoad renderRoad, double x, double y, double z, float partialTicks, int destroyStage, float alpha);

}
