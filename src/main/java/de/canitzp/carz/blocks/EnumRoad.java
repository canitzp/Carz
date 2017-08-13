package de.canitzp.carz.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public enum EnumRoad implements IStringSerializable {

    DEFAULT(MapColor.BLACK, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 7 / 8D, 1.0D));

    private MapColor color;
    private AxisAlignedBB boundingBox;

    EnumRoad(MapColor color, AxisAlignedBB boundingBox) {
        this.color = color;
        this.boundingBox = boundingBox;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    public MapColor getColor() {
        return color;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public List<AxisAlignedBB> getCollisionBoxes() {
        return Collections.singletonList(this.boundingBox);
    }
}
