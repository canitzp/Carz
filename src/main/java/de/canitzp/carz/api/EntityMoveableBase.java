package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import de.canitzp.carz.CarzStats;
import de.canitzp.carz.client.models.ModelCar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


/**
 * Represents moveable vehicles
 * //TODO: Documentation
 * //TODO: Collision
 *
 * @author MisterErwin
 */
public abstract class EntityMoveableBase extends EntityRenderdBase {
    protected float deltaRotation;
    protected float momentum, angularMomentum;
    protected int spinningTicks = 0; //Out of control

    public double speedSqAbs, speedSq;
    public double angle;

    private double lastColX = 0, lastColZ = 0;
    private int lastColTime;

    public EntityMoveableBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.onUpdate(this.canPassengerSteer());
        this.stepHeight = 0.2f; //Yeah - config

    }

    protected void onUpdate(boolean canPassengerSteer) {
        if (canPassengerSteer) {
            this.updateMotion();
        } else {
            if (this.motionZ != 0)
                Carz.LOG.info("MotZ = " + this.motionZ);
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }
    }

    private void updateMotion() {
        if (spinningTicks > 0) {
            if (--spinningTicks > 15) {
                this.momentum = 0.8F;
                this.angularMomentum = 0.9F;

                world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY + 2, this.posZ, 0.1, 0.1, 0.1);
            }
        } else {
            this.momentum = this.angularMomentum = 0.9F;
        }
        this.motionX *= (double) this.momentum;
        this.motionZ *= (double) this.momentum;
        this.motionY += this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
        this.deltaRotation *= this.angularMomentum;

        this.speedSqAbs = this.motionZ * this.motionZ + this.motionX * this.motionX;
        if (this.speedSqAbs > 0.00001) {
            double momYaw = MathHelper.wrapDegrees(MathHelper.atan2(this.motionZ, this.motionX) * 180 / Math.PI) - 90;
            double rotYaw = MathHelper.wrapDegrees(this.rotationYaw);
            this.angle = MathHelper.wrapDegrees(rotYaw - momYaw);
            this.speedSq = (this.angle > 170 || this.angle < -170) ? -this.speedSqAbs : this.speedSqAbs;
        } else {
            this.angle = 0;
            this.speedSq = 0;
        }
    }

    private void updateServerDrivingData() {
        double x = this.posX - this.lastColX;
        double z = this.posZ - this.lastColZ;
        Carz.LOG.info("X=>" + this.posX + "-" + this.lastColX);

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
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox(); //so others can't pass through us
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return super.getEntityBoundingBox();
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
    public void applyEntityCollision(Entity entityIn) {
        if (world.isRemote || this.isPassenger(entityIn)) return;
        if (entityIn instanceof EntityMoveableBase) {

        } else if (entityIn instanceof EntityLiving) {
            if (this.ticksExisted - this.lastColTime > 10) {
                this.lastColX = this.posX;
                this.lastColZ = this.posZ;
                this.lastColTime = this.ticksExisted;
            } else if (this.lastColX != 0) {
                this.updateServerDrivingData();
                double angle = Math.atan2(this.posZ, this.posX) - Math.atan2(entityIn.posZ, entityIn.posX);
                Carz.LOG.info("Hit with angle " + angle + " // " + this.angle + " @ " + this.speedSq);

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
                this.lastColTime += 20;
            }
        }
    }



}
