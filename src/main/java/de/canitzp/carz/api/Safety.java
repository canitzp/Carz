package de.canitzp.carz.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author canitzp
 */
public class Safety {

    public static <T extends TileEntity> T getTile(IBlockAccess world, BlockPos pos, Class<T> tileClass){
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null && tile.getClass().isAssignableFrom(tileClass)){
            return (T) tile;
        }
        throw new RuntimeException(""); // TODO valid text to tell the user that his/her world is corrupted
    }

}
