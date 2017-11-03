package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityAIDriveableBase;
import de.canitzp.carz.api.IWheelClampable;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntitySpeedster extends EntityAIDriveableBase implements IWheelClampable {

    private boolean clamped = false;

    public EntitySpeedster(World world) {
        super(world);
        this.setSize(1.75F, 1.8125F);
        this.someOtherRandomRotModifier = 2.15;
    }

    @Override
    public ModelBase getCarModel() {
        return Registry.MODEL_SPEEDSTER;
    }

    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return new ResourceLocation(Carz.MODID, "textures/cars/speedster.png");
    }

    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.translate(x, y + 3, z);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.rotationPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(2.0, 2.0, 2.0);
    }

    @Override
    public void setClamped(boolean clamped) {
        this.clamped = clamped;
    }

    @Override
    public boolean isClamped() {
        return this.clamped;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.clamped = compound.getBoolean("clamped");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("clamped", this.clamped);
    }
}
