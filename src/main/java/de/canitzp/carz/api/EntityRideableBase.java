package de.canitzp.carz.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents rideable vehicles
 * //TODO: Get rid of the generic boat stuff
 *
 * @author MisterErwin
 */
public abstract class EntityRideableBase extends EntityMoveableBase {

    protected Vec3d[] seats = new Vec3d[]{new Vec3d(0,0,0)};

    public EntityRideableBase(World worldIn) {
        super(worldIn);
    }

    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < getMaxPassengerAmount();
    }

    public int getMaxPassengerAmount(){
        return this.seats.length;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote && !player.isSneaking()) {
            player.startRiding(this);
            return true;
        }
        return false;
    }

    /**
     * Applies this boat's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = /*f;//*/MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    /**
     * Applies this entity's orientation (pitch/yaw) to another entity. Used to update passenger orientation.
     */
    @SideOnly(Side.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            float f = 0.0F;
            float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset());

            int index = 0;
            if (this.getPassengers().size() > 1) {
                index = this.getPassengers().indexOf(passenger);
            }
            if (index==-1)
                return;
            Vec3d seat = seats[index];

            Vec3d vec3d = seat.rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
            passenger.setPosition(this.posX + vec3d.x, this.posY + (double) f1 + vec3d.y, this.posZ + vec3d.z);
            passenger.rotationYaw += this.deltaRotation;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
            this.applyYawToEntity(passenger);

//            if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1) {
//                int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
//                passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + (float) j);
//                passenger.setRotationYawHead(passenger.getRotationYawHead() + (float) j);
//            }
        }
    }

}
