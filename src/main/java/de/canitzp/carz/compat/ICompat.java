package de.canitzp.carz.compat;

import de.canitzp.carz.tile.TileBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public interface ICompat {

    @Nullable
    default <T> T getCapability(@Nonnull TileBase tile, @Nonnull Capability<T> capability, @Nullable EnumFacing facing){
        return null;
    }

}
