package de.canitzp.carz.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public interface IColoredBlock {

    @SideOnly(Side.CLIENT)
    int getBlockColor(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex);

    @SideOnly(Side.CLIENT)
    int getItemColor(ItemStack stack, int tintIndex);

}
