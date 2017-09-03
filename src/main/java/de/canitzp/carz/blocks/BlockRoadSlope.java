package de.canitzp.carz.blocks;

import de.canitzp.carz.client.renderer.RenderRoad;
import de.canitzp.carz.tile.TileRoadSlope;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import static de.canitzp.carz.util.BlockProps.*;

/**
 * The meta for this looks like this:
 * Slope Number   Facing
 *      11          10
 * @author canitzp
 */
public class BlockRoadSlope extends BlockRoad<BlockRoadSlope>{

    public BlockRoadSlope() {
        super("road_slope");
        this.overrideTileClass(TileRoadSlope.class);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(SLOPE_NUMBER, 0));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileRoadSlope.class, new RenderRoad());
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        int slopeNumber = (meta >> 2) & 3; // 1110 => 0011
        int facing = meta & 3; // 1110 => 0010
        return super.getStateFromMeta(meta).withProperty(SLOPE_NUMBER, slopeNumber).withProperty(FACING, EnumFacing.getHorizontal(facing));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SLOPE_NUMBER) << 2 | state.getValue(FACING).getHorizontalIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, SLOPE_NUMBER);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && state.getValue(SLOPE_NUMBER) == 0) {
            EnumFacing baseFacing = state.getValue(FACING);
            world.setBlockState(pos.offset(baseFacing), this.getDefaultState().withProperty(FACING, baseFacing).withProperty(SLOPE_NUMBER, 1));
            world.setBlockState(pos.offset(baseFacing, 2), this.getDefaultState().withProperty(FACING, baseFacing).withProperty(SLOPE_NUMBER, 2));
            world.setBlockState(pos.offset(baseFacing, 3), this.getDefaultState().withProperty(FACING, baseFacing).withProperty(SLOPE_NUMBER, 3));
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing()).withProperty(SLOPE_NUMBER, 0);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            int slopeNumber = state.getValue(SLOPE_NUMBER);
            EnumFacing baseFacing = state.getValue(FACING);
            for(int i = 0; i <= 3; i++){
                BlockPos offsetPos = pos.offset(baseFacing, -slopeNumber).offset(baseFacing, i);
                IBlockState state1 = world.getBlockState(offsetPos);
                if(state1.getBlock() instanceof BlockRoadSlope && state1.getValue(SLOPE_NUMBER) != slopeNumber){
                    world.destroyBlock(offsetPos, false);
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        this.onBlockDestroyedByPlayer(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public boolean canBePlaced(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, EnumHand hand, float hitX, float hitY, float hitZ) {
        EnumFacing baseFacing = state.getValue(FACING);
        if(state.getValue(SLOPE_NUMBER) == 0){
            for(int i = 1; i <= 3; i++){
                if(!world.mayPlace(this, pos.offset(baseFacing, i), true, facing, player)){
                    return false;
                }
            }
            return true;
        }
        return super.canBePlaced(world, pos, state, player, facing, hand, hitX, hitY, hitZ);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(SLOPE_NUMBER)){
            case 0: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
            case 1: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
            case 2: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
            default: return FULL_BLOCK_AABB;
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        boolean isXFacing = state.getValue(FACING) == EnumFacing.WEST || state.getValue(FACING) == EnumFacing.EAST;
        switch (state.getValue(SLOPE_NUMBER)){
            case 0:{
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0D, 0.0D, 0.0D, isXFacing ? 0.25D : 1.0D, 1/16D, !isXFacing ? 0.25D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.25D : 0.0D, 0.0D, !isXFacing ? 0.25D : 0.0D, isXFacing ? 0.5D : 1.0D, 2/16D, !isXFacing ? 0.5D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.5D : 0.0D, 0.0D, !isXFacing ? 0.5D : 0.0D, isXFacing ? 0.75D : 1.0D, 3/16D, !isXFacing ? 0.75D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.75D : 0.0D, 0.0D, !isXFacing ? 0.75D : 0.0D, 1.0D, 4/16D, 1.0D));
                break;
            }
            case 1:{
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0D, 0.0D, 0.0D, isXFacing ? 0.25D : 1.0D, 5/16D, !isXFacing ? 0.25D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.25D : 0.0D, 0.0D, !isXFacing ? 0.25D : 0.0D, isXFacing ? 0.5D : 1.0D, 6/16D, !isXFacing ? 0.5D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.5D : 0.0D, 0.0D, !isXFacing ? 0.5D : 0.0D, isXFacing ? 0.75D : 1.0D, 7/16D, !isXFacing ? 0.75D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.75D : 0.0D, 0.0D, !isXFacing ? 0.75D : 0.0D, 1.0D, 8/16D, 1.0D));
                break;
            }
            case 2:{
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0D, 0.0D, 0.0D, isXFacing ? 0.25D : 1.0D, 9/16D, !isXFacing ? 0.25D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.25D : 0.0D, 0.0D, !isXFacing ? 0.25D : 0.0D, isXFacing ? 0.5D : 1.0D, 10/16D, !isXFacing ? 0.5D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.5D : 0.0D, 0.0D, !isXFacing ? 0.5D : 0.0D, isXFacing ? 0.75D : 1.0D, 11/16D, !isXFacing ? 0.75D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.75D : 0.0D, 0.0D, !isXFacing ? 0.75D : 0.0D, 1.0D, 12/16D, 1.0D));
                break;
            }
            default: {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0D, 0.0D, 0.0D, isXFacing ? 0.25D : 1.0D, 13/16D, !isXFacing ? 0.25D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.25D : 0.0D, 0.0D, !isXFacing ? 0.25D : 0.0D, isXFacing ? 0.5D : 1.0D, 14/16D, !isXFacing ? 0.5D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.5D : 0.0D, 0.0D, !isXFacing ? 0.5D : 0.0D, isXFacing ? 0.75D : 1.0D, 15/16D, !isXFacing ? 0.75D : 1.0D));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(isXFacing ? 0.75D : 0.0D, 0.0D, !isXFacing ? 0.75D : 0.0D, 1.0D, 16/16D, 1.0D));
                break;
            }
        }
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return null;
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}