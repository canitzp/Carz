package de.canitzp.amcm;

import net.minecraft.client.renderer.BufferBuilder;

/**
 * @author canitzp
 */
public interface IAMCMShapes<T extends IAMCMShapes> {

    T render(float scale);

    void refreshResources();

}
