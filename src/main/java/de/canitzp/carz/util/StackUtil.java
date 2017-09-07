package de.canitzp.carz.util;

import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class StackUtil {

    public static boolean canMerge(ItemStack first, ItemStack second){
        return first.isItemEqual(second) && ItemStack.areItemStackTagsEqual(first, second) && first.getCount() + second.getCount() <= Math.min(second.getMaxStackSize(), first.getMaxStackSize());
    }

}
