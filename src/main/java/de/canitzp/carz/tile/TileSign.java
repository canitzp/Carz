package de.canitzp.carz.tile;

import de.canitzp.carz.api.IPaintableTile;
import de.canitzp.carz.blocks.BlockRoadSign;
import de.canitzp.carz.blocks.sign.EnumSignTypes;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import net.minecraft.block.material.MapColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author canitzp
 */
public class TileSign extends TileEntity implements IPaintableTile {

    private EnumSignTypes signType = EnumSignTypes.TRIANGLE;
    private PixelMesh mesh = null;

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
        return this.world.getBlockState(this.pos).getValue(BlockRoadSign.FACING);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.signType = EnumSignTypes.values()[compound.getInteger("SignType")];
        if (compound.hasKey("MeshUUIDMost", Constants.NBT.TAG_LONG) && compound.hasKey("MeshUUIDLeast", Constants.NBT.TAG_LONG)) {
            this.mesh = WorldEvents.getMeshByUUID(compound.getUniqueId("MeshUUID"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("SignType", this.signType.ordinal());
        if (this.mesh != null) {
            compound.setUniqueId("MeshUUID", this.mesh.getId());
        }
        return super.writeToNBT(compound);
    }

    public void setMesh(PixelMesh mesh) {
        this.mesh = mesh;
    }

    public PixelMesh getMesh() {
        return mesh;
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

    @Override
    public void setPixelMesh(@Nonnull PixelMesh mesh) {
        this.mesh = mesh;
    }
}
