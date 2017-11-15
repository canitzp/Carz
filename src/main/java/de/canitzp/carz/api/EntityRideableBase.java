package de.canitzp.carz.api;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents rideable vehicles
 * //TODO: Get rid of the generic boat stuff
 *
 * @author MisterErwin
 */
public abstract class EntityRideableBase extends EntityMoveableBase {

    private List<double[]> seats = Lists.newArrayList(new double[]{0.0D, 0.0D, 0.0D});

    public EntityRideableBase(World worldIn) {
        super(worldIn);
    }

    /**
     * Add a seat position.
     *
     * @param x offset from entity center
     * @param y offset from entity center
     * @param z offset from entity center
     */
    protected void addSeat(double x, double y, double z) {
        this.seats.add(new double[]{x, y, z});
    }

    /**
     * Add a seat position.
     *
     * @param x           offset from entity center
     * @param y           offset from entity center
     * @param z           offset from entity center
     * @param yawRotation extra yaw Rotation
     */
    protected void addSeat(double x, double y, double z, double yawRotation) {
        this.seats.add(new double[]{x, y, z, yawRotation});
    }

    /**
     * Sets the position of the driver seat
     *
     * @param x offset from entity center
     * @param y offset from entity center
     * @param z offset from entity center
     */
    protected void setDriverSeat(double x, double y, double z) {
        this.seats.set(0, new double[]{x, y, z});
    }

    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Can an entity enter this vehicle?
     *
     * @param passenger entity
     * @return true if said entity may enter this vehicle as a passenger
     */
    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < getMaxPassengerAmount();
    }

    public int getMaxPassengerAmount() {
        return this.seats.size();
    }


    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!(this instanceof EntityMultiSeatsBase) && !this.world.isRemote && !player.isSneaking()) {
            if (player.getHeldItem(player.getActiveHand()).getItem() instanceof ItemMonsterPlacer) {
                ItemStack spawnEgg = player.getHeldItem(player.getActiveHand());
                Entity entity = ItemMonsterPlacer.spawnCreature(this.world, ItemMonsterPlacer.getNamedIdFrom(spawnEgg), this.posX, this.posY, this.posZ);
                if (entity != null) {
                    if (entity.startRiding(this)) {
                        return true;
                    }
                }
            }
            return player.startRiding(this);
        }
        return super.processInitialInteract(player, hand);
    }

    /**
     * Applies this boat's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void applyYawToEntity(Entity entityToUpdate, float seatRotation) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw - seatRotation);

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
        int seatI = getSeatByPassenger(entityToUpdate);
        if (seatI == -1) return;
        double[] s = seats.get(seatI);
        this.applyYawToEntity(entityToUpdate, s.length == 3 ? 0 : (float) s[3]);
    }

    /**
     * Get the seat(index) to "place" an entity on.
     *
     * @param passenger the passenger
     * @return the index of the seat, or -1 if said entity is not an passenger
     */
    protected int getSeatByPassenger(Entity passenger) {
        if (!this.getPassengers().isEmpty()) {
            return this.getPassengers().indexOf(passenger);
        }
        return -1;
    }

    public void updatePassenger(@Nonnull Entity passenger) {
        if (this.isPassenger(passenger)) {
            float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset());

            int index = getSeatByPassenger(passenger);
            if (index == -1)
                return;

            double[] seat = this.seats.get(index);
            double seatRotation = (seat.length == 3) ? 0 : seat[3];
            double[] vec3d = rotateYaw(seat, -this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
            if (this instanceof EntitySteerableBase)
                passenger.setPosition(this.posX + ((EntitySteerableBase) this).rotationTranslationX + vec3d[0],
                        this.posY + (double) f1 + vec3d[1],
                        this.posZ + ((EntitySteerableBase) this).rotationTranslationZ + vec3d[2]);
            else
                passenger.setPosition(this.posX + vec3d[0], this.posY + (double) f1 + vec3d[1], this.posZ + vec3d[2]);
            passenger.rotationYaw += this.deltaRotationYaw;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotationYaw);
            passenger.rotationPitch += prevRotationPitch - rotationPitch;
            this.applyYawToEntity(passenger, (float) seatRotation);
        }
    }

    private double[] rotateYaw(double[] seat, float yaw) {
        float cos = MathHelper.cos(yaw);
        float pitch = MathHelper.sin(yaw);
        double d0 = seat[0] * (double) cos + seat[2] * (double) pitch;
        double d2 = seat[2] * (double) cos - seat[0] * (double) pitch;
        return new double[]{d0, seat[1], d2};
    }
    //ToDo: RenderLivingBase rotate prevRenderYawOffset
}
