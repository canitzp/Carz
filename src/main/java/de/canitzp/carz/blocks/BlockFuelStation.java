package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.tile.TileFuelStation;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static de.canitzp.carz.util.BlockProps.BOTTOM;
import static net.minecraft.block.BlockDirectional.FACING;

/**
 * @author canitzp
 */
@SuppressWarnings({"WeakerAccess", "deprecation"})
public class BlockFuelStation extends BlockContainerBase<BlockFuelStation> {

    public static final AxisAlignedBB AABB_BASE_PLATE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    public static final AxisAlignedBB AABB_STAND_N = new AxisAlignedBB(0.0D, 0.0D, 3 / 16D, 7 / 8D, 1.0D, 13 / 16D);
    public static final AxisAlignedBB AABB_STAND_S = new AxisAlignedBB(1-AABB_STAND_N.minX, AABB_STAND_N.minY, AABB_STAND_N.minZ, 1-AABB_STAND_N.maxX, AABB_STAND_N.maxY, AABB_STAND_N.maxZ);
    public static final AxisAlignedBB AABB_STAND_W = new AxisAlignedBB(AABB_STAND_N.minZ, AABB_STAND_N.minY, 1- AABB_STAND_N.minX, AABB_STAND_N.maxZ, AABB_STAND_N.maxY, 1- AABB_STAND_N.maxX);
    public static final AxisAlignedBB AABB_STAND_E = new AxisAlignedBB(AABB_STAND_N.minZ, AABB_STAND_N.minY, AABB_STAND_N.minX, AABB_STAND_N.maxZ, AABB_STAND_N.maxY, AABB_STAND_N.maxX);

    public BlockFuelStation() {
        super(Material.IRON, TileFuelStation.class);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(1.5F);
        this.setResistance(7.5F);
        this.setRegistryName(new ResourceLocation(Carz.MODID, "fuel_station"));
        this.setDefaultState(this.blockState.getBaseState().withProperty(BOTTOM, true).withProperty(FACING, EnumFacing.NORTH));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(new ResourceLocation(this.getRegistryName().toString()), "inventory"));
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();
        if (meta >= EnumFacing.values().length) {
            return state.withProperty(FACING, EnumFacing.values()[meta - EnumFacing.values().length]).withProperty(BOTTOM, true);
        }
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta]).withProperty(BOTTOM, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal() + (state.getValue(BOTTOM) ? EnumFacing.values().length : 0);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BOTTOM, FACING);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && state.getValue(BOTTOM)) {
            world.setBlockState(pos.up(), this.getDefaultState().withProperty(BOTTOM, false).withProperty(FACING, state.getValue(FACING)), 1 | 2);
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BOTTOM, true).withProperty(FACING, placer.getHorizontalFacing());
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
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (!state.getValue(BOTTOM)) {
            switch (state.getValue(FACING)){
                case NORTH: return AABB_STAND_N;
                case SOUTH: return AABB_STAND_S;
                case EAST: return AABB_STAND_E;
                case WEST: return AABB_STAND_W;
            }
        }
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nullable BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        if(pos != null){
            if (state.getValue(BOTTOM)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE_PLATE);
            }
            switch (state.getValue(FACING)){
                case NORTH: {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_STAND_N);
                    break;
                }
                case SOUTH: {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_STAND_S);
                    break;
                }
                case EAST: {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_STAND_E);
                    break;
                }
                case WEST: {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_STAND_W);
                    break;
                }
            }
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(BOTTOM);
    }
}
