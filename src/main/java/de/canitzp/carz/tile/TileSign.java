package de.canitzp.carz.tile;

import de.canitzp.carz.blocks.EnumSigns;
import de.canitzp.carz.blocks.sign.EnumSignTypes;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.block.material.MapColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * @author canitzp
 */
public class TileSign extends TileEntity {

    private EnumSignTypes signType = EnumSignTypes.TRIANGLE;

    public MapColor getMapColor(){
        return this.signType.getColor();
    }

    public AxisAlignedBB getBoundingBox(boolean bottom){
        return bottom ? this.signType.getBottomBoundingBox() : this.signType.getTopBoundingBox();
    }

    public List<AxisAlignedBB> getHitBoxes(boolean bottom){
        return bottom ? this.signType.getBottomHitBoxes() : this.signType.getTopHitBoxes();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("SignType", Constants.NBT.TAG_INT)) {
            this.signType = EnumSignTypes.values()[compound.getInteger("SignType")];
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("SignType", this.signType.ordinal());
        return super.writeToNBT(compound);
    }

    public void setSignType(EnumSignTypes signType) {
        this.signType = signType;
    }

    public EnumSignTypes getSignType() {
        return signType;
    }

    public boolean hasSignType(){
        return getSignType() != null;
    }
}
