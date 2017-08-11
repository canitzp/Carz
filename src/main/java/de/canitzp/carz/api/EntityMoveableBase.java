package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;


/**
 * Represents moveable vehicles
 * //TODO: Documentation
 * //TODO: Collision
 * @author MisterErwin
 */
public abstract class EntityMoveableBase extends Entity {
    protected float deltaRotation;
    protected float momentum, angularMomentum;
    protected int spinningTicks = 0; //Out of control

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
        if (canPassengerSteer)
            this.updateMotion();
        else {
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
    }

    /**
     * Returns the collision bounding box for this entity
     */
    @javax.annotation.Nullable
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
}
