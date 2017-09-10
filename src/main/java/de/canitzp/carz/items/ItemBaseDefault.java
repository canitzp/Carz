package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ItemBaseDefault<T extends ItemBaseDefault> extends ItemBase<T> {

    public ItemBaseDefault(String name){
        this.setRegistryName(new ResourceLocation(Carz.MODID, name));
    }

}
