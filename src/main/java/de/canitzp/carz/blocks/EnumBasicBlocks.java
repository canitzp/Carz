package de.canitzp.carz.blocks;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * The maximum amount of blocks here is 16!
 * @author canitzp
 */
public enum EnumBasicBlocks implements IStringSerializable {

    PYLON_EUROPEAN(Material.CIRCUITS, MapColor.ORANGE_STAINED_HARDENED_CLAY, Block.FULL_BLOCK_AABB){
        @Override
        public boolean isFullBlock(IBlockState state) {
            return false;
        }
    };

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

}
