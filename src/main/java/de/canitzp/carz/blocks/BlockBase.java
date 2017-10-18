package de.canitzp.carz.blocks;

import de.canitzp.carz.Registry;
import de.canitzp.carz.items.ItemBlockCustom;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
public abstract class BlockBase<T extends BlockBase> extends Block {

    public BlockBase(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public BlockBase(Material materialIn) {
        super(materialIn);
    }

    @SuppressWarnings("unchecked")
    public T register() {
        Registry.BLOCKS_FOR_REGISTERING.add(this);
        return (T) this;
    }

    @SideOnly(Side.CLIENT)
    public void registerClient() {
    }

    @SideOnly(Side.CLIENT)
    public void registerClientInit() {
    }

    @SuppressWarnings("ConstantConditions")
    public ItemBlock getItemBlock() {
        return (ItemBlock) new ItemBlockCustom(this).setRegistryName(this.getRegistryName());
    }

    public boolean canBePlaced(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, EnumHand hand, float hitX, float hitY, float hitZ){
        return true;
    }

    @Nonnull
    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
        return Registry.TAB_GENERAL;
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "tile." + this.getRegistryName().toString();
    }
}
