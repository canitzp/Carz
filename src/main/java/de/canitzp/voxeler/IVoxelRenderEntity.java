package de.canitzp.voxeler;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public interface IVoxelRenderEntity<T extends VoxelBase> {

    @Nonnull
    T getVoxelModel();

    @Nullable
    ResourceLocation getTexture();

    void setupGL(double x, double y, double z, float entityYaw, float partialTicks);

}
