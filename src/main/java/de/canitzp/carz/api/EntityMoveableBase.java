package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import de.canitzp.carz.network.MessageCarSpeed;
import de.canitzp.carz.network.NetworkHandler;
import de.canitzp.carz.util.MathUtil;
import de.canitzp.carz.util.TimedCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;


/**
 * Represents moveable vehicles
 * //TODO: Documentation
 * //TODO: Collision
 *
 * @author MisterErwin
 */
public abstract class EntityMoveableBase extends EntityPartedBase /*EntityCollideableBase*/ {
    protected float deltaRotationYaw;

    protected float momentum, angularMomentum;
    protected int spinningTicks = 0; //Out of control

    public double speedSqAbs, speedSq;
    public double angle, centrifugalForce, centrifugalV2;

    protected final static DataParameter<Float> SPEED = EntityDataManager.createKey(EntityMoveableBase.class, DataSerializers.FLOAT);
    private float remoteSpeed = 0;
    private double lastColX = 0, lastColZ = 0;
    private TimedCache<Integer> collisionCache = new TimedCache<>(1000);


    public EntityMoveableBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.onUpdate(this.canPassengerSteer());
        this.stepHeight = 0.26f; //Yeah - config - now a bit greater than 4/16

        if (this.isCollidedHorizontally) {
            blockCollisionCheck();
        }
        this.doBlockCollisions();

        float speed = getSpeed();
        float origRotationYaw = this.rotationYaw;
//        if (speedSqAbs > 0.001 && this.isBeingRidden()) {
        this.rotationYaw += this.deltaRotationYaw;
//        }
        this.rotationPitch += this.deltaRotationPitch;
        //TODO: Get rid of deltaRotationPitch and get the delta ourself

        this.motionX = (double) (MathHelper.sin(-this.rotationYaw * 0.017453292F) * speed);
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * speed);
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

        //(Math.PI / 180.0F) = 0.017453292F
        double cosYaw = Math.cos(-this.deltaRotationYaw * 0.017453292F);
        double sinYaw = Math.sin(this.deltaRotationYaw * 0.017453292F);
        double cosPitch = Math.cos(-this.deltaRotationPitch * 0.017453292F);
        double sinPitch = Math.sin(this.deltaRotationPitch * 0.017453292F);


        if (this.isCollidedHorizontally)
            this.rotationYaw = origRotationYaw;
        else {
            for (Entity e : movingAlong) {
                if (world.isRemote || !(e instanceof EntityPlayerMP)) { //TODO: Do I really do not want to move Players?
                    e.rotationYaw += deltaRotationYaw;
                    e.setRotationYawHead(e.getRotationYawHead() + deltaRotationYaw);

                    double ox = e.posX - this.posX, oy = e.posY - this.posY, oz = e.posZ - this.posZ;

                    //vehiclepos + moved + offset rotated

                    double ny = this.motionY + MathUtil.rotY(ox, oy, oz,
                            cosYaw, sinYaw, cosPitch, sinPitch, 1, 0);
                    if (ny > 0)
                        ny += 0.1;

                    e.setPosition(
                            this.posX + this.motionX + MathUtil.rotX(ox, oy, oz,
                                    cosYaw, sinYaw, cosPitch, sinPitch, 1, 0),
                            this.posY + ny,
                            this.posZ + this.motionZ + MathUtil.rotZ(ox, oy, oz,
                                    cosYaw, sinYaw, cosPitch, sinPitch, 1, 0)
                    );
                }
            }
        }
    }

    protected void onUpdate(boolean canPassengerSteer) {
        if (canPassengerSteer) {
            this.updateMotion();
            float speed = getSpeed();
            speed *= this.momentum;
            if (world.isRemote)
                NetworkHandler.net.sendToServer(new MessageCarSpeed(speed));
            setSpeed(speed);
        }
    }

    private void updateMotion() {
        if (spinningTicks > 0) {
            if (--spinningTicks > 15) {
                this.momentum = 0.99F;
                this.angularMomentum = 0.9F;

                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 2, this.posZ, 0.1, 0.1, 0.1);
            }
        } else {
            this.momentum = this.angularMomentum = 0.9F;
        }
        this.motionX *= (double) this.momentum;
        this.motionZ *= (double) this.momentum;
        this.motionY += this.hasNoGravity() ? 0.0D : -0.2; // -0.03999999910593033D;
        this.deltaRotationYaw *= this.angularMomentum;
        this.centrifugalV2 *= this.angularMomentum;

        this.speedSqAbs = this.motionZ * this.motionZ + this.motionX * this.motionX;
        if (this.speedSqAbs > 0.00001) {
            double momYaw = MathHelper.wrapDegrees(MathHelper.atan2(this.motionZ, this.motionX) * 180 / Math.PI) - 90;
            double rotYaw = MathHelper.wrapDegrees(this.rotationYaw);
            this.angle = MathHelper.wrapDegrees(rotYaw - momYaw);
            this.speedSq = (this.angle > 170 || this.angle < -170) ? -this.speedSqAbs : this.speedSqAbs;
            BlockPos pos = this.getPosition().add(0, -1, 0);
            IBlockState stateBase = this.world.getBlockState(pos);
            float slippery = stateBase.getBlock().getSlipperiness(stateBase, world, pos, this);
            this.centrifugalForce = slippery * this.speedSqAbs * Math.abs(this.angle);
            this.centrifugalV2 += slippery * this.speedSqAbs * Math.abs(this.angle) * 10;
        } else {
            this.angle = 0;
            this.speedSq = 0;
            this.centrifugalForce = 0;
        }
    }

    private void updateServerDrivingData() {
        double x = this.posX - this.lastColX;
        double z = this.posZ - this.lastColZ;

        this.speedSqAbs = x * x + z * z;
        if (this.speedSqAbs > 0.00001) {
            double momYaw = MathHelper.wrapDegrees(MathHelper.atan2(z, x) * 180 / Math.PI) - 90;
            double rotYaw = MathHelper.wrapDegrees(this.rotationYaw);
            this.angle = MathHelper.wrapDegrees(rotYaw - momYaw);
            this.speedSq = (this.angle > 170 || this.angle < -170) ? -this.speedSqAbs : this.speedSqAbs;
        } else {
            this.angle = 0;
            this.speedSq = 0;
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.deltaRotationYaw = 0;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(SPEED, 0f);
    }

    public void setSpeed(float speed) {
        remoteSpeed = speed;
        if (world.isRemote) {
            NetworkHandler.net.sendToServer(new MessageCarSpeed(speed));
        } else {
            this.dataManager.set(SPEED, speed);
        }
    }

    public float getSpeed() {
        return canPassengerSteer() ? remoteSpeed : this.dataManager.get(SPEED);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        this.remoteSpeed = this.dataManager.get(SPEED);
        super.addPassenger(passenger);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     * (or interacted with - in this case)
     */
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

    /**
     * Returns the collision bounding box for this entity
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox(); //so others can't pass through us
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

    @Override
    public void applyEntityCollision(@Nonnull Entity entityIn) {
        if (world.isRemote || this.isPassenger(entityIn)) return;
        if (true) return; //ToDo: Yeah - let's reuse this :)
        if (entityIn instanceof EntityMoveableBase) {

        } else if (entityIn instanceof EntityLiving) {
            if (collisionCache.contains(entityIn.getEntityId())/*this.ticksExisted - this.lastColTime > 10*/) {

//                this.lastColTime = this.ticksExisted;
            } else if (this.lastColX != 0) {
                this.updateServerDrivingData();
                double angle = Math.atan2(this.posZ, this.posX) - Math.atan2(entityIn.posZ, entityIn.posX);
                Carz.LOG.info("Hit with angle " + angle + " // " + this.angle + " @ " + this.speedSq);

                //if inside EntityBus: move along
                //if speed is great enough: deal damage
                //otherwise: push entity "softly"

                if (angle > -20 && angle < 20) {
                    //Only direct hits count
                    if (this.speedSq > 0.04) {
                        entityIn.attackEntityFrom(EntityDamageSourceCared.causeDamageAndStat(
                                this.getControllingPassenger(), this, 6), 6);
                        entityIn.addVelocity((this.posX - this.lastColX) * 17, 0.1, (this.posZ - this.lastColZ) * 17);
                    } else if (this.speedSq > 0.02) {
                        entityIn.attackEntityFrom(EntityDamageSourceCared.causeDamageAndStat(
                                this.getControllingPassenger(), this, 1), 1);
                        entityIn.addVelocity((this.posX - this.lastColX) * 2, 0.1, (this.posZ - this.lastColZ) * 2);
                    }
                }
                this.lastColX = this.lastColZ = 0;
//                this.lastColTime += 20;
                this.collisionCache.add(entityIn.getEntityId());
            } else {
                //TODO: Yeah - let's actually use the collision contact point
                this.lastColX = this.posX;
                this.lastColZ = this.posZ;
                entityIn.addVelocity(0, 2, 0);
//                this.collisionCache.add(entityIn.getEntityId());
            }
        }
    }

    private void blockCollisionCheck() {
        List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().grow(0.01, -0.01, 0.01));
        Vec3d ourCenter = getCenter(this.getCollisionBoundingBox());
        //TODO: get side of collision and apply damage
        for (AxisAlignedBB bb : list) {
            rayCheck(bb.maxX, ourCenter.y, bb.maxZ, ourCenter);
            rayCheck(bb.maxX, ourCenter.y, bb.minZ, ourCenter);
            rayCheck(bb.minX, ourCenter.y, bb.maxZ, ourCenter);
            rayCheck(bb.minX, ourCenter.y, bb.minZ, ourCenter);
            Vec3d c = getCenter(bb);
            double colYaw = MathHelper.wrapDegrees(MathHelper.atan2(this.posZ - c.z, this.posX - c.x) * 180 / Math.PI) - 90;
            double rotYaw = MathHelper.wrapDegrees(this.rotationYaw);
            double colAngle = -(MathHelper.wrapDegrees(rotYaw - colYaw) - 90) + 90;
            //ToDO: yeah - do I really need this?
        }
    }

    @Nullable
    private RayTraceResult rayCheck(double x, double y, double z, Vec3d center) {
        //calculateIntercept(pos, view)
        Vec3d p = new Vec3d(x, y, z);
//        world.spawnParticle(EnumParticleTypes.DRIP_LAVA, x, y, z, 0.1, 0.1, 0.1);
        RayTraceResult rtr = this.getCollisionBoundingBox().calculateIntercept(p, center.subtract(p));
//        if (rtr != null)
//            world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, rtr.hitVec.x, rtr.hitVec.y, rtr.hitVec.z, 0.1, 0.1, 0.1);
        return rtr;
    }

    /**
     * Copy as AxisAlignedBB#getCenter is client only
     *
     * @param b the bounding box
     * @return the center
     * @see AxisAlignedBB#getCenter()
     */
    private Vec3d getCenter(AxisAlignedBB b) {
        return new Vec3d(b.minX + (b.maxX - b.minX) * 0.5D, b.minY + (b.maxY - b.minY) * 0.5D, b.minZ + (b.maxZ - b.minZ) * 0.5D);
    }

    @Override
    protected void onCollision(double force, Collection<AxisAlignedBB> collisions) {
        if (force > 0.09 && this.speedSqAbs > 0.05) {
            System.out.println("Collision with " + force);
            System.out.println(collisions.size());
            for (AxisAlignedBB bb : collisions) {
                System.out.println(bb.maxX + "|" + bb.minX);
                world.destroyBlock(new BlockPos(bb.maxX - 0.2, bb.maxY - 0.2, bb.maxZ - 0.2), true);
            }
        }
    }
}
