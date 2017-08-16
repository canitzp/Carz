package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.client.renderer.RenderRoadSign;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class BlockRoadSign extends BlockContainerBase<BlockRoadSign> {

    public static final AxisAlignedBB SIGN_DEFAULT_BOTTOM = new AxisAlignedBB(7 / 16D, 0.0D, 7 / 16D, 9 / 16D, 1.0D, 9 / 16D);
    public static final AxisAlignedBB SIGN_DEFAULT_TOP = new AxisAlignedBB(7 / 16D, 0.0D, 7 / 16D, 9 / 16D, 15 / 16D, 9 / 16D);

    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    public BlockRoadSign() {
        super(Material.IRON, TileSign.class);
        this.setCreativeTab(Registry.TAB);
        this.setRegistryName(Carz.MODID, "road_sign");
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setDefaultState(this.blockState.getBaseState().withProperty(BOTTOM, true).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void registerClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSign.class, new RenderRoadSign());
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSign) {
            return ((TileSign) tile).getMapColor();
        }
        return super.getMapColor(state, world, pos);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSign) {
            return ((TileSign) tile).getBoundingBox(state.getValue(BOTTOM));
        }
        return super.getBoundingBox(state, world, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nullable BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSign) {
            for (AxisAlignedBB typeAABB : ((TileSign) tile).getHitBoxes(state.getValue(BOTTOM))) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, typeAABB);
            }
        } else {
            super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entityIn, bool);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(BOTTOM);
    }
}
