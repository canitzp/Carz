package de.canitzp.carz;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Launching " + MODNAME + " v" + MODVERSION);
        Registry.preInit(event);
    }

}
