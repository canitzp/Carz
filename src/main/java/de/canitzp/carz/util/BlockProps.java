package de.canitzp.carz.util;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.EnumFacing;

/**
 * @author canitzp
 */
public class BlockProps {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", input -> input == EnumFacing.NORTH || input == EnumFacing.WEST || input == EnumFacing.SOUTH || input == EnumFacing.EAST);
    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    public static final PropertyInteger SLOPE_NUMBER = PropertyInteger.create("slope_number", 0, 3);

}
