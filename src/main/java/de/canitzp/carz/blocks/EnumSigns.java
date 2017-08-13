package de.canitzp.carz.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public enum EnumSigns implements IStringSerializable{

    WARNING(MapColor.RED, BlockSign.SIGN_DEFAULT_BOTTOM, BlockSign.SIGN_DEFAULT_TOP), //Danger ahead
    UIWPFR(MapColor.RED, BlockSign.SIGN_DEFAULT_BOTTOM, BlockSign.SIGN_DEFAULT_TOP); //Unmarked intersection ahead with priority from right

    private MapColor color;
    private AxisAlignedBB bottomBB, topBB;

    EnumSigns(MapColor color, AxisAlignedBB bottomBB, AxisAlignedBB topBB){
        this.color = color;
        this.bottomBB = bottomBB;
        this.topBB = topBB;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    public MapColor getColor() {
        return color;
    }

    public AxisAlignedBB getBottomBoundingBox() {
        return bottomBB;
    }

    public AxisAlignedBB getTopBoundingBox() {
        return topBB;
    }

    public List<AxisAlignedBB> getBottomHitBoxes(){
        return Collections.singletonList(this.getBottomBoundingBox());
    }

    public List<AxisAlignedBB> getTopHitBoxes(){
        return Collections.singletonList(this.getTopBoundingBox());
    }

}
