package de.canitzp.carz.api;

import de.canitzp.carz.entity.EntityInvisibleCarPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Just to test stuff... and no collisions :(
 * //TODO: Clean up this mess - it's even worse than my room - and my room looks like a warzone
 *
 * @author MisterErwin
 */
public abstract class EntityPartedBase extends EntityWorldInteractionBase {
    private EntityInvisibleCarPart[] partArray;
    private EntityInvisibleCarPart[] collidingParts;

    protected float deltaRotationPitch;
    //TODO: Seperate parts that are only following without the move/rotation collision checks

    private AxisAlignedBB renderBoundingBox;

    protected float horizontalCollisionModifier = 0.2f;

    public Set<Entity> movingAlong = new HashSet<>();

    public EntityPartedBase(World worldIn) {
        super(worldIn);

        this.partArray = constructPartArray();

        int[] col = this.constructCollidingPartIndizes();
        this.collidingParts = new EntityInvisibleCarPart[col.length];
        for (int i = 0; i < col.length; ++i)
            this.collidingParts[i] = this.partArray[col[i]];

        //Get a bounding box loosely representing our total size
        float rwidth = this.width, rheight = this.height;
        for (EntityInvisibleCarPart p : this.partArray) {
            rwidth = Math.max(p.getWidthOffset(), rwidth);
            rheight = Math.max(p.getHeightOffset(), rheight);
        }
        rwidth += 0.2;
        rheight += 0.2;
        this.renderBoundingBox = new AxisAlignedBB(-rwidth * 1.3, -rheight, -rwidth * 1.3,
                rwidth * 1.3, rheight, rwidth * 1.3);

    }

    @Override
    public void onUpdate() {
        //Reset the movingAlong collection, as we will re-add them in onUpdate if needed
        movingAlong.clear();
        //prev renderYawOffset

        double cosYaw = Math.cos(this.rotationYaw * (Math.PI / 180.0F));
        double sinYaw = Math.sin(this.rotationYaw * (Math.PI / 180.0F));
        double cosPitch = Math.cos(-this.rotationPitch * (Math.PI / 180.0F));
        double sinPitch = Math.sin(-this.rotationPitch * (Math.PI / 180.0F));
        for (EntityInvisibleCarPart part : partArray) {
            part.onUpdate(cosYaw, sinYaw, cosPitch, sinPitch, 1, 0);
        }

        super.onUpdate();
    }

    public boolean isMovingAlong(Entity entity) {
        return movingAlong.contains(entity);
    }

    public EntityInvisibleCarPart[] getPartArray() {
        return partArray;
    }


    public EntityInvisibleCarPart[] getCollidingParts() {
        return collidingParts;
    }

    /**
     * This method will be only called during the spawning of the parted entity.
     *
     * @return an array of EntityInvisibleCarParts containing this entities parts
     */
    protected EntityInvisibleCarPart[] constructPartArray() {
        return new EntityInvisibleCarPart[]{};
    }

    protected int[] constructCollidingPartIndizes() {
        return new int[]{};
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand, int partIndex) {
        return this.processInitialInteract(player, hand);
    }

    protected EntityInvisibleCarPart createPart(float offsetX, float offsetY, float offsetZ, float width, float height) {
        return new EntityInvisibleCarPart(this, width, height, offsetX, offsetY, offsetZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public @Nonnull
    AxisAlignedBB getRenderBoundingBox() {
        return this.renderBoundingBox.offset(posX, posY, posZ);
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();
        for (EntityInvisibleCarPart part : collidingParts)
            part.doBlockCollisionsFromParent();
    }

    /**
     * Gets a list of bounding boxes that intersect with all my EntityBoundingBoxed expanded.
     */
    public Set<AxisAlignedBB> getWorldCollisionBoxes(@Nullable Entity entityIn, double expandX, double expandY, double expandZ) {
        Set<AxisAlignedBB> returnSet = new HashSet<>(); //No duplicates
        returnSet.addAll(this.getWorldCollisionBoxes(this, this.getEntityBoundingBox().expand(expandX, expandY, expandZ)));
        for (EntityInvisibleCarPart part : this.collidingParts)
            returnSet.addAll(this.getWorldCollisionBoxes(part, part.getEntityBoundingBox().expand(expandX, expandY, expandZ)));
        return returnSet;
    }

    /**
     * Gets a list of bounding boxes that intersect with the provided AABB.
     */
    public List<AxisAlignedBB> getWorldCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb) {
        List<AxisAlignedBB> boxes = this.world.getCollisionBoxes(null, aabb);
        if (entityIn != null) {
            addEntity(entityIn, this, aabb, boxes);
            for (EntityInvisibleCarPart part : this.collidingParts)
                addEntity(part, this, aabb, boxes);
        }
        return boxes;
    }

    private void addEntity(Entity entityIn, Entity entityParentIgnored, AxisAlignedBB aabb, List<AxisAlignedBB> boxes) {
        List<Entity> list1 = this.world.getEntitiesWithinAABBExcludingEntity(entityIn, aabb.grow(0.25D));
        for (Entity entity : list1) {
            if (!entityIn.isRidingSameEntity(entity) && !entity.isEntityEqual(entityIn) && !entity.isEntityEqual(entityParentIgnored)) {
                AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();

                if (axisalignedbb != null && axisalignedbb.intersects(aabb)) {
                    boxes.add(axisalignedbb);
                }

                axisalignedbb = entityIn.getCollisionBox(entity);
                if (axisalignedbb != null && axisalignedbb.intersects(aabb)) {
                    boxes.add(axisalignedbb);
                }
            }
        }
    }

    //ToDo: Performance - like: for real

    /**
     * Tries to move the entity towards the specified location.
     */
    @Override
    public void move(MoverType type, double x, double y, double z) {
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        } else {
            this.world.profiler.startSection("move");

            if (this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25D;
                y *= 0.05000000074505806D;
                z *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double origX = x;
            double origY = y;
            double origZ = z;


            Set<AxisAlignedBB> worldCollisionBoxes = this.getWorldCollisionBoxes(this, x, y, z);
            AxisAlignedBB[] backup = new AxisAlignedBB[this.collidingParts.length + 1];

            double temp;

            List<AxisAlignedBB> collisions = new ArrayList<>();
            double realFirstY = y;
            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                backup[i] = e.getEntityBoundingBox();
                if (y != 0) {
                    temp = realFirstY;
                    for (AxisAlignedBB bb : worldCollisionBoxes) {
                        y = bb.calculateYOffset(e.getEntityBoundingBox(), y);
                        temp = bb.calculateYOffset(e.getEntityBoundingBox(), temp);
                    }
                    e.onGround = (temp != realFirstY);
                } else
                    e.onGround = true;
            }

            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                if (y != 0)
                    e.setEntityBoundingBox(e.getEntityBoundingBox().offset(0.0D, y, 0.0D));
                if (x != 0) {
                    temp = x;
                    for (AxisAlignedBB aList1 : worldCollisionBoxes) {
                        x = aList1.calculateXOffset(e.getEntityBoundingBox(), x);
                        if (x != temp)
                            collisions.add(aList1);
                        temp = x;
                    }
                }
            }
            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                if (x != 0) {
                    e.setEntityBoundingBox(e.getEntityBoundingBox().offset(x, 0.0D, 0.0D));
                }
                if (z != 0) {
                    temp = z;
                    for (AxisAlignedBB bb : worldCollisionBoxes) {
                        z = bb.calculateZOffset(e.getEntityBoundingBox(), z);
                        if (z != temp)
                            collisions.add(bb);
                        temp = z;
                    }
                }
            }
            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                if (z != 0) {
                    e.setEntityBoundingBox(e.getEntityBoundingBox().offset(0.0D, 0.0D, z));
                }
            }

            //On impact: reduce
            if (x != origX && z == origZ) {
                z *= horizontalCollisionModifier;
                origZ *= horizontalCollisionModifier;
            } else if (x == origX && z != origZ) {
                x *= horizontalCollisionModifier;
                origX *= horizontalCollisionModifier;
            }

            boolean flag = this.onGround || origY != y && origY < 0.0D;

            //TODO: stepHeight
            if (this.stepHeight > 0.0F && flag && (origX != x || origZ != z)) {
                collisions.clear(); //Welp - efficiency will be dammed
                double curX = x;
                double curY = y;
                double curZ = z;
                AxisAlignedBB[] backup1 = new AxisAlignedBB[this.collidingParts.length + 1];
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
                    backup1[i] = e.getEntityBoundingBox();
                    //AxisAlignedBB axisalignedbb1 = this.getEntityBoundingBox();
                    e.setEntityBoundingBox(backup[i]);
                }
                y = (double) this.stepHeight;
                if (this.rotationPitch != 0)
                    y *= 2;
//                List<AxisAlignedBB> list = this.getWorldCollisionBoxes(this, this.getEntityBoundingBox().expand(origX, y, origZ));
                AxisAlignedBB[] axisalignedbb2 = new AxisAlignedBB[this.collidingParts.length + 1];
                AxisAlignedBB[] backup3 = new AxisAlignedBB[this.collidingParts.length + 1];
                List<AxisAlignedBB> list = new ArrayList<>();
                double potY = y;
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
                    list.addAll(this.world.getCollisionBoxes(e, e.getEntityBoundingBox().expand(origX, y, origZ)));
//                    AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
                    axisalignedbb2[i] = e.getEntityBoundingBox();
                    AxisAlignedBB axisalignedbb3 = axisalignedbb2[i].expand(origX, 0.0D, origZ);

                    for (AxisAlignedBB bb : list) {
                        potY = bb.calculateYOffset(axisalignedbb3, potY);
                    }
                }

                double potX = origX;
                for (int i = 0; i < backup1.length; ++i) {
                    axisalignedbb2[i] = axisalignedbb2[i].offset(0.0D, potY, 0.0D);

                    for (AxisAlignedBB bb : list) {
                        temp = potX;
                        potX = bb.calculateXOffset(axisalignedbb2[i], potX);
                        if (temp != potX)
                            collisions.add(bb);
                    }
                }
                double potZ = origZ;

                for (int i = 0; i < backup1.length; ++i) {
                    axisalignedbb2[i] = axisalignedbb2[i].offset(potX, 0.0D, 0.0D);

                    for (AxisAlignedBB bb : list) {
                        temp = potZ;
                        potZ = bb.calculateZOffset(axisalignedbb2[i], potZ);
                        if (temp != potZ)
                            collisions.add(bb);
                    }
                }

                AxisAlignedBB[] axisalignedbb4 = new AxisAlignedBB[this.collidingParts.length + 1];
                double someOtherY = y;
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];

                    axisalignedbb2[i] = axisalignedbb2[i].offset(0.0D, 0.0D, potZ);

                    axisalignedbb4[i] = e.getEntityBoundingBox();
//                    AxisAlignedBB axisalignedbb4 = e.getEntityBoundingBox();


                    for (AxisAlignedBB bb : list) {
                        someOtherY = bb.calculateYOffset(axisalignedbb4[i], someOtherY);
                    }
                }
                double ox = origX;
                for (int i = 0; i < backup1.length; ++i) {
                    axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, someOtherY, 0.0D);


                    for (AxisAlignedBB bb : list) {
                        ox = bb.calculateXOffset(axisalignedbb4[i], ox);
                    }
                }
                double oz = origZ;
                for (int i = 0; i < backup1.length; ++i) {

                    axisalignedbb4[i] = axisalignedbb4[i].offset(ox, 0.0D, 0.0D);


                    for (AxisAlignedBB bb : list) {
                        oz = bb.calculateZOffset(axisalignedbb4[i], oz);
                    }
                }
//                for (int i = 0; i < backup1.length; ++i) {
//                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
//                    axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, 0.0D, d22);
//                }
                double potXZLen = potX * potX + potZ * potZ;
                double oXZLen = ox * ox + oz * oz;

                if (potXZLen > oXZLen) {
                    x = potX;
                    z = potZ;
                    y = -potY;
//                        e.setEntityBoundingBox(axisalignedbb2);
                    for (int i = 0; i < backup1.length; ++i) {
                        Entity e = i == 0 ? this : this.collidingParts[i - 1];
//                        axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, 0.0D, d22);
                        e.setEntityBoundingBox(axisalignedbb2[i]);
                    }
//                    e.setEntityBoundingBox(axisalignedbb2[i]);
                } else {
                    x = ox;
                    z = oz;
                    y = -someOtherY;
//                    e.setEntityBoundingBox(axisalignedbb4);
                    for (int i = 0; i < backup1.length; ++i) {
                        Entity e = i == 0 ? this : this.collidingParts[i - 1];
                        axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, 0.0D, oz);
                        e.setEntityBoundingBox(axisalignedbb4[i]);
                    }
                }


                if (curX * curX + curZ * curZ >= x * x + z * z) {
                    x = curX;
                    y = curY;
                    z = curZ;
//                        e.setEntityBoundingBox(axisalignedbb1);
                    for (int i = 0; i < backup1.length; ++i) {
                        Entity e = i == 0 ? this : this.collidingParts[i - 1];
                        e.setEntityBoundingBox(backup1[i]);
                    }
                } else {
                    for (int i = 0; i < backup1.length; ++i) {
                        Entity e = i == 0 ? this : this.collidingParts[i - 1];
                        double startY = y;
                        for (AxisAlignedBB bb : list) {
                            y = bb.calculateYOffset(e.getEntityBoundingBox(), y);
                        }
//                        e.onGround = y != startY;
                        e.setEntityBoundingBox(e.getEntityBoundingBox().offset(0.0D, y, 0.0D));
                    }
                }

//                e.setEntityBoundingBox(e.getEntityBoundingBox().offset(0.0D, y, 0.0D));

//                if (curX * curX + curZ * curZ >= x * x + z * z) {
//                    x = curX;
//                    y = curY;
//                    z = curZ;
////                        e.setEntityBoundingBox(axisalignedbb1);
//                    e.setEntityBoundingBox(backup1[i]);
//                }
            }

            if (x != origX || z != origZ) {
                double d = (x - origX) * (x - origX) + (z - origZ) * (z - origZ);
                this.onCollision(d, collisions);
            }

            int yFront = 0;
            int yBack = 0;
            for (EntityInvisibleCarPart part : this.collidingParts) {
                if (part.onGround) {
                    if (part.getOffsetZ() > .3) {
                        yFront++;
                    } else if (part.getOffsetZ() < -.3) {
                        yBack++;
                    }
                }
            }
            if (yFront > yBack && yBack == 0) {
                this.rotationPitch = Math.max(-20, this.rotationPitch - 1);
            } else if (yFront < yBack && yFront == 0) {
                this.rotationPitch = Math.min(20, this.rotationPitch + 1);
            } else if (this.rotationPitch != 0 && yFront != 0 && yBack != 0) {
                this.rotationPitch = this.rotationPitch + (this.rotationPitch > 0 ? -1 : 1);
            }

            this.world.profiler.endSection();
            this.world.profiler.startSection("rest");
            this.resetPositionToBB();
            this.isCollidedHorizontally = origX != x || origZ != z;
            this.isCollidedVertically = origY != y;
            this.onGround = this.isCollidedVertically && origY < 0.0D;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            int j6 = MathHelper.floor(this.posX);
            int i1 = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k6 = MathHelper.floor(this.posZ);
            BlockPos blockpos = new BlockPos(j6, i1, k6);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == Material.AIR) {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate1 = this.world.getBlockState(blockpos1);
                Block block1 = iblockstate1.getBlock();

                if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate) {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            this.updateFallState(y, this.onGround, iblockstate, blockpos);

            if (origX != x) {
                this.motionX = 0.0D;
            }

            if (origZ != z) {
                this.motionZ = 0.0D;
            }

            Block block = iblockstate.getBlock();

            if (origY != y) {
                block.onLanded(this.world, this);
            }

            try {
                this.doBlockCollisions();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

//            boolean flag1 = this.isWet();

//            if (this.world.isFlammableWithin(this.getEntityBoundingBox().shrink(0.001D))) {
//                this.dealFireDamage(1);
//
//                if (!flag1) {
//                    ++this.fire;
//
//                    if (this.fire == 0) {
//                        this.setFire(8);
//                    }
//                }
//            } else if (this.fire <= 0) {
//                this.fire = -this.getFireImmuneTicks();
//            }

//            if (flag1 && this.isBurning()) {
//                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
//                this.fire = -this.getFireImmuneTicks();
//            }

            this.world.profiler.endSection();
        }
    }

    /**
     * @param force      A number representing how strong the collision was (not really a force)
     * @param collisions All collisions happened
     */
    protected void onCollision(double force, Collection<AxisAlignedBB> collisions) {

    }

    protected static class PartData {
        final float[][] data;
        final int[] collidingPartIndizes;

        private PartData(float[][] data, int[] collidingPartIndizes) {
            this.data = data;
            this.collidingPartIndizes = collidingPartIndizes;
        }

        public EntityInvisibleCarPart[] spawnInvisibleParts(EntityPartedBase parent) {
            EntityInvisibleCarPart[] ret = new EntityInvisibleCarPart[data.length];
            for (int i = 0, l = ret.length; i < l; ++i) {
                ret[i] = parent.createPart(data[i][0], data[i][1], data[i][2],
                        data[i][3], data[i][4]);
                if (data[i][5] == 0)
                    ret[i].colliding = false;
            }
            return ret;
        }

        public int[] getCollidingPartIndizes() {
            return collidingPartIndizes;
        }
    }

    protected static PartBuilder builder() {
        return new PartBuilder();
    }

    protected static class PartBuilder {
        private List<Map.Entry<float[], Boolean>> data = new ArrayList<>();


        public PartBuilder addPart(float offsetX, float offsetY, float offsetZ, float width, float height) {
            data.add(new AbstractMap.SimpleImmutableEntry<>(new float[]{offsetX, offsetY, offsetZ, width, height, 1}, false));
            return this;
        }

        public PartBuilder addCollidingPart(float offsetX, float offsetY, float offsetZ, float width, float height) {
            data.add(new AbstractMap.SimpleImmutableEntry<>(new float[]{offsetX, offsetY, offsetZ, width, height, 1}, true));
            return this;
        }

        public PartBuilder addInteractOnlyPart(float offsetX, float offsetY, float offsetZ, float width, float height) {
            data.add(new AbstractMap.SimpleImmutableEntry<>(new float[]{offsetX, offsetY, offsetZ, width, height, 0}, false));
            return this;
        }

        public PartData build() {
            float[][] d = new float[data.size()][];
            List<Integer> colliding = new ArrayList<>();
            int i = 0;
            for (Map.Entry<float[], Boolean> e : data) {
                if (e.getValue()) {
                    colliding.add(i);
                }
                d[i++] = e.getKey();
            }
            colliding.toArray(new Integer[0]);
            return new PartData(d, colliding.stream().mapToInt(x -> x).toArray());
        }

    }

}
