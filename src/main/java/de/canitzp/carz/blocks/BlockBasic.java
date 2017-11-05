package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.EnumCarParts;
import de.canitzp.carz.items.ItemBlockCustom;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class BlockBasic extends BlockBase<BlockBasic> {

    static EnumBasicBlocks staticTypeForCreatingThisBlockTheHackyWay;
    private EnumBasicBlocks type;

    public BlockBasic(EnumBasicBlocks type) {
        super(Material.ROCK);
        this.type = type;
        this.setRegistryName(Carz.MODID, "basic_" + type.getName().toLowerCase());
        this.setDefaultState(type.getDefaultState(this.getBlockState().getBaseState()));
    }

    private EnumBasicBlocks getType(){
        return this.type != null ? this.type : staticTypeForCreatingThisBlockTheHackyWay;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        ModelResourceLocation loc = this.type.getModelResourceLocation();
        ModelLoader.setCustomStateMapper(this, block -> {
            Map<IBlockState, ModelResourceLocation> map = new HashMap<>();
            for(int meta = 0; meta < 15; meta++){
                map.put(block.getStateFromMeta(meta), loc);
            }
            return map;
        });
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, loc);
    }

    @Nonnull
    @Override
    public Material getMaterial(IBlockState state) {
        return type.getMaterial(state);
    }

    @Nonnull
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return type.getColor(state, worldIn, pos);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return getType().isFullBlock(state);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return this.isOpaqueCube(state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return type.getBoundingBox(state, source, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        for(AxisAlignedBB hitBox : type.getHitBoxes(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_)){
            addCollisionBoxToList(pos, entityBox, collidingBoxes, hitBox);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return this.getType().createBlockState(this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.type.parseState(meta, this.getDefaultState());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return this.type.parseMeta(state);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.type.getPlacementState(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer, this.getStateFromMeta(meta));
    }
}
