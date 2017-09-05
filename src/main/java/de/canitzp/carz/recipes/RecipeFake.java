package de.canitzp.carz.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Actually since forge wants a IRecipe for registering
 * and I want to use the nice .json recipes
 * without writing to much myself, I need this class.
 * The Forge team would hate me.
 * @author canitzp
 */
public class RecipeFake extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
