package de.canitzp.carz;

import de.canitzp.carz.packet.MessageCarSpeed;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
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

    public SimpleNetworkWrapper networkWrapper;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Launching " + MODNAME + " v" + MODVERSION);
        Registry.preInit(event);

        this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("carz");
//        this.networkWrapper.registerMessage(MessageCarInput.class, MessageCarInput.class, 0, Side.SERVER);
        this.networkWrapper.registerMessage(MessageCarSpeed.class, MessageCarSpeed.class, 0, Side.SERVER);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        CarzStats.registerStats();
    }


}
