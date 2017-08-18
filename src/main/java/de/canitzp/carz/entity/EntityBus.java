package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntitySteerableBase;
import de.canitzp.carz.client.models.ModelNakedBus;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author canitzp, MisterErwin
 */
public class EntityBus extends EntitySteerableBase {

    public EntityBus(World worldIn) {
        super(worldIn);
        this.setSize(1.75F, 1.8125F);

        this.setDriverSeat(2.75F, -1.6F, -0.9F);
    }

    @Override
    protected EntityInvisibleCarPart[] constructPartArray() {
        EntityInvisibleCarPart[] ret = new EntityInvisibleCarPart[7*3];
        int i = 0;
        for (int z=-3;z<=3;++z)
            for (int x=-1;x<=1;++x)
                ret[i++] = createPart(x,0,z, 1, 1);

        return ret;
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