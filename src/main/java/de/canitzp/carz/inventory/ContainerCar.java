package de.canitzp.carz.inventory;

import de.canitzp.carz.api.EntityRenderedBase;
import de.canitzp.carz.api.Safety;
import de.canitzp.carz.items.ItemKey;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class ContainerCar extends Container {

    public ContainerCar(EntityPlayer player, int entityId) {
        EntityRenderedBase car = Safety.getEntity(player.world, entityId, EntityRenderedBase.class);

        IItemHandler inv = car.getInventory(null);
        if(inv != null){
            this.addSlotToContainer(new SlotItemHandler(inv, 0, 80, 34){
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ItemKey;
                }
            });
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new SlotItemHandler(inv, j + i * 9 + 1, 8 + j * 18, 54 + i * 18));
                }
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 198));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
