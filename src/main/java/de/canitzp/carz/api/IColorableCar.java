package de.canitzp.carz.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public interface IColorableCar {

    @SideOnly(Side.CLIENT)
    ResourceLocation getOverlayTexture();

    @SideOnly(Side.CLIENT)
    default int getCurrentColor(){
        return 0xD70404; // nice red
    }

    default boolean shouldRecalculateTexture(){
        return false;
    }

    default void setRecalculated(){}

}
