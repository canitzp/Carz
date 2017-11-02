package de.canitzp.carz;

import de.canitzp.carz.config.ConfigCarz;
import de.canitzp.carz.gen.OilChunkGen;
import de.canitzp.carz.gen.WorldGenRubberTree;
import de.canitzp.carz.integration.IntegrationHandler;
import de.canitzp.carz.network.CommonProxy;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenTrees;
import de.canitzp.voxeler.Voxeler;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.util.Objects;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
@Mod(modid = Carz.MODID, name = Carz.MODNAME, version = Carz.MODVERSION)
public class Carz {

    public static final String MODID = "carz";
    public static final String MODNAME = "Carz";
    public static final String MODVERSION = "@VERSION@";
    public static final String BUILDDATE = "@BUILD_DATE@";
    public static final Logger LOG = LogManager.getFormatterLogger(MODNAME);

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.Instance(MODID)
    public static Carz carz;

    public static boolean RENDER_DEBUG = false;

    @SidedProxy(clientSide = "de.canitzp.carz.network.ClientProxy", serverSide = "de.canitzp.carz.network.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Launching " + MODNAME + " v" + MODVERSION);
        IntegrationHandler.loadIntegrations();
        Registry.preInit(event);
        LOG.info("Creating Network issues");
        NetworkHandler.preInit(event);
        LOG.info("Let the proxy out Part I");
        proxy.preInit(event);
        IntegrationHandler.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Registry.init(event);
        if (ConfigCarz.Generation.OIL_CHUNKS_ACTIVE) {
            GameRegistry.registerWorldGenerator(new OilChunkGen(), 10);
        }
        if(ConfigCarz.Generation.RUBBERTREES_ACTIVE){
            GameRegistry.registerWorldGenerator(new WorldGenRubberTree(), 1);
        }
        proxy.init(event);
        IntegrationHandler.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        IntegrationHandler.postInit(event);
    }

}
