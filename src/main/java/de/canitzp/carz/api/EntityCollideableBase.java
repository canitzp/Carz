package de.canitzp.carz.api;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;

import java.util.*;

/**
 * @author MisterErwin
 */
public abstract class EntityCollideableBase extends EntityPartedBase {
    private List<SubBoundingBox> subBoundingBoxes = new ArrayList<>();

    public EntityCollideableBase(World worldIn) {
        super(worldIn);
    }

    public void addSubBoundingBox(double ox, double oy, double oz, AxisAlignedBB axisAlignedBB) {
        this.addSubBoundingBox(new Vec3d(ox, oy, oz), axisAlignedBB);
    }

    public void addSubBoundingBox(Vec3d offset, AxisAlignedBB axisAlignedBB) {
        this.subBoundingBoxes.add(new SubBoundingBox(offset, axisAlignedBB));
    }

    /**
     * Here you're be able to manipulate the hitboxes of the car.
     * This is done via an event {@link de.canitzp.carz.events.CarEvents#entityCollisionEvent(GetCollisionBoxesEvent)}
     *
     * @param aabb The current hit box of the touching entity
     * @param list The currently defined hit boxes
     * @return A list of manipulated hit boxes
     */
    public List<AxisAlignedBB> getHitBoxes(AxisAlignedBB aabb, final List<AxisAlignedBB> list) {
        if (!subBoundingBoxes.isEmpty()) {
            float sin = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float cos = MathHelper.cos(this.rotationYaw * 0.017453292F);
            List<AxisAlignedBB> foundBoxes = new ArrayList<>();

            int n = list.size();

            for (SubBoundingBox boundingBox : subBoundingBoxes) {
                AxisAlignedBB bb = boundingBox.boundingBox.offset(
                        posX + boundingBox.offset.x * cos - boundingBox.offset.z * sin,
                        posY + boundingBox.offset.y,
                        posZ + boundingBox.offset.x * sin + boundingBox.offset.z * cos);
                Iterator<AxisAlignedBB> checkAgainstIt = list.iterator();
                while (checkAgainstIt.hasNext()) {
                    AxisAlignedBB checkAgainstBB = checkAgainstIt.next();
                    if (bb.intersects(checkAgainstBB)) {
                        foundBoxes.add(checkAgainstBB);
                        checkAgainstIt.remove();
                    }
                }
            }
            SubBoundingBox boundingBox = subBoundingBoxes.get(0);
            AxisAlignedBB bb = boundingBox.boundingBox.offset(
                    posX + boundingBox.offset.x * cos - boundingBox.offset.z * sin,
                    posY + boundingBox.offset.y,
                    posZ + boundingBox.offset.x * sin + boundingBox.offset.z * cos);

            if (!foundBoxes.isEmpty())
                return foundBoxes;
            return foundBoxes;
//            return foundBoxes;
        }
        return list;
    }

    /**
     * Here you're be able to manipulate the hitboxes of the car.
     * This is done via an event {@link de.canitzp.carz.events.CarEvents#entityCollisionEvent(GetCollisionBoxesEvent)}
     *
     * @param aabb The current hit box of the touching entity
     * @param list The currently defined hit boxes
     * @return A list of manipulated hit boxes
     */
    public List<AxisAlignedBB> getAnyHitBox(AxisAlignedBB aabb, final List<AxisAlignedBB> list) {
        if (!subBoundingBoxes.isEmpty()) {
            float sin = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float cos = MathHelper.cos(this.rotationYaw * 0.017453292F);
            List<AxisAlignedBB> foundBoxes = new ArrayList<>();

            for (SubBoundingBox boundingBox : subBoundingBoxes) {
                AxisAlignedBB bb = boundingBox.boundingBox.offset(
                        posX + boundingBox.offset.x * cos - boundingBox.offset.z * sin,
                        posY + boundingBox.offset.y,
                        posZ + boundingBox.offset.x * sin + boundingBox.offset.z * cos);
                for (AxisAlignedBB checkAgainstBB : list) {
                    if (bb.intersects(checkAgainstBB)) {
                        return Collections.singletonList(checkAgainstBB);
                    }
                }
            }
            return Collections.emptyList();
        }
        return list;
    }

    public List<AxisAlignedBB> getHitBoxes() {
        if (!subBoundingBoxes.isEmpty()) {
            float sin = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float cos = MathHelper.cos(this.rotationYaw * 0.017453292F);
            List<AxisAlignedBB> list = new ArrayList<>();

            for (SubBoundingBox boundingBox : subBoundingBoxes) {
                list.add( boundingBox.boundingBox.offset(
                        posX + boundingBox.offset.x * cos - boundingBox.offset.z * sin,
                        posY + boundingBox.offset.y,
                        posZ + boundingBox.offset.x * sin + boundingBox.offset.z * cos));
            }
            return list;
        }
        return Collections.singletonList(this.getEntityBoundingBox());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
//        if (world.isRemote)
//            renderTest();
    }

    public void renderTest() {
        if (!subBoundingBoxes.isEmpty()) {
            float sin = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float cos = MathHelper.cos(this.rotationYaw * 0.017453292F);
            for (SubBoundingBox boundingBox : subBoundingBoxes) {
                AxisAlignedBB bb = boundingBox.boundingBox.offset(
                        posX + boundingBox.offset.x * cos - boundingBox.offset.z * sin,
                        posY + boundingBox.offset.y,
                        posZ + boundingBox.offset.x * sin + boundingBox.offset.z * cos);
                if (world.getCollisionBoxes(this, bb).isEmpty())
                    spawnParticle(EnumParticleTypes.DRIP_WATER, bb);
                else
                    spawnParticle(EnumParticleTypes.DRIP_LAVA, bb);
            }
        }
        if (this.getEntityBoundingBox() != null) {
            AxisAlignedBB broadBox = getEntityBoundingBox();
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.minX, posY + broadBox.minY, posZ + broadBox.minZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.minX, posY + broadBox.minY, posZ + broadBox.maxZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.minX, posY + broadBox.maxY, posZ + broadBox.minZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.minX, posY + broadBox.maxY, posZ + broadBox.maxZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.maxX, posY + broadBox.minY, posZ + broadBox.minZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.maxX, posY + broadBox.minY, posZ + broadBox.maxZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.maxX, posY + broadBox.maxY, posZ + broadBox.minZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, posX + broadBox.maxX, posY + broadBox.maxY, posZ + broadBox.maxZ, 0, 0, 0);
        }
    }

    private void spawnParticle(EnumParticleTypes t,AxisAlignedBB bb) {
        world.spawnParticle(t, bb.minX, bb.minY, bb.minZ, 0, 0, 0);
        world.spawnParticle(t, bb.minX, bb.minY, bb.maxZ, 0, 0, 0);
        world.spawnParticle(t, bb.minX, bb.maxY, bb.minZ, 0, 0, 0);
        world.spawnParticle(t, bb.minX, bb.maxY, bb.maxZ, 0, 0, 0);

        world.spawnParticle(t, bb.maxX, bb.minY, bb.minZ, 0, 0, 0);
        world.spawnParticle(t, bb.maxX, bb.minY, bb.maxZ, 0, 0, 0);
        world.spawnParticle(t, bb.maxX, bb.maxY, bb.minZ, 0, 0, 0);
        world.spawnParticle(t, bb.maxX, bb.maxY, bb.maxZ, 0, 0, 0);
    }

    private class SubBoundingBox {
        private final Vec3d offset;
        private final AxisAlignedBB boundingBox;

        SubBoundingBox(Vec3d offset, AxisAlignedBB boundingBox) {
            this.offset = offset;
            this.boundingBox = boundingBox;
        }
    }
}
