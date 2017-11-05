package de.canitzp.carz.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public abstract class TileRoadBase extends TileBase {

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
}
