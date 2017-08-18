package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.api.EntitySteerableBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author canitzp, MisterErwin
 */
public class EntityBus extends EntitySteerableBase {

    private static EntityPartedBase.PartData partData;

    static {
        EntityPartedBase.PartBuilder builder = builder();
        //Unterboden
        for (int z = -3; z <= 3; ++z)
            for (int x = -1; x <= 1; ++x)
                builder.addCollidingPart(x, 0, z, 1, 0.2f);

        float wh = 1.6f;
        float wo = 0.1f; //wall offset Y
        for (float z = -3; z <= 3; z += 0.5f)
            builder.addPart(1.5f, 0, z, 0.2f, wh);

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
        super(worldIn);
        this.setSize(1.75F, 0.2f); //1.8125F

        this.setDriverSeat(2.75F, -1.6F, -0.9F);
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
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}