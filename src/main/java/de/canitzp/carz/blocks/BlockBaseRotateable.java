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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class BlockBaseRotateable<T extends BlockBaseRotateable> extends BlockBase<T> {

    public BlockBaseRotateable(Material material, MapColor mapColor, String name) {
        super(material, mapColor);
        this.setRegistryName(new ResourceLocation(Carz.MODID, name));
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockProps.FACING, EnumFacing.NORTH));
    }

    public BlockBaseRotateable(Material material, String name) {
        this(material, material.getMaterialMapColor(), name);
    }

    protected EnumFacing transformDirection(EnumFacing direction){
        return direction;
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
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockProps.FACING, this.transformDirection(placer.getHorizontalFacing()));
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
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
