package de.canitzp.carz.tile;

import com.google.common.collect.ImmutableSortedMap;
import de.canitzp.carz.blocks.BlockRoadSign;
import de.canitzp.carz.blocks.EnumSigns;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

/**
 * @author canitzp
 */
public class TileSign extends TileEntity {

    private EnumSigns signType;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("SignValue", Constants.NBT.TAG_INT)) {
            this.signType = EnumSigns.values()[compound.getInteger("SignValue")];
        }
        System.out.println(this.signType);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (this.signType != null) {
            compound.setInteger("SignValue", this.signType.ordinal());
        }
        return super.writeToNBT(compound);
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
