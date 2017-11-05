package de.canitzp.carz.api;

import de.canitzp.carz.client.PixelMesh;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author canitzp
 */
public interface IColorableCar {

    @SideOnly(Side.CLIENT)
    ResourceLocation getOverlayTexture();

    @SideOnly(Side.CLIENT)
    default int getCurrentColor() {
        return 0xD70404; // nice red
    }

    default boolean shouldRecalculateTexture() {
        return false;
    }

    default void setRecalculated() {}

    @Nullable
    default PixelMesh getCurrentMesh(){
        return null;
    }

    default List<Pair<Integer, Integer>> getPixelMeshCoordiantes(){
        return Collections.emptyList();
    }

}
