package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.util.BlockProps;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import java.util.Random;

import static de.canitzp.carz.util.BlockProps.BOTTOM;

/**
 * @author canitzp
 */
public class BlockStreetLantern extends BlockBase<BlockStreetLantern> {

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(6/16F, 0, 6/16F, 10/16F, 1, 10/16F);

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

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if(!state.getValue(BOTTOM)){
            world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + (8/16F), pos.getY() + (14/16F), pos.getZ() + (8/16F), 0, 0, 0);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }
}
