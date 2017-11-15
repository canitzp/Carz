package de.canitzp.carz.util;

import de.canitzp.carz.client.PixelMesh;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

/**
 * @author canitzp
 */
public class BlockProps {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", input -> input == EnumFacing.NORTH || input == EnumFacing.WEST || input == EnumFacing.SOUTH || input == EnumFacing.EAST);
    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    public static final PropertyInteger SLOPE_NUMBER = PropertyInteger.create("slope_number", 0, 3);

    public static final IUnlistedProperty<PixelMesh> MODEL = new IUnlistedProperty<PixelMesh>() {
        @Override
        public String getName() {
            return "model_id";
        }

        @Override
        public boolean isValid(PixelMesh value) {
            return value != null;
        }

        @Override
        public Class<PixelMesh> getType() {
            return PixelMesh.class;
        }

        @Override
        public String valueToString(PixelMesh value) {
            return value.toString();
        }
    };
    public static final IUnlistedProperty<EnumFacing> FACING_MESH =
            new Properties.PropertyAdapter<>(PropertyEnum.create("facing", EnumFacing.class));
}
