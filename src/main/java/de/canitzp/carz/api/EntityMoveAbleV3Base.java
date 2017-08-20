package de.canitzp.carz.api;

import de.canitzp.carz.math.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by MisterErwin on 19.08.2017.
 * In case you need it, ask me ;)
 */
public abstract class EntityMoveAbleV3Base extends EntityPartedBase {

    float deltaRotation = 0;
    protected EngineData drivetrain;
    protected WheelData[] wheels;

    private float momentum = 0.9f;

    public float mass = 500;


    public EntityMoveAbleV3Base(World worldIn) {
        super(worldIn);
        wheels = new WheelData[]{new WheelData()};
        drivetrain = new EngineData(new WheelData[]{wheels[0]});
        this.stepHeight = 0.2f; //Yeah - config

    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.canPassengerSteer()){
            this.motionX *= (double) this.momentum;
            this.motionZ *= (double) this.momentum;
            this.motionY += this.hasNoGravity() ? 0.0D : -0.03999999910593033D;

            for (WheelData w : wheels){
                w.onUpdate(this);
            }
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
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



    protected Vector3 getVelocity() {
        return new Vector3((float) this.motionX, (float) motionY, (float) motionZ);
    }

    protected Vector3 getForwardDirection() {
        double cos = Math.cos(this.rotationYaw * (Math.PI / 180.0F));
        double sin = Math.sin(this.rotationYaw * (Math.PI / 180.0F));
        return new Vector3((float) (cos - sin),
                0f,
                (float) (sin + cos));
    }

    public void addForce(Vector3 vector3){
        this.motionX += vector3.x;
        this.motionY += vector3.y;
        this.motionZ += vector3.z;
    }
}
