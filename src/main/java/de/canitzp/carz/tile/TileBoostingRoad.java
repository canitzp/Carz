package de.canitzp.carz.tile;

import de.canitzp.carz.api.EntityMoveableBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author MisterErwin
 */
public class TileBoostingRoad extends TileRoad {
    private boolean absolute;
    private int x, y, z;

    public void applyEntityWalk(EntityMoveableBase moveable) {
        //TODO: Well - get x and z to work
        if (absolute) {
            moveable.addVelocity(x / 50d, y / 50d, z / 50d);
        } else {
            double cos = Math.cos(moveable.angle);
            double sin = Math.sin(moveable.angle);
            moveable.addVelocity(x / 50d * cos - z / 50d * sin, y / 50d, z / 50d * cos + x / 50d * sin);
        }
    }

    public boolean isAbsolute() {
        return absolute;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setAbsolute(boolean absolute) {
        this.absolute = absolute;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("mx", 3)) {
            this.x = compound.getInteger("mx");
        }
        if (compound.hasKey("my", 3)) {
            this.y = compound.getInteger("my");
        }
        if (compound.hasKey("mz", 3)) {
            this.z = compound.getInteger("mz");
        }
        if (compound.hasKey("absolute")) {
            this.absolute = compound.getBoolean("absolute");
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("mx", x);
        compound.setInteger("my", y);
        compound.setInteger("mz", z);
        compound.setBoolean("absolute", absolute);
        return super.writeToNBT(compound);
    }
}
