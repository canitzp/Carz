package de.canitzp.carz.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * @author canitzp
 */
public class CustomItemBlock extends ItemBlock{

    public CustomItemBlock(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
