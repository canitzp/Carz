package de.canitzp.carz.entity;

import de.canitzp.carz.api.EntitySteerableBase;
import de.canitzp.carz.client.renderer.RenderCar;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntityCar extends EntitySteerableBase {

    public EntityCar(World worldIn) {
        super(worldIn);
        this.setSize(1.5F, 0.65F);
    }

    @Override
    public ModelBase getCarModel() {
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
        GlStateManager.rotate(this.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0, 0);
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