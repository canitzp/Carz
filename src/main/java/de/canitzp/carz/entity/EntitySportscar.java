package de.canitzp.carz.entity;

import de.canitzp.carz.api.EntitySteerableBase;
import de.canitzp.carz.client.models.ModelCar;
import de.canitzp.carz.client.renderer.RenderCar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntitySportscar extends EntitySteerableBase {

    public EntitySportscar(World worldIn) {
        super(worldIn);
        this.setSize(1.5F, 0.65F);
    }

    @Override
    public ModelCar getCarModel() {
        return RenderCar.MODEL_CAR; // TODO
    }

    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return null; // TODO
    }

    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.translate(x, y, z);
        // TODO
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
