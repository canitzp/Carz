package de.canitzp.carz.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

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
    private Material material;
    private MapColor color;
    private AxisAlignedBB boundingBox;

    EnumBasicBlocks(Material material, MapColor color, AxisAlignedBB boundingBox){
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
    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    public static void registerBlocks(){
        for(EnumBasicBlocks block : values()){
            block.instance = new BlockBasic(block).register();
        }
    }

}
