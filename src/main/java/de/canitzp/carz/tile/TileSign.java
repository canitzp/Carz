package de.canitzp.carz.tile;

import de.canitzp.carz.blocks.BlockRoadSign;
import de.canitzp.carz.blocks.sign.EnumSignTypes;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.MapColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/**
 * @author canitzp
 */
public class TileSign extends TileEntity {

    private EnumSignTypes signType = EnumSignTypes.TRIANGLE;
    private PixelMesh upperMesh = null, lowerMesh = null;
    public static final AxisAlignedBB RENDER_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0F, 2.0D, 2.0F, 2.0F);

    public MapColor getMapColor() {
        return this.signType.getColor();
    }

    public AxisAlignedBB getBoundingBox(boolean bottom) {
        return bottom ? this.signType.getBottomBoundingBox() : this.signType.getTopBoundingBox();
    }

    public List<AxisAlignedBB> getHitBoxes(boolean bottom) {
        return bottom ? this.signType.getBottomHitBoxes() : this.signType.getTopHitBoxes();
    }

    public EnumFacing getFacing(){
        return this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.signType = EnumSignTypes.values()[compound.getInteger("SignType")];
        if (compound.hasUniqueId("MeshUpperUUID")) {
            this.upperMesh = WorldEvents.getMeshByUUID(compound.getUniqueId("MeshUpperUUID"));
        }
        if (compound.hasUniqueId("MeshLowerUUID")) {
            this.lowerMesh = WorldEvents.getMeshByUUID(compound.getUniqueId("MeshLowerUUID"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("SignType", this.signType.ordinal());
        if (this.upperMesh != null) {
            compound.setUniqueId("MeshUpperUUID", this.upperMesh.getId());
        }
        if (this.lowerMesh != null) {
            compound.setUniqueId("MeshLowerUUID", this.lowerMesh.getId());
        }
        return super.writeToNBT(compound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return RENDER_BOX.offset(this.pos);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    public void setUpperMesh(PixelMesh upperMesh) {
        this.upperMesh = upperMesh;
    }

    public PixelMesh getUpperMesh() {
        return upperMesh;
    }

    public void setLowerMesh(PixelMesh lowerMesh) {
        this.lowerMesh = lowerMesh;
    }

    public PixelMesh getLowerMesh() {
        return lowerMesh;
    }

    public void setSignType(EnumSignTypes signType) {
        this.signType = signType;
    }

    public EnumSignTypes getSignType() {
        return signType;
    }

    public boolean hasSignType() {
        return getSignType() != null;
    }

    public void clickedWithPainter(PixelMesh mesh, boolean bottom){
        if(bottom){
            this.lowerMesh = mesh;
        } else {
            this.upperMesh = mesh;
        }
    }
}
