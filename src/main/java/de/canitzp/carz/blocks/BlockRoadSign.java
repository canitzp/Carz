package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.items.ItemBlockSign;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class BlockRoadSign extends BlockContainerBase<BlockRoadSign> {

    public static final AxisAlignedBB SIGN_DEFAULT_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    public static final AxisAlignedBB SIGN_DEFAULT_TOP = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public static final PropertyEnum<EnumSigns> SIGN_TYPE = PropertyEnum.create("type", EnumSigns.class);
    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    public BlockRoadSign() {
        super(Material.IRON, TileSign.class);
        this.setCreativeTab(Registry.TAB);
        this.setRegistryName(Carz.MODID, "road_sign");
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setDefaultState(this.blockState.getBaseState().withProperty(SIGN_TYPE, EnumSigns.WARNING).withProperty(BOTTOM, true).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void registerClient() {
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockSign(this);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(SIGN_TYPE).ordinal());
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
        return new BlockStateContainer(this, SIGN_TYPE, BOTTOM, FACING);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(SIGN_TYPE).getColor();
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && state.getValue(BOTTOM)) {
            world.setBlockState(pos.up(), this.getDefaultState().withProperty(BOTTOM, false).withProperty(FACING, state.getValue(FACING)).withProperty(SIGN_TYPE, state.getValue(SIGN_TYPE)), 1 | 2);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(BOTTOM)) {
            return state.getValue(SIGN_TYPE).getBottomBoundingBox();
        }
        return state.getValue(SIGN_TYPE).getTopBoundingBox();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nullable BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        for (AxisAlignedBB typeAABB : state.getValue(BOTTOM) ? state.getValue(SIGN_TYPE).getBottomHitBoxes() : state.getValue(SIGN_TYPE).getTopHitBoxes()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, typeAABB);
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumSigns sign : EnumSigns.values()) {
            items.add(new ItemStack(this, 1, sign.ordinal()));
        }
    }

}
