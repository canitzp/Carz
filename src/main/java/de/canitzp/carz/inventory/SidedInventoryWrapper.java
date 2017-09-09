package de.canitzp.carz.inventory;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class SidedInventoryWrapper implements IItemHandlerModifiable{

    private SidedInventory inventory;
    private EnumFacing side;

    public SidedInventoryWrapper(SidedInventory inv, EnumFacing side) {
        this.inventory = inv;
        this.side = side;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.inventory.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.inventory.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!stack.isEmpty()){
            ItemStack stackInSlot = this.getStackInSlot(slot);
            int m;
            if (!stackInSlot.isEmpty()) {
                if(ItemHandlerHelper.canItemStacksStack(stack, stackInSlot) && this.inventory.isItemValidForSlot(slot, stack) && this.inventory.canInsertItem(slot, stack, this.side)){
                    m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();
                    if (stack.getCount() <= m) {
                        if (!simulate) {
                            ItemStack copy = stack.copy();
                            copy.grow(stackInSlot.getCount());
                            this.inventory.setInventorySlotContents(slot, copy);
                            this.inventory.markDirty();
                        }

                        return ItemStack.EMPTY;
                    } else {
                        // copy the stack to not modify the original one
                        stack = stack.copy();
                        if (!simulate) {
                            ItemStack copy = stack.splitStack(m);
                            copy.grow(stackInSlot.getCount());
                            this.inventory.setInventorySlotContents(slot, copy);
                            this.inventory.markDirty();
                            return stack;
                        } else {
                            stack.shrink(m);
                            return stack;
                        }
                    }
                } else {
                    return stack;
                }
            } else {
                if (this.inventory.isItemValidForSlot(slot, stack) && this.inventory.canInsertItem(slot, stack, this.side)){
                    m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
                    if (m < stack.getCount()) {
                        // copy the stack to not modify the original one
                        stack = stack.copy();
                        if (!simulate) {
                            this.inventory.setInventorySlotContents(slot, stack.splitStack(m));
                            this.inventory.markDirty();
                            return stack;
                        } else {
                            stack.shrink(m);
                            return stack;
                        }
                    } else {
                        if (!simulate) {
                            this.inventory.setInventorySlotContents(slot, stack);
                            this.inventory.markDirty();
                        }
                        return ItemStack.EMPTY;
                    }
                } else {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(amount > 0){
            ItemStack stackInSlot = this.getStackInSlot(slot);
            if(!stackInSlot.isEmpty() && this.inventory.canExtractItem(slot, stackInSlot, this.side)){
                if(simulate){
                    if (stackInSlot.getCount() < amount) {
                        return stackInSlot.copy();
                    } else {
                        ItemStack copy = stackInSlot.copy();
                        copy.setCount(amount);
                        return copy;
                    }
                } else {
                    int m = Math.min(stackInSlot.getCount(), amount);
                    ItemStack decrStackSize = this.inventory.decrStackSize(slot, m);
                    this.inventory.markDirty();
                    return decrStackSize;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.inventory.getSlotLimit(slot);
    }
}
