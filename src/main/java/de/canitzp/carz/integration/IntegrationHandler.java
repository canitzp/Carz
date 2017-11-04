package de.canitzp.carz.integration;

import de.canitzp.carz.Carz;
import de.canitzp.carz.integration.opencomputers.IntegrationOC;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class IntegrationHandler {

    private static boolean loadingComplete = false;
    private static List<IntegrationWrapper> integrationWrapper = new ArrayList<>();
    private static List<IInternalIntegration> integrations = new ArrayList<>();

    static {
        integrationWrapper.add(new IntegrationWrapper(IntegrationOC.MODID, IntegrationOC.class));
    }

    public static void loadIntegrations(){
        if(!loadingComplete){
            for(IntegrationWrapper wrapper : integrationWrapper){
                if(Loader.isModLoaded(wrapper.getModid())){
                    try {
                        IInternalIntegration instance = wrapper.getIntegrationClass().newInstance();
                        integrations.add(instance);
                        Carz.LOG.info("Loading integration for: " + wrapper.getModid());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            loadingComplete = true;
        } else {
            throw new RuntimeException("The Integration can only be initialized once!");
        }
    }

    private static void checkLoadingState(){
        if(!loadingComplete){
            throw new RuntimeException("Integrations can't be called without even loading these!");
        }
    }

    public static void preInit(FMLPreInitializationEvent event){
        checkLoadingState();
        for(IInternalIntegration integration : integrations){
            integration.preInit(event);
        }
    }

    public static void init(FMLInitializationEvent event){
        checkLoadingState();
        for(IInternalIntegration integration : integrations){
            integration.init(event);
        }
    }

    public static void postInit(FMLPostInitializationEvent event){
        checkLoadingState();
        for(IInternalIntegration integration : integrations){
            integration.postInit(event);
        }
    }

}
