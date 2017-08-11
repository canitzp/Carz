package de.canitzp.carz.entity;

import de.canitzp.carz.api.EntitySteerableBase;
import de.canitzp.carz.client.renderer.RenderCar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Just a test car
 * @author MisterErwin
 */
public class EntityTestCar extends EntitySteerableBase {
    public EntityTestCar(World worldIn) {
        super(worldIn);
        this.setSize(1.375F, 0.5625F);
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

    @SideOnly(Side.CLIENT)
    public void renderCar(double x, double y, double z, float entityYaw, float partialTicks) {
        RenderCar.MODEL_CAR.render(this, partialTicks, 0, -0.1F, 0, 0, 0.0625F);
    }
}
