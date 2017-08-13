package de.canitzp.carz.items;

import de.canitzp.carz.blocks.BlockSign;
import de.canitzp.carz.blocks.EnumSigns;
import de.canitzp.carz.tile.TileSign;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class ItemBlockSign extends ItemBlock {

    public ItemBlockSign(Block block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
        this.setHasSubtypes(true);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, @Nullable BlockPos pos, @Nullable EnumHand hand, @Nullable EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty() && player.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(this.block, pos, false, facing, player)) {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState state = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
            if(stack.hasTagCompound() && stack.getTagCompound().hasKey("SignValue", Constants.NBT.TAG_INT)){
                state = state.withProperty(BlockSign.SIGN_TYPE, EnumSigns.values()[stack.getTagCompound().getInteger("SignValue")]);
            }
            if (placeBlockAt(stack, player, worldIn, pos, facing, hitX, hitY, hitZ, state)) {
                state = worldIn.getBlockState(pos);
                SoundType soundtype = state.getBlock().getSoundType(state, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if(stack.hasTagCompound()){
            for(EnumSigns sign : EnumSigns.values()){
                NBTTagCompound nbt = stack.getTagCompound();
                if(nbt.hasKey("SignValue", Constants.NBT.TAG_INT) && nbt.getInteger("SignValue") == sign.ordinal()){
                    return "tile.sign." + sign.getName();
                }
            }
        }
        return super.getUnlocalizedName(stack);
    }

    @Override
    public int getDamage(ItemStack stack) {
        if(stack.hasTagCompound()){
            return stack.getTagCompound().getInteger("SignValue");
        }
        return super.getDamage(stack);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
