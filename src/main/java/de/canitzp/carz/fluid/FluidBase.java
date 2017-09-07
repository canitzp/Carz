package de.canitzp.carz.fluid;

import de.canitzp.carz.Carz;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author canitzp
 */
public class FluidBase extends Fluid {

    private int color;

    public FluidBase(String name, int color){
        super(name, new ResourceLocation(Carz.MODID, "fluids/".concat(name).concat("_still")), new ResourceLocation(Carz.MODID, "fluids/".concat(name).concat("_flowing")));
        this.color = color;
    }

    @Override
    public String getUnlocalizedName(FluidStack stack) {
        return this.getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName() {
        return "fluid." + Carz.MODID + ":" + this.fluidName + ".name";
    }

    @Override
    public boolean doesVaporize(FluidStack fluidStack) {
        return false;
    }

    @Override
    public int getColor() {
        return this.color;
    }
}
