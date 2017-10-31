package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.inventory.ContainerCar;
import de.canitzp.carz.items.ItemWheelClamp;
import de.canitzp.carz.network.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class provides all function for any world interaction
 * of any vehicles with the world and interfaces.
 *
 * @author canitzp
 */
public abstract class EntityWorldInteractionBase extends EntityRenderedBase {

    protected int currentInventoryPage = 0;

    public EntityWorldInteractionBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        ItemStack handItem = player.getHeldItem(hand);
        if (handItem.getItem() == Registry.itemWheelClamp && ItemWheelClamp.doInteract(
                handItem, player, this
        )) {
            return true;
        }

        if (!this.world.isRemote && player.isSneaking()) {
            player.openGui(Carz.carz, GuiHandler.ID_CAR, this.world, this.getEntityId(), 0, 0);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            IFluidHandler fluidHandler = this.getFluidHandler(facing);
            if (fluidHandler != null) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
            }
        }
        if (capability == CapabilityEnergy.ENERGY) {
            IEnergyStorage energyStorage = this.getEnergyStorage(facing);
            if (energyStorage != null) {
                return CapabilityEnergy.ENERGY.cast(energyStorage);
            }
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            IItemHandler itemHandler = this.getInventory(facing);
            if (itemHandler != null) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
            }
        }
        return super.getCapability(capability, facing);
    }

    /**
     * This is for the internal FluidTank of the car. Define here a {@link IFluidHandler} as internal tank,
     * so that the car can interact with other {@link IFluidHandler}
     *
     * @param facing The side from which another {@link IFluidHandler} wants to interact with.
     * @return The corresponding {@link IFluidHandler} for the {@link EnumFacing}
     */
    @Nullable
    public IFluidHandler getFluidHandler(@Nullable EnumFacing facing) {
        return null;
    }

    /**
     * This is for the internal EnergyStorage of the car. Define here a {@link IEnergyStorage} as internal storage,
     * so that the car can interact with other {@link IEnergyStorage}
     *
     * @param facing The side from which another {@link IEnergyStorage} wants to interact with.
     * @return The corresponding {@link IEnergyStorage} for the {@link EnumFacing}
     */
    @Nullable
    public IEnergyStorage getEnergyStorage(@Nullable EnumFacing facing) {
        return null;
    }

    /**
     * This is for the inventory of the car. Define here a {@link IItemHandler} as inventory,
     * so that the car can interact with other {@link IItemHandler}
     *
     * @param facing The side from which another {@link IItemHandler} wants to interact with.
     * @return The corresponding {@link IItemHandler} for the {@link EnumFacing}
     */
    @Nullable
    public IItemHandler getInventory(@Nullable EnumFacing facing) {
        return null;
    }

    /**
     * This is for defining an index offset for the car inventories.
     * Per example this is used for paged inventories.
     *
     * @param container The current Container of the car.
     * @param inventory The current inventory. Same as {@link #getInventory(EnumFacing)}
     * @param row       The row of the slot
     * @param column    The column of the slot
     * @return A new index
     */
    public int getInventoryIndexOffset(@Nonnull ContainerCar container, @Nonnull IItemHandler inventory, int row, int column) {
        return (inventory.getSlots() - 1) * this.currentInventoryPage;
    }

    /**
     * This is for setting a new inventory page.
     * You should be check if the inventory can be paged up for yourself!
     *
     * @param newIndex The new page index.
     */
    public void setInventoryPageIndex(int newIndex) {
        this.currentInventoryPage = newIndex;
    }

}
