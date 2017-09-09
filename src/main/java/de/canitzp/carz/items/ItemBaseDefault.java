package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ItemBaseDefault extends ItemBase<ItemBaseDefault> {

    public ItemBaseDefault(String name){
        this.setRegistryName(new ResourceLocation(Carz.MODID, name));
    }

}
