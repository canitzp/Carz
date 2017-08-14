package de.canitzp.carz.tile;

import de.canitzp.carz.blocks.EnumSigns;
import de.canitzp.carz.network.MessageNBT;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

/**
 * @author canitzp
 */
public class TileSign extends TileEntity {

    private EnumSigns signType = EnumSigns.WARNING;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("SignValue", Constants.NBT.TAG_INT)) {
            this.signType = EnumSigns.values()[compound.getInteger("SignValue")];
        }
        sendUpdates();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("SignValue", this.signType.ordinal());
        System.out.println(compound);
        return super.writeToNBT(compound);
    }

    public void sendUpdates(){
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("SignValue", this.signType.ordinal());
        NetworkHandler.net.sendToAll(new MessageNBT(nbt, this.getPos()));
    }

    public void handleUpdate(NBTTagCompound nbt){
        this.signType = EnumSigns.values()[nbt.getInteger("SignValue")];
    }

    public void setSignType(EnumSigns signType) {
        this.signType = signType;
    }

    public EnumSigns getSignType() {
        return signType;
    }

    public boolean hasSignType(){
        return getSignType() != null;
    }
}
