package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.util.BlockProps;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class BlockDelineator extends BlockBase<BlockDelineator> {

    public BlockDelineator() {
        super(Material.CIRCUITS, MapColor.WHITE_STAINED_HARDENED_CLAY);
        this.setRegistryName(new ResourceLocation(Carz.MODID, "delineator_german"));

        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockProps.FACING, EnumFacing.NORTH));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(BlockProps.FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockProps.FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockProps.FACING);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockProps.FACING, placer.getHorizontalFacing());
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

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
        return FULL_BLOCK_AABB;
    }
}
