package de.canitzp.carz.tile;

import de.canitzp.carz.api.CarzAPI;
import de.canitzp.carz.recipes.FactoryPlantFermenter;
import de.canitzp.carz.recipes.RecipePlantFermenter;
import de.canitzp.carz.util.StackUtil;
import de.canitzp.carz.util.TileUtil;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TilePlantFermenter extends TileEntity implements ITickable{

    @Nonnull
    public InvWrapper inventory = new InvWrapper(new InventoryBasic("Plant Fermenter", false, 6));
    @Nonnull
    public FluidTank tank = new FluidTank(10000);
    public int ticksLeft, maxTicks;
    private ItemStack outputStack = ItemStack.EMPTY;
    private FluidStack outputFluid;

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
        } else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.ticksLeft = compound.getInteger("TicksLeft");
        this.maxTicks = compound.getInteger("MaxTicks");
        if(compound.hasKey("FluidTank", Constants.NBT.TAG_COMPOUND)){
            this.tank = this.tank.readFromNBT(compound.getCompoundTag("FluidTank"));
        }
        if(compound.hasKey("OutputStack", Constants.NBT.TAG_COMPOUND)){
            this.outputStack = new ItemStack(compound.getCompoundTag("OutputStack"));
        }
        if(compound.hasKey("OutputFluid", Constants.NBT.TAG_COMPOUND)){
            this.outputFluid = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("OutputFluid"));
        }
        if(compound.hasKey("Inventory", Constants.NBT.TAG_LIST)){
            if(this.inventory.getSlots() > 0){
                NBTTagList tagList = compound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
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
            compound.setTag("Inventory", tagList);
        }
        compound.setTag("FluidTank", this.tank.writeToNBT(new NBTTagCompound()));
        compound.setInteger("TicksLeft", this.ticksLeft);
        compound.setInteger("MaxTicks", this.maxTicks);
        if(!this.outputStack.isEmpty()){
            compound.setTag("OutputStack", this.outputStack.writeToNBT(new NBTTagCompound()));
        }
        if(this.outputFluid != null){
            compound.setTag("OutputFluid", this.outputFluid.writeToNBT(new NBTTagCompound()));
        }
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound synctag = super.getUpdateTag();
        synctag.setInteger("TicksLeft", this.ticksLeft);
        synctag.setInteger("MaxTicks", this.maxTicks);
        this.tank.writeToNBT(synctag);
        if(!this.outputStack.isEmpty()){
            synctag.setTag("OutputStack", this.outputStack.writeToNBT(new NBTTagCompound()));
        }
        if(this.outputFluid != null){
            synctag.setTag("OutputFluid", this.outputFluid.writeToNBT(new NBTTagCompound()));
        }
        return new SPacketUpdateTileEntity(this.pos, 0, synctag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        this.ticksLeft = tag.getInteger("TicksLeft");
        this.maxTicks = tag.getInteger("MaxTicks");
        this.tank.readFromNBT(tag);
        if(tag.hasKey("OutputStack", Constants.NBT.TAG_COMPOUND)){
            this.outputStack = new ItemStack(tag.getCompoundTag("OutputStack"));
        }
        if(tag.hasKey("OutputFluid", Constants.NBT.TAG_COMPOUND)){
            this.outputFluid = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("OutputFluid"));
        }
    }

    @Override
    public void update() {
        if(!this.world.isRemote){
            if(ticksLeft > 0){
                this.ticksLeft--;
                if(this.world.getTotalWorldTime() % 5 == 0){
                    TileUtil.sync(this);
                }
            }
            if(ticksLeft <= 0){
                if(!this.outputStack.isEmpty() || this.outputFluid != null){
                    if(!this.outputStack.isEmpty()){
                        this.inventory.getStackInSlot(4).grow(this.outputStack.getCount());
                        this.outputStack = ItemStack.EMPTY;
                    }
                    if(this.outputFluid != null){
                        this.tank.fill(this.outputFluid, true);
                        this.outputFluid = null;
                    }
                    TileUtil.sync(this);
                } else {
                    for(int i = 0; i <= 3; i++){
                        ItemStack stackInInputSlot = this.inventory.getStackInSlot(i);
                        if(!stackInInputSlot.isEmpty()){
                            RecipePlantFermenter recipe = canProduce(stackInInputSlot);
                            if(recipe != null){
                                this.ticksLeft = this.maxTicks = recipe.getProduceTicks();
                                stackInInputSlot.shrink(recipe.getInput().getCount());
                                if(stackInInputSlot.isEmpty()){
                                    this.inventory.setStackInSlot(i, ItemStack.EMPTY);
                                }
                                this.outputStack = recipe.getOutput().copy();
                                if(recipe.getOutputFluid() != null){
                                    this.outputFluid = recipe.getOutputFluid().copy();
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // Fill FluidHandler
            if(this.tank.getFluidAmount() > 0){
                ItemStack bucket = this.inventory.getStackInSlot(5);
                if(!bucket.isEmpty() && bucket.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)){
                    IFluidHandlerItem handler = bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                    if(handler != null){
                        handler.fill(this.tank.drain(this.tank.getFluidAmount(), true), true);
                        this.inventory.setStackInSlot(5, bucket);
                        TileUtil.sync(this);
                    }
                }
            }
        }
    }

    @Nullable // Is null if it can't produce
    public RecipePlantFermenter canProduce(ItemStack input){
        if(!input.isEmpty() && CarzAPI.isStackValidPlant(input)){
            RecipePlantFermenter recipe = FactoryPlantFermenter.getRecipeOrDefault(input);
            ItemStack currentlyInsideOutSlot = this.inventory.getStackInSlot(4);
            if(currentlyInsideOutSlot.isEmpty() || StackUtil.canMerge(recipe.getOutput(), currentlyInsideOutSlot)){
                if(recipe.getOutputFluid() != null){
                    if(this.tank.fill(recipe.getOutputFluid(), false) == 0){
                        return null;
                    }
                }
                return recipe;
            }
        }
        return null;
    }
}
