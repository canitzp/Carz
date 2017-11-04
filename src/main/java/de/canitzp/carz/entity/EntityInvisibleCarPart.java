package de.canitzp.carz.entity;

import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.util.MathUtil;
import de.canitzp.carz.util.VehiclePackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Created by MisterErwin on 14.08.2017.
 * In case you need it, ask me ;)
 */
public class EntityInvisibleCarPart extends Entity {
    private EntityPartedBase parent;

    private float offsetX, offsetY, offsetZ;

    /**
     * Are other entities "allowed" to collide with
     */
    public boolean colliding = true;

    /**
     * If true, other entities are able to stand on this part and will be moved along
     */
    public boolean moveAlong = true;

    @SuppressWarnings("unused")
    public EntityInvisibleCarPart(World worldIn) {
        super(worldIn);
    }

    public EntityInvisibleCarPart(EntityPartedBase parent, float width, float height, float offsetX, float offsetY, float offsetZ) {
        super(parent.world);
        this.parent = parent;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.setSize(width, height);
    }

    @Override
    public void entityInit() {

    }

    @Override
    public @Nonnull
    String getName() {
        if (parent != null)
            return parent.getName();
        return super.getName();
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound nbtTag) {
        //Empty - do not save me
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound nbtTag) {
        //Empty - do not save me
    }

    @Override
    public boolean writeToNBTAtomically(@Nonnull NBTTagCompound compound) {
        return false;
    }

    @Override
    public boolean writeToNBTOptional(@Nonnull NBTTagCompound compound) {
        return false;
    }

    @Override
    public @Nonnull
    NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return compound;
    }

    @Override
    public void onUpdate() {
        if (this.parent == null || this.parent.isDead) { //Most likely not called on the client, but yeah :)
            this.world.removeEntity(this);
        }
        //The stuff normally done here is now called from the parent, with "cached" sin/cos
    }

    /**
     * Called from the parent (EntityPartedBase)
     *
     * @param transX       an optional translation along the x-axis
     * @param transY       an optional translation along the y-axis
     * @param transZ       an optional translation along the z-axis
     * @param cosYaw       the parent yaw-cos
     * @param sinYaw       the parent yaw-sin
     * @param cosPitch     the parent pitch-cos
     * @param sinPitch     the parent pitch-sin
     * @param cosRoll      the parent roll-cos
     * @param sinRoll      the parent roll-sin
     * @param movingAlong_ possible moving along candidates (grouped)
     */
    public void onUpdate(double transX, double transY, double transZ, double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll, List<Entity> movingAlong_) {
        this.setPositionAndUpdate(
                this.parent.posX + transX + MathUtil.rotX(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll),
                this.parent.posY + transY + MathUtil.rotY(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll),
                this.parent.posZ + transZ + MathUtil.rotZ(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll));

        onUpdateAlong(movingAlong_);

        super.onUpdate();
    }

    /**
     * Called from the parent (EntityPartedBase)
     *
     * @param cosYaw       the parent yaw-cos
     * @param sinYaw       the parent yaw-sin
     * @param cosPitch     the parent pitch-cos
     * @param sinPitch     the parent pitch-sin
     * @param cosRoll      the parent roll-cos
     * @param sinRoll      the parent roll-sin
     * @param movingAlong_ possible moving along candidates (grouped)
     */
    public void onUpdate(double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll, List<Entity> movingAlong_) {
        this.setPositionAndUpdate(
                this.parent.posX + MathUtil.rotX(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll),
                this.parent.posY + MathUtil.rotY(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll),
                this.parent.posZ + MathUtil.rotZ(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll));

        onUpdateAlong(movingAlong_);
    }

    private void onUpdateAlong(List<Entity> movingAlong_) {
        if (!this.world.isRemote && colliding) {
            if (moveAlong) {
                this.moveAlongNearbyEntities(movingAlong_);
            }
            this.collideWithNearbyEntities();
        } else if (this.world.isRemote && moveAlong) {
            this.moveAlongNearbyEntities(movingAlong_);
        }

        super.onUpdate();
    }

    /**
     * Called from the parent (EntityPartedBase)
     * <p>
     * Oh... for many parts, this is "kinda" performance hungry - be warned
     *
     * @param cosYaw   the parent yaw-cos
     * @param sinYaw   the parent yaw-sin
     * @param cosPitch the parent pitch-cos
     * @param sinPitch the parent pitch-sin
     * @param cosRoll  the parent roll-cos
     * @param sinRoll  the parent roll-sin
     */
    public void onUpdate(double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll) {
        this.setPositionAndUpdate(
                this.parent.posX + MathUtil.rotX(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll),
                this.parent.posY + MathUtil.rotY(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll),
                this.parent.posZ + MathUtil.rotZ(this.offsetX, this.offsetY, this.offsetZ,
                        cosYaw, sinYaw, cosPitch, sinPitch, cosRoll, sinRoll));

        if (!this.world.isRemote && colliding) {
            if (moveAlong) {
                this.moveAlongNearbyEntities();
            }
            this.collideWithNearbyEntities();
        } else if (this.world.isRemote && moveAlong) {
            this.moveAlongNearbyEntities();
        }

        super.onUpdate();
    }

    @Override
    public boolean isEntityEqual(Entity entity) {
        return this == entity || this.parent == entity;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return colliding;
    }

    /**
     * Returns the collision bounding box for this entity
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return colliding ? this.getEntityBoundingBox() : null; //so others can't pass through us
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null; //to pass through other entities
    }

    private void collideWithNearbyEntities() {
        //TODO: (entity instanceof EntityInvisibleCarPart) allow from different parents
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        entities.stream()
                .filter(entity -> entity != this.parent && !(entity instanceof EntityInvisibleCarPart) && entity.canBePushed() && !this.parent.getPassengers().contains(entity) && !parent.movingAlong.contains(entity))
                .forEach(entity -> entity.applyEntityCollision(this.parent));
    }


    private void moveAlongNearbyEntities(List<Entity> movingAlong_) {
        //Check entities standing on us
        AxisAlignedBB bb = this.getEntityBoundingBox().expand(-0.01, 0.5D, -0.01);
        movingAlong_.stream()
                .filter(entity -> entity != this && entity != this.parent && entity.getEntityBoundingBox().intersects(bb) && !(entity instanceof EntityInvisibleCarPart) && entity.canBePushed() && !this.parent.getPassengers().contains(entity))
                .forEach(e -> parent.movingAlong.add(e));
    }

    private void moveAlongNearbyEntities() {
        //Collect entities standing on us
        List<Entity> movingAlong_ = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(-0.01, 0.5D, -0.01));
        movingAlong_.stream()
                .filter(entity -> entity != this.parent && !(entity instanceof EntityInvisibleCarPart) && entity.canBePushed() && !this.parent.getPassengers().contains(entity))
                .forEach(e -> parent.movingAlong.add(e));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }

    public void doBlockCollisionsFromParent() {
        super.doBlockCollisions();
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
        //All move-checks are done via the parent
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        //Maybe redirect to the parent?
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (this.parent != null && !this.parent.isDead) {
            int index = -1;
            for (int i = 0; i < this.parent.getPartArray().length; ++i) {
                if (parent.getPartArray()[i] == this) {
                    index = i;
                    break;
                }
            }
            if (world.isRemote) {
                VehiclePackets.sendCarInteractToServer(this.parent, hand, index);
            } else {
                return this.parent.processInitialInteract(player, hand, index);
            }
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.parent != null && !this.parent.isDead) {
            int index = -1;
            for (int i = 0; i < this.parent.getPartArray().length; ++i) {
                if (parent.getPartArray()[i] == this) {
                    index = i;
                    break;
                }
            }
            if (world.isRemote) {
                VehiclePackets.sendCarInteractToServer(this.parent, (short) -1, index);
            } else {
                return this.parent.attackEntityFrom(source, amount);
            }
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void applyEntityCollision(Entity entityIn) {
        if (parent != null && !parent.isDead && colliding) {
            parent.applyEntityCollision(entityIn);
        }
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public float getWidthOffset() {
        return Math.max(Math.abs(this.offsetX), Math.abs(this.offsetZ)) + 0.5f * this.width;
    }

    public float getHeightOffset() {
        return Math.abs(this.offsetY) + 0.5f * this.height;
    }
}
