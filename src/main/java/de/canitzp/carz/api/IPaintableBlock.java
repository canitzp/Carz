package de.canitzp.carz.api;

import de.canitzp.carz.client.PixelMesh;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public interface IPaintableBlock {

    void clickedWithPainter(World world, BlockPos pos, EntityPlayer player, IBlockState state, EnumHand hand, EnumFacing facing, PixelMesh mesh, float hitX, float hitY, float hitZ);

    boolean hitWithPainter(World world, BlockPos pos, EntityPlayer player, IBlockState state, ItemStack stack);

}
