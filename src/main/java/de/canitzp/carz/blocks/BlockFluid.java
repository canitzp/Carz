package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * @author canitzp
 */
public class BlockFluid extends BlockFluidClassic {

    public BlockFluid(Fluid fluid) {
        super(fluid, Material.WATER);
        this.setRegistryName(Carz.MODID, fluidName.toLowerCase());
        this.setTranslationKey(this.getRegistryName().toString());
    }



}
