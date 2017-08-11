package de.canitzp.carz;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Statistics collector class
 * @author MisterErwin
 */
public class CarzStats {
    public static StatBase ENTITY_HIT_COUNT;
    public static StatBase ENTITY_HIT_DAMAGE;

    static void registerStats() {
        ENTITY_HIT_COUNT = new StatBasic("stat.entity_hit.count",
                new TextComponentTranslation("stat.entity_hit.count")).registerStat();
        ENTITY_HIT_DAMAGE = new StatBasic("stat.entity_hit.dmg",
                new TextComponentTranslation("stat.entity_hit.dmg")).registerStat();

    }
}
