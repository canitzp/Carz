package de.canitzp.carz;

import de.canitzp.carz.entity.EntityCar;
import de.canitzp.carz.client.renderer.RenderCar;
import de.canitzp.carz.entity.EntityTestCar;
import de.canitzp.carz.client.renderer.RenderTestCar;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
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
    public void preInit(FMLPreInitializationEvent event){
        LOG.info("Launching " + MODNAME + " v" + MODVERSION);
        LOG.info("Registering Cars");
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "car"), EntityCar.class, "car", 0, carz, 64, 5, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Carz.MODID, "testcar"), EntityTestCar.class, "testcar", 1, Carz.carz, 64, 5, true);
        if(event.getSide().isClient()){
            LOG.info("Registering Car renderer");
            RenderingRegistry.registerEntityRenderingHandler(EntityCar.class, RenderCar::new);
            RenderingRegistry.registerEntityRenderingHandler(EntityTestCar.class, RenderTestCar::new);
        }
    }

}
