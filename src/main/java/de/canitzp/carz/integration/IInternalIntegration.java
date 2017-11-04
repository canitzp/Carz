package de.canitzp.carz.integration;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author canitzp
 */
public interface IInternalIntegration {

    default void preInit(FMLPreInitializationEvent event){}

    default void init(FMLInitializationEvent event){}

    default void postInit(FMLPostInitializationEvent event){}

}
