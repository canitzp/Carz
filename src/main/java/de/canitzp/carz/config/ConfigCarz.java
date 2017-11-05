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
@Config.LangKey("carz:config.generation")
public class ConfigCarz{

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.getModID().equals(Carz.MODID)){
            ConfigManager.sync(Carz.MODID, Config.Type.INSTANCE);
        }
    }

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

    @Config.Name("Rubber Tree biome whitelist")
    @Config.Comment({"Rubber trees only spawn in biomes, that are listed with their correct name in here.",
            "Vanilla biomes: ocean, plains, desert, extreme_hills, forest, taiga, swampland, river, hell, sky, frozen_ocean, frozen_river,",
            "ice_flats, ice_mountains, mushroom_island, mushroom_island_shore, beaches, desert_hills, forest_hills, taiga_hills,",
            "smaller_extreme_hills, jungle, jungle_hills, jungle_edge, deep_ocean, stone_beach, cold_beach, birch_forest, birch_forest_hills,",
            "roofed_forest, taiga_cold, taiga_cold_hills, redwood_taiga, redwood_taiga_hills, extreme_hills_with_trees, savanna, savanna_rock,",
            "mesa, mesa_rock, mesa_clear_rock, void, mutated_plains, mutated_desert, mutated_extreme_hills, mutated_forest, mutated_taiga,",
            "mutated_swampland, mutated_ice_flats, mutated_jungle, mutated_jungle_edge, mutated_birch_forest, mutated_birch_forest_hills,",
            "mutated_roofed_forest, mutated_taiga_cold, mutated_redwood_taiga, mutated_redwood_taiga_hills, mutated_extreme_hills_with_trees,",
            "mutated_savanna, mutated_savanna_rock, mutated_mesa, mutated_mesa_rock, mutated_mesa_clear_rock",
            "",
            "Default: plains, forest, taiga, swampland, ",
            "forest_hills, jungle, jungle_hills, jungle_edge, birch_forest, birch_forest_hills, roofed_forest, redwood_taiga,",
            "redwood_taiga_hills, extreme_hills_with_trees, mutated_plains, mutated_forest, mutated_taiga, mutated_swampland, mutated_jungle,",
            "mutated_jungle_edge, mutated_birch_forest, mutated_birch_forest_hills, mutated_roofed_forest, mutated_redwood_taiga,",
            "mutated_redwood_taiga_hills, mutated_extreme_hills_with_trees"})
    @Config.RequiresMcRestart
    public static String[] RUBBERTREE_BIOME_WHITELIST = new String[]{
            "plains", "forest", "taiga", "swampland", "forest_hills", "taiga_hills", "smaller_extreme_hills", "jungle", "jungle_hills",
            "jungle_edge", "birch_forest", "birch_forest_hills", "roofed_forest", "redwood_taiga", "redwood_taiga_hills",
            "extreme_hills_with_trees", "mutated_plains", "mutated_forest", "mutated_taiga", "mutated_swampland", "mutated_jungle",
            "mutated_jungle_edge", "mutated_birch_forest", "mutated_birch_forest_hills", "mutated_roofed_forest", "mutated_redwood_taiga",
            "mutated_redwood_taiga_hills", "mutated_extreme_hills_with_trees"
    };

}
