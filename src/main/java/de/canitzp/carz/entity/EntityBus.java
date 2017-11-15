package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityMultiSeatsBase;
import de.canitzp.carz.api.EntityPartedBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp, MisterErwin
 */
public class EntityBus extends EntityMultiSeatsBase {

    private static EntityPartedBase.PartData partData;

    private final static int SEAT_AMOUNT = 7;

    static {
        EntityPartedBase.PartBuilder builder = builder();

        //Driver
        builder.addInteractOnlyPart(.9f, 0.4f, 2.9f, .8f, 1.1f);

        //first row
        builder.addInteractOnlyPart(.9f, 0.4f, 1.4f, .8f, 1.1f);
        builder.addInteractOnlyPart(-.9f, 0.4f, 1.4f, .8f, 1.1f);

        //2nd row
        builder.addInteractOnlyPart(.9f, 0.3f, 0.2f, .8f, 1.1f);
        builder.addInteractOnlyPart(-.9f, 0.3f, 0.2f, .8f, 1.1f);

        builder.addInteractOnlyPart(.9f, 0.3f, -2.7f, .8f, 1.1f);
        builder.addInteractOnlyPart(-.9f, 0.3f, -2.7f, .8f, 1.1f);

        //Under**fucking**ground
        for (int z = -3; z <= 3; ++z)
            for (int x = -1; x <= 1; ++x)
                builder.addFloor(x, 0, z, 1, 0.2f);

        float wh = 1.6f;
        float wo = 0.1f; //wall offset Y
        for (float z = -3; z <= 3; z += 0.5f)
            builder.addPart(1.5f, 0, z, 0.2f, wh);

        //Seitenwand
        builder.addPart(-1.5f, wo, -3.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, -2.5f, 0.2f, wh);
        builder.addPart(-1.5f, wo, -2.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, -1.65f, 0.2f, wh);
        //-1
        //-0.5
        builder.addPart(-1.5f, wo, +0.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +0.5f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +1.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +1.5f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +1.85f, 0.2f, wh);
        //2.5
        builder.addPart(-1.5f, wo, +3.2f, 0.2f, wh);

        for (float x = -1f; x <= 1; x += 0.5f) {
            builder.addPart(x, wo, -3.5f, 0.2f, wh);
            if (x % 1 == 0)
                builder.addPart(x, wo, +3.5f, 0.2f, wh);
            else
                builder.addCollidingPart(x, wo, +3.5f, 0.2f, wh);
        }


        partData = builder.build();
    }


    public EntityBus(World worldIn) {
        super(worldIn, SEAT_AMOUNT);
        this.setSize(1.75F, 0.2f); //1.8125F

        this.setDriverSeat(2.75F, -1.3F, -0.9F);
        this.addSeat(1.05F, -1.3F, -0.9F);
        this.addSeat(1.05F, -1.3F, 0.9F);

        this.addSeat(0.05F, -1.3F, -0.9F);
        this.addSeat(0.05F, -1.3F, 0.9F);

        this.addSeat(-2.7f, -1.3F, -0.9F);
        this.addSeat(-2.7f, -1.3F, 0.9F);

        this.wheelLength = 7 / 2;
        this.wheelWidth = 3 / 2;

        this.steeringMax = 2;
        this.steeringMod = 0.07;

        this.zPitchOffset = 1.3;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public double getMountedYOffset() {
        return 1.45; //1.812*0.8
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
    public ModelBase getCarModel() {
        return Registry.MODEL_BUS;
    }

    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return new ResourceLocation(Carz.MODID, "textures/cars/bus.png");
    }

    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.translate(x, y + 1.5, z);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.rotationPitch, 1.0F, 0.0F, 0.0F);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

    }
}