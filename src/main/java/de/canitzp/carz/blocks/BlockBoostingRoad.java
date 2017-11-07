package de.canitzp.carz.blocks;

import de.canitzp.carz.api.EntityMoveableBase;
import de.canitzp.carz.tile.TileBoostingRoad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * @author MisterErwin
 */
public class BlockBoostingRoad extends BlockRoad<BlockBoostingRoad> {

    public BlockBoostingRoad() {
        super("road_boosting"); //ToDo: Fix block texture?
        this.overrideTileClass(TileBoostingRoad.class);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            entity.motionX *= 1.25D;
            entity.motionZ *= 1.25D;
        } else if (entity instanceof EntityMoveableBase) {
            TileEntity tile = world.getTileEntity(pos);
            if (!(tile instanceof TileBoostingRoad)) return;
            ((TileBoostingRoad) tile).applyEntityWalk((EntityMoveableBase) entity);
        }
    }
}
