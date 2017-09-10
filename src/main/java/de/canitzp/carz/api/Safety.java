package de.canitzp.carz.api;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class Safety {

    public static <T extends TileEntity> T getTile(IBlockAccess world, BlockPos pos, Class<T> tileClass){
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null && tileClass.isAssignableFrom(tile.getClass())){
            return (T) tile;
        }
        throw new RuntimeException(""); // TODO valid text to tell the user that his/her world is corrupted
    }

    public static <T extends Entity> T getEntity(World world, int entityId, Class<T> entityClass){
        Entity entity = world.getEntityByID(entityId);
        if(entity != null && entityClass.isAssignableFrom(entity.getClass())){
            return (T) entity;
        }
        throw new RuntimeException("");
    }

}
