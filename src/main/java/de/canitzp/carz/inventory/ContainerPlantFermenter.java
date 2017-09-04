package de.canitzp.carz.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * @author canitzp
 */
public class ContainerPlantFermenter extends Container {

    public ContainerPlantFermenter(EntityPlayer player, int x, int y, int z) {
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
