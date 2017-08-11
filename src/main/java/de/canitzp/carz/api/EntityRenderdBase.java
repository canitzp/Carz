package de.canitzp.carz.api;

import de.canitzp.carz.client.models.ModelCar;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by MisterErwin on 11.08.2017.
 * In case you need it, ask me ;)
 */
public abstract class EntityRenderdBase extends Entity {
    public EntityRenderdBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            IFluidHandler fluidHandler = this.getFluidHandler(facing);
            if(fluidHandler != null){
                return (T) fluidHandler;
            }
        }
        if(capability == CapabilityEnergy.ENERGY){
            IEnergyStorage energyStorage = this.getEnergyStorage(facing);
            if(energyStorage != null){
                return (T) energyStorage;
            }
        }
        return super.getCapability(capability, facing);
    }

    @SideOnly(Side.CLIENT)
    public abstract ModelCar getCarModel();

    @SideOnly(Side.CLIENT)
    @Nullable
    public abstract ResourceLocation getCarTexture();

    @SideOnly(Side.CLIENT)
    public abstract void setupGL(double x, double y, double z, float entityYaw, float partialTicks);

    @Nullable
    protected IFluidHandler getFluidHandler(@Nullable EnumFacing facing){
        return null;
    }

    @Nullable
    protected IEnergyStorage getEnergyStorage(@Nullable EnumFacing facing){
        return null;
    }
}
