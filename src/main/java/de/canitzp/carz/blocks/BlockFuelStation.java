package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.tile.TileFuelStation;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class BlockFuelStation extends BlockContainer {

    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    public static final AxisAlignedBB AABB_BASE_PLATE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    public static final AxisAlignedBB AABB_STAND = new AxisAlignedBB(0.0D, 0.0D, 3 / 16D, 7 / 8D, 1.0D, 13 / 16D);

    public BlockFuelStation() {
        super(Material.IRON);
        this.setCreativeTab(Registry.TAB);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(1.5F);
        this.setResistance(7.5F);
        this.setRegistryName(new ResourceLocation(Carz.MODID, "fuel_station"));
        this.setUnlocalizedName(this.getRegistryName().toString());

        this.setDefaultState(this.blockState.getBaseState().withProperty(BOTTOM, true));
        GameRegistry.registerTileEntity(TileFuelStation.class, this.getRegistryName().toString());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(BOTTOM, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BOTTOM) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BOTTOM);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && state.getValue(BOTTOM)) {
            world.setBlockState(pos.up(), this.getDefaultState().withProperty(BOTTOM, false), 1 | 2);
        }
    }

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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFuelStation();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (!state.getValue(BOTTOM)) {
            return AABB_STAND;
        }
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nullable BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        if (state.getValue(BOTTOM)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE_PLATE);
        }
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_STAND);
    }
}
