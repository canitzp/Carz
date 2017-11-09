package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.util.BlockProps;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static de.canitzp.carz.util.BlockProps.BOTTOM;

/**
 * @author canitzp
 */
public class BlockStreetLantern extends BlockBase<BlockStreetLantern> {

    public BlockStreetLantern() {
        super(Material.IRON);
        this.setRegistryName(new ResourceLocation(Carz.MODID, "street_lantern_straight"));
        this.setDefaultState(this.blockState.getBaseState().withProperty(BOTTOM, true));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BOTTOM, meta == 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BOTTOM) ? 0 : 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BOTTOM);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && state.getValue(BOTTOM)) {
            world.setBlockState(pos.up(), this.getDefaultState().withProperty(BOTTOM, false), 3);
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BOTTOM, true);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            if (state.getValue(BOTTOM)) {
                worldIn.destroyBlock(pos.up(), false);
            } else {
                worldIn.destroyBlock(pos.down(), false);
            }
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        this.onBlockDestroyedByPlayer(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public int getLightValue(IBlockState state) {
        return !state.getValue(BOTTOM) ? 15 : 0;
    }
}
