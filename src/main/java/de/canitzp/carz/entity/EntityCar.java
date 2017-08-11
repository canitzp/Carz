package de.canitzp.carz.entity;

import de.canitzp.carz.api.EntitySteerableBase;
import de.canitzp.carz.client.models.ModelCar;
import de.canitzp.carz.client.renderer.RenderCar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntityCar extends EntitySteerableBase {

    public EntityCar(World worldIn) {
        super(worldIn);
    }

    @Override
    public ModelCar getCarModel() {
        return RenderCar.MODEL_CAR;
    }

    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return null;
    }

    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.translate(x - 1F, y + 1.15F, z + 0.5F);
        GlStateManager.rotate(180.0F, 1.0F, 0, 0);
        GlStateManager.rotate(this.rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public double getCarLength() {
        return 2.0D;
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


//    @Override
//    protected CarPart[] constructArray() {
//        return new CarPart[]{
//                new CarPart(this, "front_left", 0.5f, 1f, 1,0,-.5f),
//                new CarPart(this, "front_right", 0.5f, 2f, 1,0,.5f),
//
//        };
//    }


}