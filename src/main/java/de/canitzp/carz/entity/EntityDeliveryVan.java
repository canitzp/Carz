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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntityDeliveryVan extends EntityAIDriveableBase implements IWheelClampable {

    private boolean isClamped;

    public EntityDeliveryVan(World world) {
        super(world);
        this.setSize(4.0F, 3.0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBase getCarModel() {
        return Registry.MODEL_DELIVERY_VAN;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return new ResourceLocation(Carz.MODID, "textures/cars/delivery_van.png");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.translate(x, y + 1.5, z);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.rotationPitch, 1.0F, 0.0F, 0.0F);
    }

    @Override
    public void setClamped(boolean clamped) {
        this.isClamped = clamped;
    }

    @Override
    public boolean isClamped() {
        return this.isClamped;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.isClamped = compound.getBoolean("clamped");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("clamped", this.isClamped);
    }
}
