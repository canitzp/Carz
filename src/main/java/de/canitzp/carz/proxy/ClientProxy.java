package de.canitzp.carz.proxy;

import de.canitzp.carz.Carz;
import de.canitzp.carz.entity.EntityCar;
import de.canitzp.carz.entity.car.EntityTestCar;
import de.canitzp.carz.entity.renderer.RenderCar;
import de.canitzp.carz.entity.renderer.RenderTestCar;
import de.canitzp.carz.listener.KeyListener;
import de.canitzp.carz.listener.TickingClientListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author MisterErwin
 */
public class ClientProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        Carz.LOG.info("Registering Car renderer");
        RenderingRegistry.registerEntityRenderingHandler(EntityCar.class, RenderCar::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTestCar.class, RenderTestCar::new);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MinecraftForge.EVENT_BUS.register(new KeyListener());
        MinecraftForge.EVENT_BUS.register(new TickingClientListener());

    }

    @Override
    public void postInit(FMLPostInitializationEvent  e) {
        super.postInit(e);
    }
}
