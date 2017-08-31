package de.canitzp.carz.config;

import de.canitzp.carz.Carz;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
@Config(modid = Carz.MODID, name = Carz.MODNAME)
@Config.LangKey("carz:config.general")
public class ConfigCarz{

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.getModID().equals(Carz.MODID)){
            ConfigManager.sync(Carz.MODID, Config.Type.INSTANCE);
        }
    }

    @Config(modid = Carz.MODID, name = Carz.MODNAME, category = "generation")
    @Config.LangKey("carz:config.generation")
    public static class Generation{

        @Config.Name("Should OilChunks spawn")
        @Config.RequiresMcRestart
        public static boolean OIL_CHUNKS_ACTIVE = true;

        @Config.Name("Chance of OilChunk generation")
        @Config.Comment({"This defines how often a oil chunk should appear. A lower value means a bigger chance", "Default: 25"})
        @Config.RangeInt(min = 0, max = 50)
        public static int OIL_CHUNK_CHANCE = 25;

        @Config.Name("Minimum Oil per chunk in mB")
        @Config.Comment("Default: 100000")
        @Config.RangeInt(min = 1000, max = 10000000)
        public static int OIL_MIN = 100000;

        @Config.Name("Maximum Oil per chunk in mB")
        @Config.Comment("Default: 10000000")
        @Config.RangeInt(min = 1000, max = 100000000)
        public static int OIL_MAX = 10000000;

    }

}
