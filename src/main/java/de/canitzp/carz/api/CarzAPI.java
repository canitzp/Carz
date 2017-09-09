package de.canitzp.carz.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class CarzAPI {

    private static NonNullList<ItemStack> VALID_PLANT_ITEMS = NonNullList.create();

    static {
        addPlant(new ItemStack(Items.WHEAT));
    }

    public static boolean addPlant(@Nonnull ItemStack stack) {
        return !VALID_PLANT_ITEMS.contains(stack) && VALID_PLANT_ITEMS.add(stack);
    }

    public static boolean isStackValidPlant(@Nonnull ItemStack stack){
        if(!stack.isEmpty()){
            Block block = Block.getBlockFromItem(stack.getItem());
            return VALID_PLANT_ITEMS.contains(stack) || stack.getItem() instanceof ItemFood || block instanceof BlockBush || block instanceof IPlantable || block instanceof IShearable;
        }
        return false;
    }

}
