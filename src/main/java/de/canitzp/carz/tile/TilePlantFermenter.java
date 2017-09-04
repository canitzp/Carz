package de.canitzp.carz.tile;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TilePlantFermenter extends TileEntity {

    @Nonnull
    public InvWrapper inventory = new InvWrapper(new InventoryBasic("Plant Fermenter", false, 5));
    @Nonnull
    public FluidTank tank = new FluidTank(10000);

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return (T) this.inventory;
        } else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return (T) this.tank;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("FluidTank", Constants.NBT.TAG_COMPOUND)){
            this.tank = this.tank.readFromNBT(compound.getCompoundTag("FluidTank"));
        }
        if(compound.hasKey("Inventory", Constants.NBT.TAG_COMPOUND)){
            if(this.inventory.getSlots() > 0){
                NBTTagList tagList = compound.getTagList("Items", 10);
                for(int i = 0; i < this.inventory.getSlots(); i++){
                    NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                    this.inventory.setStackInSlot(i, tagCompound.hasKey("id") ? new ItemStack(tagCompound) : ItemStack.EMPTY);
                }
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound invNBT = new NBTTagCompound();
        if(this.inventory.getSlots() > 0){
            NBTTagList tagList = new NBTTagList();
            for(int i = 0; i < this.inventory.getSlots(); i++){
                ItemStack slot = this.inventory.getStackInSlot(i);
                NBTTagCompound tagCompound = new NBTTagCompound();
                if(!slot.isEmpty()){
                    slot.writeToNBT(tagCompound);
                }
                tagList.appendTag(tagCompound);
            }
            invNBT.setTag("Items", tagList);
        }
        compound.setTag("Inventory", invNBT);
        compound.setTag("FluidTank", this.tank.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }
}
