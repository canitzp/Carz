package de.canitzp.carz.entity;

import de.canitzp.carz.client.models.ModelCar;
import de.canitzp.carz.client.renderer.RenderCar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntityCar extends Entity {

    public EntityCar(World worldIn) {
        super(worldIn);
        //this.setSize(1.5F, 0.65F);
        this.width = 1.0F;
        this.height = 0.65F;
        AxisAlignedBB aabb = this.getEntityBoundingBox();
        this.setEntityBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
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

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote && !player.isSneaking()) {
            player.startRiding(this);
            return true;
        }
        return false;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        double length = this.getCarLenght() / 2;
        float f = this.width / 2.0F;
        float f1 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - length, y, z - (double)f, x + length, y + (double)f1, z + (double)f));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }

    /**
     * Returns the collision bounding box for this entity
     */
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public ModelCar getCarModel(){
        return RenderCar.MODEL_CAR;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    public ResourceLocation getCarTexture(){
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks){
        GlStateManager.translate(x - 1F, y + 1.15F, z + 0.5F);
        GlStateManager.rotate(180.0F, 1.0F, 0, 0);
    }

    public double getCarLenght(){
        return 2.0D;
    }

}