package de.canitzp.carz.entity;

import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.network.MessageCarPartInteract;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Created by MisterErwin on 14.08.2017.
 * In case you need it, ask me ;)
 */
public class EntityInvisibleCarPart extends Entity {
    protected EntityPartedBase parent;

    protected float offsetX, offsetY, offsetZ;

    public boolean colliding = true;

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
    public void readEntityFromNBT(NBTTagCompound nbtTag) {

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTag) {

    }

    @Override
    public void onUpdate() {
        if (this.parent == null || this.parent.isDead) {
            this.setDead();
            super.onUpdate();
            return;
        }
        //prev renderYawOffset
        double cos = Math.cos(this.parent.rotationYaw * (Math.PI / 180.0F));
        double sin = Math.sin(this.parent.rotationYaw * (Math.PI / 180.0F));
        this.setPositionAndUpdate(this.parent.posX + this.offsetX * cos - this.offsetZ * sin,
                this.parent.posY + this.offsetY,
                this.parent.posZ + this.offsetX * sin + this.offsetZ * cos);

        if (!this.world.isRemote && colliding) {
            this.collideWithNearbyEntities();
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
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        entities.stream().filter(entity -> entity != this.parent && !(entity instanceof EntityInvisibleCarPart) && entity.canBePushed()).forEach(entity -> entity.applyEntityCollision(this.parent));
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
                NetworkHandler.net.sendToServer(new MessageCarPartInteract(this.parent.getEntityId(), hand, index));
            } else {
                this.parent.processInitialInteract(player, hand, index);
            }
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void applyEntityCollision(Entity entityIn) {
        if (parent != null && !parent.isDead && colliding) {
            parent.applyEntityCollision(entityIn);
        }
    }
}
