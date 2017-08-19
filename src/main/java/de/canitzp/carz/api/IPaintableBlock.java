package de.canitzp.carz.api;

import de.canitzp.carz.client.PixelMesh;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public interface IPaintableBlock {

    void clickedWithPainter(World world, BlockPos pos, IBlockState state, PixelMesh mesh);

}
