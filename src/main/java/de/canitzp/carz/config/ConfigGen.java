package de.canitzp.carz.config;

import de.canitzp.carz.Carz;
import net.minecraftforge.common.config.Config;

/**
 * @author canitzp
 */
@Config(modid = Carz.MODID, name = Carz.MODNAME, category = "generation")
public class ConfigGen {

    @Config.Name("oil_chunk_chance")
    @Config.Comment({"This defines how often a oil chunk should appear. A lower value means a bigger chance"})
    @Config.RangeInt(min = 0, max = 50)
    public static int OIL_CHUNK_CHANCE = 20;

}
