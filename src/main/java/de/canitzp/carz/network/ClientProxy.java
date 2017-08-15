package de.canitzp.carz.network;

import de.canitzp.carz.Registry;
import de.canitzp.carz.blocks.BlockBase;
import de.canitzp.carz.items.ItemBase;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * @author canitzp
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        for (BlockBase block : Registry.BLOCKS_FOR_REGISTERING) {
            block.registerClientInit();
        }
        for(ItemBase item : Registry.ITEMS_FOR_REGISTERING){
            item.registerClientInit();
        }
    }

}
