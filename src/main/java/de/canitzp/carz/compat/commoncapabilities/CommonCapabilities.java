/*package de.canitzp.carz.compat.commoncapabilities;

import de.canitzp.carz.compat.ICompat;
import de.canitzp.carz.tile.TileBase;
import de.canitzp.carz.tile.TilePlantFermenter;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

/**
 * @author canitzp
 */
/*
public class CommonCapabilities implements ICompat{

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull TileBase tile, @Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(tile instanceof TilePlantFermenter && capability == WorkerConfig.CAPABILITY){
            return WorkerConfig.CAPABILITY.cast(new IWorker() {
                @Override
                public boolean hasWork() {
                    return ((TilePlantFermenter) tile).ticksLeft > 0;
                }

                @Override
                public boolean canWork() {
                    return this.hasWork();
                }
            });
        }
        return null;
    }
}
*/