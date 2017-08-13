package de.canitzp.carz.entity;

import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntitySteerableBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntityBus extends EntitySteerableBase {

    public EntityBus(World worldIn) {
        super(worldIn);
    }

    @Override
    public ModelBase getCarModel() {
        return Registry.MODEL_BUS;
    }

    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return null;
    }

    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {

    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
