package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityMultiSeatsBase;
import de.canitzp.carz.api.EntityPartedBase;
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
public class EntityDeliveryVan extends EntityMultiSeatsBase implements IWheelClampable {
    private static EntityPartedBase.PartData partData;
    private boolean isClamped;

    static {
        EntityPartedBase.PartBuilder builder = builder();
        builder.addInteractOnlyPart(0.4f, 0.3f, 0.6f, 0.7f, 1f);
        builder.addInteractOnlyPart(-0.4f, 0.3f, 0.6f, 0.7f, 1f);

        //Floor
        builder.addCollidingPart(0.35f, 0.3f, 1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, 1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, 0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, 0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, -0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, -0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, -1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, -1.05f, 0.7f, 0.4f);

        //Motor block
        builder.addCollidingPart(-0.65f, 0.3f, 1.55f, 0.4f, 1f);
        builder.addCollidingPart(0, 0.3f, 1.55f, 0.4f, 1f);
        builder.addCollidingPart(+0.65f, 0.3f, 1.55f, 0.4f, 1f);

        //Seperator
        builder.addCollidingPart(-0.5f, 0.5f, 0.25f, 0.1f, 1.6f);
        builder.addCollidingPart(0f, 0.5f, 0.25f, 0.1f, 1.6f);
        builder.addCollidingPart(+0.5f, 0.5f, 0.25f, 0.1f, 1.6f);

        //Walls
        for (float z = -0.14f; z > -1.9; z -= 0.4) {
            builder.addPart(-0.94f, 0.5f, z, 0.1f, 2.3f);
            builder.addPart(+0.94f, 0.5f, z, 0.1f, 2.3f);
        }


        partData = builder.build();
    }

    public EntityDeliveryVan(World world) {
        super(world, 2);
//        this.setSize(4.0F, 3.0F);
        this.setSize(0.2f, 0.2f);

        this.setDriverSeat(0.5, 0.3D, -0.4);
        this.addSeat(0.5, 0.3D, 0.4);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
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
    protected EntityInvisibleCarPart[] constructPartArray() {
        return partData.spawnInvisibleParts(this);
    }

    @Override
    protected int[] constructCollidingPartIndizes() {
        return partData.getCollidingPartIndizes();
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
