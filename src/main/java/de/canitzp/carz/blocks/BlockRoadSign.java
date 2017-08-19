package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.IPaintableBlock;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.renderer.RenderRoadSign;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.block.BlockDirectional.FACING;

/**
 * @author canitzp
 */
public class BlockRoadSign extends BlockContainerBase<BlockRoadSign> implements IPaintableBlock{

    public static final AxisAlignedBB SIGN_DEFAULT_BOTTOM = new AxisAlignedBB(1 / 16D, 0.0D, 7 / 16D, 15 / 16D, 1.0D, 9 / 16D);
    public static final AxisAlignedBB SIGN_DEFAULT_TOP = new AxisAlignedBB(0.0D, 0.0D, 7 / 16D, 1.0D, 1.0F, 9 / 16D);

    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");

    public BlockRoadSign() {
        super(Material.IRON, TileSign.class);
        this.setRegistryName(Carz.MODID, "road_sign");
        this.setDefaultState(this.blockState.getBaseState().withProperty(BOTTOM, true).withProperty(FACING, EnumFacing.NORTH));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRoadSign.this), new ResourceLocation(Carz.MODID, "signs/triangle"));
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(new ResourceLocation(Carz.MODID, "signs/triangle"), "inventory");
            }
        });
        ClientRegistry.bindTileEntitySpecialRenderer(TileSign.class, new RenderRoadSign());
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = getTileFromSign(world, pos);
        if (tile != null) {
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
        TileSign tile = getTileFromSign(world, pos);
        if(tile != null){
            AxisAlignedBB box = tile.getBoundingBox(state.getValue(BOTTOM));
            switch (state.getValue(FACING)){
                case NORTH: case SOUTH:{
                    return box;
                }
                case WEST: case EAST:{
                    return new AxisAlignedBB(box.minZ, box.minY, box.minX, box.maxZ, box.maxY, box.maxX);
                }
            }
        }
        return super.getBoundingBox(state, world, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nullable BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        TileEntity tile = getTileFromSign(world, pos);
        if (tile != null) {
            for (AxisAlignedBB typeAABB : ((TileSign) tile).getHitBoxes(state.getValue(BOTTOM))) {
                switch (state.getValue(FACING)){
                    case NORTH: case SOUTH:{
                        break;
                    }
                    case WEST: case EAST:{
                        typeAABB = new AxisAlignedBB(typeAABB.minZ, typeAABB.minY, typeAABB.minX, typeAABB.maxZ, typeAABB.maxY, typeAABB.maxX);
                    }
                }
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

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer playerIn) {
        TileSign tile = getTileFromSign(world, pos);
        if(tile != null){
            if(world.getBlockState(pos).getValue(BOTTOM)){
                if(tile.getLowerMesh() != null){
                    tile.setLowerMesh(null);
                }
            } else {
                if(tile.getUpperMesh() != null){
                    tile.setUpperMesh(null);
                }
            }
        }
    }

    @Override
    public void clickedWithPainter(World world, BlockPos pos, EntityPlayer player, IBlockState state, EnumHand hand, EnumFacing facing, PixelMesh mesh, float hitX, float hitY, float hitZ){
        TileSign tile = getTileFromSign(world, pos);
        if(tile != null){
            tile.clickedWithPainter(mesh, state.getValue(BOTTOM));
        }
    }

    @Nullable
    public static TileSign getTileFromSign(IBlockAccess world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == Registry.blockRoadSign){
            if(!state.getBlock().hasTileEntity(state)){
                pos = pos.down();
            }
            return (TileSign) world.getTileEntity(pos);
        }
        return null;
    }
}
