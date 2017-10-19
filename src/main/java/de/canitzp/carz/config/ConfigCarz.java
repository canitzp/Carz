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
@Config(modid = Carz.MODID)
@Config.LangKey("carz:config.general")
public class ConfigCarz{

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.getModID().equals(Carz.MODID)){
            ConfigManager.sync(Carz.MODID, Config.Type.INSTANCE);
        }
    }

    @Config(modid = Carz.MODID, category = "generation")
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

        @Config.Name("Should Rubber Trees spawn")
        @Config.RequiresMcRestart
        public static boolean RUBBERTREES_ACTIVE = true;

        @Config.Name("Rubber Tree population")
        @Config.Comment({"This defines in how much chunks a Rubber Tree should spawn. 1 = 1 per chunk, 4 = 1 per 4 chunks", "Default: 5"})
        @Config.RangeInt(min = 1)
        public static int RUBBERTREES_POPULATION = 5;

        @Config.Name("Rubber Tree biome blacklist")
        @Config.Comment({"Rubber trees won't spawn in biomes, that are listed with their correct name in here.",
                "Vanilla biomes: Ocean, Plains, Desert, Extreme Hills, Forest, Taiga, Swampland, River, Hell, The End,",
                "FrozenOcean, FrozenRiver, Ice Plains, Ice Mountains, MushroomIsland, MushroomIslandShore, Beach,",
                "DesertHills, ForestHills, TaigaHills, Extreme Hills Edge, Jungle, JungleHills, JungleEdge, Deep Ocean,",
                "Stone Beach, Cold Beach, Birch Forest, Birch Forest Hills, Roofed Forest, Cold Taiga, Cold Taiga Hills,",
                "Mega Taiga, Mega Taiga Hills, Extreme Hills+, Savanna, Savanna Plateau, Mesa, Mesa Plateau F, Mesa Plateau,",
                "The Void, Sunflower Plains, Desert M, Extreme Hills M, Flower Forest, Taiga M, Swampland M, Ice Plains Spikes,",
                "Jungle M, JungleEdge M, Birch Forest M, Birch Forest Hills M, Roofed Forest M, Cold Taiga M, Mega Spruce Taiga,",
                "Redwood Taiga Hills M, Extreme Hills+ M, Savanna M, Savanna Plateau M, Mesa (Bryce), Mesa Plateau F M, Mesa Plateau M",
                "",
                "Default: Ocean, Desert, Extreme Hills, Hell, The End, FrozenOcean, FrozenRiver, Ice Plains, Ice Mountains, DesertHills,",
                "Extreme Hills Edge, Deep Ocean, Cold Beach, Cold Taiga, Cold Taiga Hills, Extreme Hills+, Savanna, Savanna Plateau, Mesa, Mesa Plateau F,",
                "Mesa Plateau, The Void, Desert M, Extreme Hills M, Ice Plains Spikes, Cold Taiga M, Extreme Hills+ M,",
                "Savanna M, Savanna Plateau M, Mesa (Bryce), Mesa Plateau F M, Mesa Plateau M"})
        @Config.RequiresMcRestart
        public static String[] RUBBERTREE_BIOME_BLACKLIST = new String[]{"Ocean", "Desert", "Extreme Hills", "Hell", "The End", "FrozenOcean",
                "FrozenRiver", "Ice Plains", "Ice Mountains", "DesertHills", "Extreme Hills Edge", "Deep Ocean", "Cold Beach", "Cold Taiga",
                "Cold Taiga Hills", "Extreme Hills+", "Savanna", "Savanna Plateau", "Mesa", "Mesa Plateau F", "Mesa Plateau", "The Void",
                "Desert M", "Extreme Hills M", "Ice Plains Spikes", "Cold Taiga M", "Extreme Hills+ M", "Savanna M", "Savanna Plateau M",
                "Mesa (Bryce)", "Mesa Plateau F M", "Mesa Plateau M"};

    }

}
