package de.canitzp.carz;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.network.CommonProxy;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author canitzp
 */
@Mod(modid = Carz.MODID, name = Carz.MODNAME, version = Carz.MODVERSION)
public class Carz {

    public static final String MODID = "carz";
    public static final String MODNAME = "Carz";
    public static final String MODVERSION = "%VERSION%";
    public static final Logger LOG = LogManager.getFormatterLogger(MODNAME);

    @Mod.Instance(MODID)
    public static Carz carz;

    @SidedProxy(clientSide = "de.canitzp.carz.network.ClientProxy", serverSide = "de.canitzp.carz.network.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Launching " + MODNAME + " v" + MODVERSION);
        Registry.preInit(event);
        LOG.info("Creating Network issues");
        NetworkHandler.preInit(event);
        LOG.info("Let the proxy out Part I");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
