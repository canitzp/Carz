package de.canitzp.carz.integration.opencomputers;

import de.canitzp.carz.api.oc.DriverCardLinkedDriver;
import de.canitzp.carz.integration.IInternalIntegration;
import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * @author canitzp
 */
public class IntegrationOC implements IInternalIntegration{

    public static final String MODID = "opencomputers";

    @Override
    public void init(FMLInitializationEvent event){
        Driver.add(new DriverCardLinkedDriver());
    }

}
