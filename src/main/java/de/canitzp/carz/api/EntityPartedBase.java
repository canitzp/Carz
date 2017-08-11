package de.canitzp.carz.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Just to test stuff... and no collisions :(
 *
 * @author MisterErwin
 */
public abstract class EntityPartedBase extends Entity implements IEntityMultiPart {
    protected CarPart[] partArray;

    public EntityPartedBase(World worldIn) {
        super(worldIn);
        this.partArray = constructArray();
    }

    /**
     * Return the Entity parts making up this Entity
     */
    public Entity[] getParts() {
        return this.partArray;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        for (CarPart part : partArray) {
            part.onUpdate();
            part.setLocationAndAngles(this.posX + part.x, this.posY + part.y, this.posZ + part.z,
                    this.rotationYaw + part.yaw, this.rotationPitch + part.pitch);
        }
    }

    public World getWorld() {
        return this.world;
    }

    public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
        return false;
    }

    //temporary removed
//    abstract protected CarPart[] constructArray();

    CarPart[] constructArray() {
        return new CarPart[]{
                new CarPart(this, "front_left", 0.5f, 1f, 1, 0, -.5f),
                new CarPart(this, "front_right", 0.5f, 2f, 1, 0, .5f),

        };
    }

    public class CarPart extends MultiPartEntityPart {
        private double x, y, z;
        private float yaw, pitch;

        public CarPart(IEntityMultiPart parent, String partName, float width, float height, double x, double y, double z, float yaw, float pitch) {
            super(parent, partName, width, height);
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public CarPart(IEntityMultiPart parent, String partName, float width, float height, double x, double y, double z) {
            this(parent, partName, width, height, x, y, z, 0, 0);
        }

    }
}
