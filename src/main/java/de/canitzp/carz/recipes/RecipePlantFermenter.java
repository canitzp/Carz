package de.canitzp.carz.recipes;

import de.canitzp.carz.Registry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class RecipePlantFermenter {

    public static RecipePlantFermenter DEFAULT = new RecipePlantFermenter(new ItemStack(Items.APPLE, 1), ItemStack.EMPTY, new FluidStack(Registry.fluidBioFuel, 1000));

    private ItemStack input = ItemStack.EMPTY, output = ItemStack.EMPTY;
    @Nullable
    private FluidStack outputFluid;
    private int produceTicks = 200;

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

    public RecipePlantFermenter setTicksNeeded(int ticks){
        this.produceTicks = ticks;
        return this;
    }

    public int getProduceTicks() {
        return produceTicks;
    }
}
