package de.canitzp.carz.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class RecipePlantFermenter {

    private ItemStack input = ItemStack.EMPTY, output = ItemStack.EMPTY;
    @Nullable
    private FluidStack outputFluid;

    public RecipePlantFermenter(ItemStack input, ItemStack output, @Nullable FluidStack outputFluid) {
        this.input = input;
        this.output = output;
        this.outputFluid = outputFluid;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Nullable
    public FluidStack getOutputFluid() {
        return outputFluid;
    }
}
