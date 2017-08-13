package de.canitzp.carz.tile;

import de.canitzp.carz.blocks.BlockSign;
import de.canitzp.carz.blocks.EnumSigns;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

/**
 * @author canitzp
 */
public class TileSign extends TileEntity {

    private EnumSigns signType;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("SignValue", Constants.NBT.TAG_INT)){
            this.signType = EnumSigns.values()[compound.getInteger("SignValue")];
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if(this.signType != null){
            compound.setInteger("SignValue", this.signType.ordinal());
        } else {
            IBlockState state = this.world.getBlockState(this.pos);
            this.signType = state.getValue(BlockSign.SIGN_TYPE);
            compound.setInteger("SignValue", this.signType.ordinal());
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void onLoad() {
        if(this.signType != null){
            //IBlockState state = this.world.getBlockState(this.pos);
            //this.world.setBlockState(this.pos, state.withProperty(BlockSign.SIGN_TYPE, this.signType));
        }
    }
}
