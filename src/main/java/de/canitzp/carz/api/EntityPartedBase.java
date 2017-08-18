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
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Just to test stuff... and no collisions :(
 *
 * @author MisterErwin
 */
public abstract class EntityPartedBase extends EntityRenderedBase {
    protected EntityInvisibleCarPart[] partArray;
    private EntityInvisibleCarPart[] collidingParts;
    //TODO: Seperate parts that are only following without the move/rotation collision checks

    public EntityPartedBase(World worldIn) {
        super(worldIn);

        this.partArray = constructPartArray();

        int[] col = this.constructCollidingPartIndizes();
        this.collidingParts = new EntityInvisibleCarPart[col.length];
        for (int i = 0; i < col.length; ++i)
            this.collidingParts[i] = this.partArray[col[i]];

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //TODO: Check collision for rotation
        for (EntityInvisibleCarPart part : partArray) {
            part.onUpdate();
        }
    }

    public EntityInvisibleCarPart[] getPartArray() {
        return partArray;
    }


    public EntityInvisibleCarPart[] getCollidingParts() {
        return collidingParts;
    }

    protected int[] allPartIndizes() {
        int[] ret = new int[this.partArray.length];
        for (int i = 0; i < this.partArray.length; ++i)
            ret[i] = i;
        return ret;
    }

    /**
     * @param from start Index incl
     * @param to   end index ecl
     * @return an array containing these indizes
     */
    protected int[] indizesRange(int from, int to) {
        int[] ret = new int[to - from];
        int n = 0;
        for (int i = from; i < to; ++i)
            ret[n++] = i;
        return ret;
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
//            if (type == MoverType.PISTON) {
//                long i = this.world.getTotalWorldTime();
//
//                if (i != this.pistonDeltasGameTime) {
//                    Arrays.fill(this.pistonDeltas, 0.0D);
//                    this.pistonDeltasGameTime = i;
//                }
//
//                if (x != 0.0D) {
//                    int j = EnumFacing.Axis.X.ordinal();
//                    double d0 = MathHelper.clamp(x + this.pistonDeltas[j], -0.51D, 0.51D);
//                    x = d0 - this.pistonDeltas[j];
//                    this.pistonDeltas[j] = d0;
//
//                    if (Math.abs(x) <= 9.999999747378752E-6D) {
//                        return;
//                    }
//                } else if (y != 0.0D) {
//                    int l4 = EnumFacing.Axis.Y.ordinal();
//                    double d12 = MathHelper.clamp(y + this.pistonDeltas[l4], -0.51D, 0.51D);
//                    y = d12 - this.pistonDeltas[l4];
//                    this.pistonDeltas[l4] = d12;
//
//                    if (Math.abs(y) <= 9.999999747378752E-6D) {
//                        return;
//                    }
//                } else {
//                    if (z == 0.0D) {
//                        return;
//                    }
//
//                    int i5 = EnumFacing.Axis.Z.ordinal();
//                    double d13 = MathHelper.clamp(z + this.pistonDeltas[i5], -0.51D, 0.51D);
//                    z = d13 - this.pistonDeltas[i5];
//                    this.pistonDeltas[i5] = d13;
//
//                    if (Math.abs(z) <= 9.999999747378752E-6D) {
//                        return;
//                    }
//                }
//            }

            this.world.profiler.startSection("move");
            double d10 = this.posX;
            double d11 = this.posY;
            double d1 = this.posZ;

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


            Set<AxisAlignedBB> list1 = this.getWorldCollisionBoxes(this, x, y, z);
            AxisAlignedBB[] backup = new AxisAlignedBB[this.collidingParts.length + 1];


            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                backup[i] = e.getEntityBoundingBox();
                if (y != 0) {
                    for (AxisAlignedBB aList1 : list1) {
                        y = aList1.calculateYOffset(e.getEntityBoundingBox(), y);
                    }
                }
            }
            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                if (y != 0)
                    e.setEntityBoundingBox(e.getEntityBoundingBox().offset(0.0D, y, 0.0D));
                if (x != 0) {
                    for (AxisAlignedBB aList1 : list1) {
                        x = aList1.calculateXOffset(e.getEntityBoundingBox(), x);
                    }
                }
            }
            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                if (x != 0) {
                    e.setEntityBoundingBox(e.getEntityBoundingBox().offset(x, 0.0D, 0.0D));
                }
                if (z != 0) {
                    for (AxisAlignedBB aList1 : list1) {
                        z = aList1.calculateZOffset(e.getEntityBoundingBox(), z);
                    }
                }
            }
            for (int i = 0; i < backup.length; ++i) {
                Entity e = i == 0 ? this : this.collidingParts[i - 1];
                if (z != 0) {
                    e.setEntityBoundingBox(e.getEntityBoundingBox().offset(0.0D, 0.0D, z));
                }
            }
//        }


//
            //

            boolean flag = this.onGround || origY != y && origY < 0.0D;

            //TODO: stepHeight
            if (this.stepHeight > 0.0F && flag && (origX != x || origZ != z) ) {
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
//                List<AxisAlignedBB> list = this.getWorldCollisionBoxes(this, this.getEntityBoundingBox().expand(origX, y, origZ));
                AxisAlignedBB[] axisalignedbb2 = new AxisAlignedBB[this.collidingParts.length + 1];
                AxisAlignedBB[] backup3 = new AxisAlignedBB[this.collidingParts.length + 1];
                List<AxisAlignedBB> list = new ArrayList<>();
                double d8 = y;
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
                    list.addAll(this.world.getCollisionBoxes(e, e.getEntityBoundingBox().expand(origX, y, origZ)));
//                    AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
                    axisalignedbb2[i] = e.getEntityBoundingBox();
                    AxisAlignedBB axisalignedbb3 = axisalignedbb2[i].expand(origX, 0.0D, origZ);

                    for (AxisAlignedBB bb : list) {
                        d8 = bb.calculateYOffset(axisalignedbb3, d8);
                    }
                }
                double d18 = origX;
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
                    axisalignedbb2[i] = axisalignedbb2[i].offset(0.0D, d8, 0.0D);

                    for (AxisAlignedBB bb : list) {
                        d18 = bb.calculateXOffset(axisalignedbb2[i], d18);
                    }
                }
                double d19 = origZ;

                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
                    axisalignedbb2[i] = axisalignedbb2[i].offset(d18, 0.0D, 0.0D);

                    for (AxisAlignedBB bb : list) {
                        d19 = bb.calculateZOffset(axisalignedbb2[i], d19);
                    }
                }

                AxisAlignedBB[] axisalignedbb4 = new AxisAlignedBB[this.collidingParts.length + 1];
                double d20 = y;
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];

                    axisalignedbb2[i] = axisalignedbb2[i].offset(0.0D, 0.0D, d19);

                    axisalignedbb4[i] = e.getEntityBoundingBox();
//                    AxisAlignedBB axisalignedbb4 = e.getEntityBoundingBox();


                    for (AxisAlignedBB bb : list) {
                        d20 = bb.calculateYOffset(axisalignedbb4[i], d20);
                    }
                }
                double d21 = origX;
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
                    axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, d20, 0.0D);


                    for (AxisAlignedBB bb : list) {
                        d21 = bb.calculateXOffset(axisalignedbb4[i], d21);
                    }
                }
                double d22 = origZ;
                for (int i = 0; i < backup1.length; ++i) {
                    Entity e = i == 0 ? this : this.collidingParts[i - 1];

                    axisalignedbb4[i] = axisalignedbb4[i].offset(d21, 0.0D, 0.0D);


                    for (AxisAlignedBB bb : list) {
                        d22 = bb.calculateZOffset(axisalignedbb4[i], d22);
                    }
                }
//                for (int i = 0; i < backup1.length; ++i) {
//                    Entity e = i == 0 ? this : this.collidingParts[i - 1];
//                    axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, 0.0D, d22);
//                }
                double d23 = d18 * d18 + d19 * d19;
                double d9 = d21 * d21 + d22 * d22;

                if (d23 > d9) {
                    x = d18;
                    z = d19;
                    y = -d8;
//                        e.setEntityBoundingBox(axisalignedbb2);
                    for (int i = 0; i < backup1.length; ++i) {
                        Entity e = i == 0 ? this : this.collidingParts[i - 1];
//                        axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, 0.0D, d22);
                        e.setEntityBoundingBox(axisalignedbb2[i]);
                    }
//                    e.setEntityBoundingBox(axisalignedbb2[i]);
                } else {
                    x = d21;
                    z = d22;
                    y = -d20;
//                    e.setEntityBoundingBox(axisalignedbb4);
                    for (int i = 0; i < backup1.length; ++i) {
                        Entity e = i == 0 ? this : this.collidingParts[i - 1];
                        axisalignedbb4[i] = axisalignedbb4[i].offset(0.0D, 0.0D, d22);
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
                        for (AxisAlignedBB bb : list) {
                            y = bb.calculateYOffset(e.getEntityBoundingBox(), y);
                        }
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
            data.add(new AbstractMap.SimpleImmutableEntry<>(new float[]{offsetX, offsetY, offsetZ, width, height}, false));
            return this;
        }

        public PartBuilder addCollidingPart(float offsetX, float offsetY, float offsetZ, float width, float height) {
            data.add(new AbstractMap.SimpleImmutableEntry<>(new float[]{offsetX, offsetY, offsetZ, width, height}, true));
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
