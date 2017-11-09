package de.canitzp.carz.blocks;

import de.canitzp.carz.util.BlockProps;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public enum EnumRotatableBasicBlocks {

    DELINEATOR_GERMAN(Material.CIRCUITS, MapColor.WHITE_STAINED_HARDENED_CLAY){
        @Nonnull
        @Override
        public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
            switch (state.getValue(BlockProps.FACING)){
                case NORTH: {
                    return new AxisAlignedBB(13/16D, 0D, 6/16D, 1D, 1D, 9/16D);
                }
                case SOUTH: {
                    return new AxisAlignedBB(0D, 0D, 5/16D, 3/16D, 1D, 8/16D);
                }
            }
            return super.getBoundingBox(state, source, pos);
        }
    },
    ROAD_BARRIER_HIGHWAY(Material.CIRCUITS, MapColor.GRAY_STAINED_HARDENED_CLAY){
        @Nonnull
        @Override
        public EnumFacing transformDirection(EnumFacing direction) {
            return direction.getOpposite();
        }
    },
    ROAD_BARIER_BUILDING_SITE_ROUND(Material.CIRCUITS, MapColor.ORANGE_STAINED_HARDENED_CLAY),
    ROAD_BARRIER_BUILDING_SITE_WHITE(Material.CIRCUITS, MapColor.WHITE_STAINED_HARDENED_CLAY),
    ROAD_BARRIER_BUILDING_SITE_RED(Material.CIRCUITS, MapColor.RED),
    HYDRANT(Material.IRON, MapColor.RED),
    ;

    private Material material;
    private MapColor color;
    private BlockBaseRotateable block;

    EnumRotatableBasicBlocks(Material material, MapColor color){
        this.material = material;
        this.color = color;
    }

    public BlockBaseRotateable getBlock() {
        return block;
    }

    @Nonnull
    public EnumFacing transformDirection(EnumFacing direction) {
        return direction;
    }

    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BlockBase.FULL_BLOCK_AABB;
    }

    @Nonnull
    public List<AxisAlignedBB> getHitBoxes(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool){
        return Collections.singletonList(this.getBoundingBox(state, world, pos));
    }

    public static void registerBlocks(){
        for(EnumRotatableBasicBlocks base : values()){
            base.block = new BlockBaseRotateable(base.material, base.color, base.name().toLowerCase()){
                @Override
                protected EnumFacing transformDirection(EnumFacing direction) {
                    return base.transformDirection(direction);
                }

                @Override
                public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
                    return base.getBoundingBox(state, source, pos);
                }

                @Override
                public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
                    for(AxisAlignedBB hitBox : base.getHitBoxes(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_)){
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, hitBox);
                    }
                }
            };
            base.block.register();
        }
    }
}
