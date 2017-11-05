package de.canitzp.carz.blocks;

import com.google.common.collect.Lists;
import de.canitzp.carz.Carz;
import de.canitzp.carz.util.BlockProps;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public enum EnumBasicBlocks implements IStringSerializable {

    PYLON_EUROPEAN(Material.CIRCUITS, MapColor.ORANGE_STAINED_HARDENED_CLAY, new AxisAlignedBB(3/16D, 0.0D, 3/16D, 13/16D, 12/16D, 13/16D)){
        @Override
        public boolean isFullBlock(IBlockState state) {
            return false;
        }

        @Nonnull
        @Override
        public List<AxisAlignedBB> getHitBoxes(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
            return Lists.newArrayList(new AxisAlignedBB(7/16D, 0.0D, 7/16D, 9/16D, 12/16D, 9/16D),
                    new AxisAlignedBB(6/16D, 0.0D, 6/16D, 10/16D, 9/16D, 10/16D),
                    new AxisAlignedBB(5/16D, 0.0D, 5/16D, 11/16D, 6/16D, 11/16D),
                    new AxisAlignedBB(4/16D, 0.0D, 4/16D, 12/16D, 3/16D, 12/16D),
                    new AxisAlignedBB(2/16D, 0.0D, 2/16D, 14/16D, 1/16D, 14/16D))
            ;
        }
    };

    private BlockBasic instance;
    @Nonnull private Material material;
    @Nonnull private MapColor color;
    @Nonnull private AxisAlignedBB boundingBox;

    EnumBasicBlocks(@Nonnull Material material, @Nonnull MapColor color, @Nonnull AxisAlignedBB boundingBox){
        this.material = material;
        this.color = color;
        this.boundingBox = boundingBox;
    }

    @Nonnull
    public Material getMaterial(IBlockState state) {
        return material;
    }

    @Nonnull
    public MapColor getColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return color;
    }

    public boolean isFullBlock(IBlockState state){
        return true;
    }

    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    @Nonnull
    public List<AxisAlignedBB> getHitBoxes(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool){
        return Collections.singletonList(this.getBoundingBox(state, world, pos));
    }

    @Nonnull
    public IBlockState getDefaultState(IBlockState baseState){
        return baseState;
    }

    @Nonnull
    public BlockStateContainer createBlockState(BlockBasic block){
        return new BlockStateContainer(block);
    }

    public int parseMeta(IBlockState state){
        return 0;
    }

    @Nonnull
    public IBlockState parseState(int meta, IBlockState defaultState){
        return defaultState;
    }

    @Nonnull
    public IBlockState getPlacementState(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, IBlockState defaultState){
        return defaultState;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public ModelResourceLocation getModelResourceLocation(){
        return new ModelResourceLocation(new ResourceLocation(Carz.MODID, "basic"), "type=" + this.getName());
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    public static void registerBlocks(){
        for(EnumBasicBlocks block : values()){
            BlockBasic.staticTypeForCreatingThisBlockTheHackyWay = block;
            block.instance = new BlockBasic(block).register();
        }
        BlockBasic.staticTypeForCreatingThisBlockTheHackyWay = null;
    }

}
