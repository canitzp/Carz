package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.EnumCarParts;
import de.canitzp.carz.items.ItemBlockCustom;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class BlockBasic extends BlockBase<BlockBasic> {

    public static final PropertyEnum<EnumBasicBlocks> TYPE = PropertyEnum.create("type", EnumBasicBlocks.class);

    public BlockBasic() {
        super(Material.ROCK);
        this.setRegistryName(Carz.MODID, "basic");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBasicBlocks.PYLON_EUROPEAN));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        for(EnumBasicBlocks block : EnumBasicBlocks.values()){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), TYPE.getName() + "=" + block.getName()));
        }
    }

    @Nonnull
    @Override
    public Material getMaterial(IBlockState state) {
        return state.getValue(TYPE).getMaterial(state);
    }

    @Nonnull
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(TYPE).getColor(state, worldIn, pos);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(TYPE).isFullBlock(state);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return this.isOpaqueCube(state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(TYPE).getBoundingBox(state, source, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        for(AxisAlignedBB hitBox : state.getValue(TYPE).getHitBoxes(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_)){
            addCollisionBoxToList(pos, entityBox, collidingBoxes, hitBox);
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumBasicBlocks.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list){
        for(EnumBasicBlocks basic : EnumBasicBlocks.values()){
            ItemStack stack = new ItemStack(this);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setShort("BlockIndex", (short) basic.ordinal());
            stack.setTagCompound(nbt);
            list.add(stack);
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBasic(this);
    }

    public static class ItemBlockBasic extends ItemBlockCustom{

        public ItemBlockBasic(BlockBase block) {
            super(block);
            this.setRegistryName(block.getRegistryName());
        }

        @Nonnull
        @Override
        public String getUnlocalizedName(ItemStack stack) {
            NBTTagCompound nbt = stack.getTagCompound();
            if(nbt != null && nbt.hasKey("BlockIndex", Constants.NBT.TAG_SHORT)){
                EnumBasicBlocks index = EnumBasicBlocks.values()[nbt.getShort("BlockIndex")];
                return super.getUnlocalizedName(stack) + "_" + index.getName().toLowerCase();
            }
            return super.getUnlocalizedName(stack);
        }
    }
}
