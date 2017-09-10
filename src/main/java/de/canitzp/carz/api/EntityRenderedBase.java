package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import de.canitzp.carz.network.GuiHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class does provide the main function for a renderable object.
 * Extend this class if you want to make a car object
 * which doesn't move, isn't steerable and you can't sit in it.
 *
 * @author MisterErwin
 * @author canitzp
 * @see EntityMoveableBase
 * @see EntityRideableBase
 * @see EntitySteerableBase
 */
@SuppressWarnings("WeakerAccess")
public abstract class EntityRenderedBase extends Entity {

    public EntityRenderedBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if(!this.world.isRemote && player.isSneaking()){
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
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            IItemHandler itemHandler = this.getInventory(facing);
            if(itemHandler != null){
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
            }
        }
        return super.getCapability(capability, facing);
    }

    /**
     * @return The Model for the Car
     */
    @SideOnly(Side.CLIENT)
    public abstract ModelBase getCarModel();

    /**
     * Here you can define the texture for the car. You can just make a new {@link ResourceLocation},
     * since this variable gets cached by the Renderer, unless you restart the game or reload
     * the resources with F3+T
     *
     * @return The Location to the texture of the Car.
     */
    @SideOnly(Side.CLIENT)
    @Nullable
    public abstract ResourceLocation getCarTexture();

    /**
     * Here you have to setup all the OpenGL Stuff for the car like translation, rotation and scaling.
     *
     * @param x            The x coordinate where the model have to render
     * @param y            The y coordinate where the model have to render
     * @param z            The y coordinate where the model have to render
     * @param entityYaw    The current rotationYaw of the car, use this instead of this.rotationYaw!
     * @param partialTicks The partial ticks for rendering
     * @see net.minecraft.client.renderer.GlStateManager
     */
    @SideOnly(Side.CLIENT)
    public abstract void setupGL(double x, double y, double z, float entityYaw, float partialTicks);

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
    public IItemHandler getInventory(@Nullable EnumFacing facing){
        return null;
    }

}
