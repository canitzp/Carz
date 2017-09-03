package de.canitzp.carz.items;

import de.canitzp.carz.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class ItemBlockCustom extends ItemBlock {

    public ItemBlockCustom(BlockBase block) {
        super(block);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, @Nullable BlockPos pos, @Nullable EnumHand hand, @Nullable EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(pos != null && hand != null && facing != null){
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (!block.isReplaceable(world, pos)) {
                pos = pos.offset(facing);
            }
            ItemStack heldItem = player.getHeldItem(hand);
            if (!heldItem.isEmpty() && player.canPlayerEdit(pos, facing, heldItem) && world.mayPlace(this.block, pos, false, facing, null)) {
                int i = this.getMetadata(heldItem.getMetadata());
                IBlockState stateForPlacement = this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, i, player, hand);
                if(((BlockBase) this.block).canBePlaced(world, pos, stateForPlacement, player, facing, hand, hitX, hitY, hitZ)){
                    if (placeBlockAt(heldItem, player, world, pos, facing, hitX, hitY, hitZ, stateForPlacement)) {
                        stateForPlacement = world.getBlockState(pos);
                        SoundType soundtype = stateForPlacement.getBlock().getSoundType(stateForPlacement, world, pos, player);
                        world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        heldItem.shrink(1);
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.FAIL;
    }
}
