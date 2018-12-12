package de.canitzp.carz.compat;

import de.canitzp.carz.tile.TileBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber // for a static initializing without calling this class for myself
public class Compat {

    private static List<ICompat> compatsLoaded = new ArrayList<>();

    static {
        if(Loader.isModLoaded("commoncapabilities")){
            //compatsLoaded.add(new CommonCapabilities());
        }
    }

    @Nullable
    public static <T> T getCapability(@Nonnull TileBase tile, @Nonnull Capability<T> capability, @Nullable EnumFacing facing){
        for(ICompat compat : compatsLoaded){
            T t = compat.getCapability(tile, capability, facing);
            if(t != null){
                return t;
            }
        }
        return null;
    }

}
