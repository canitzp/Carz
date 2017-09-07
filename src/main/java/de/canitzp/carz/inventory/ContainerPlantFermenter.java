package de.canitzp.carz.inventory;

import de.canitzp.carz.api.CarzAPI;
import de.canitzp.carz.api.Safety;
import de.canitzp.carz.tile.TilePlantFermenter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class ContainerPlantFermenter extends Container {

    public ContainerPlantFermenter(EntityPlayer player, int x, int y, int z) {
        TilePlantFermenter tile = Safety.getTile(player.getEntityWorld(), new BlockPos(x, y, z), TilePlantFermenter.class);
        IInventory inventory = tile.inventory.getInv();
        this.addSlotToContainer(new PlantSlot(inventory, 0, 20, 23));
        this.addSlotToContainer(new PlantSlot(inventory, 1, 38, 23));
        this.addSlotToContainer(new PlantSlot(inventory, 2, 20, 41));
        this.addSlotToContainer(new PlantSlot(inventory, 3, 38, 41));
        this.addSlotToContainer(new Slot(inventory, 4, 81, 32) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        this.addSlotToContainer(new FluidContainerSlot(inventory, 5, 129, 57));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        // TODO
        return ItemStack.EMPTY;
    }

    private class PlantSlot extends Slot{
        public PlantSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return CarzAPI.isStackValidPlant(stack);
        }
    }

    private class FluidContainerSlot extends Slot{
        public FluidContainerSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        }
    }
}
