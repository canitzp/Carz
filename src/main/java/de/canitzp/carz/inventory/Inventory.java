package de.canitzp.carz.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class Inventory implements IInventory, IItemHandlerModifiable{

    private String name;
    protected NonNullList<ItemStack> slots;
    protected boolean ignoreValidify = false;

    public Inventory(String name, int slots){
        this.name = name;
        this.slots = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory() {
        return this.slots.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.slots){
            if(!stack.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.slots.size() ? this.slots.get(index) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(this.slots, index, count);
        if (!stack.isEmpty()) {
            this.markDirty();
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = this.slots.get(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        else {
            this.setInventorySlotContents(index, ItemStack.EMPTY);
            return stack;
        }
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        if(index >= 0 && index < this.slots.size()){
            this.slots.set(index, stack);
            if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for(int i = 0; i < this.getSizeInventory(); i++){
            this.removeStackFromSlot(i);
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(this.name);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.setInventorySlotContents(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.getSizeInventory();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!stack.isEmpty()){
            ItemStack stackInSlot = this.getStackInSlot(slot);
            int m;
            if (!stackInSlot.isEmpty()) {
                if(ItemHandlerHelper.canItemStacksStack(stack, stackInSlot) && this.isItemValidForSlot(slot, stack)){
                    m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();
                    if (stack.getCount() <= m) {
                        if (!simulate) {
                            ItemStack copy = stack.copy();
                            copy.grow(stackInSlot.getCount());
                            this.setInventorySlotContents(slot, copy);
                            this.markDirty();
                        }

                        return ItemStack.EMPTY;
                    } else {
                        // copy the stack to not modify the original one
                        stack = stack.copy();
                        if (!simulate) {
                            ItemStack copy = stack.splitStack(m);
                            copy.grow(stackInSlot.getCount());
                            this.setInventorySlotContents(slot, copy);
                            this.markDirty();
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
                if (this.isItemValidForSlot(slot, stack)){
                    m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
                    if (m < stack.getCount()) {
                        // copy the stack to not modify the original one
                        stack = stack.copy();
                        if (!simulate) {
                            this.setInventorySlotContents(slot, stack.splitStack(m));
                            this.markDirty();
                            return stack;
                        } else {
                            stack.shrink(m);
                            return stack;
                        }
                    } else {
                        if (!simulate) {
                            this.setInventorySlotContents(slot, stack);
                            this.markDirty();
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
            if(!stackInSlot.isEmpty()){
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
                    ItemStack decrStackSize = this.decrStackSize(slot, m);
                    this.markDirty();
                    return decrStackSize;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.getInventoryStackLimit();
    }

    public NBTTagCompound writeTag(NBTTagCompound nbt){
        if(this.getSizeInventory() > 0){
            NBTTagList tagList = new NBTTagList();
            for(int i = 0; i < this.getSizeInventory(); i++){
                ItemStack slot = this.getStackInSlot(i);
                NBTTagCompound tagCompound = new NBTTagCompound();
                if(!slot.isEmpty()){
                    slot.writeToNBT(tagCompound);
                }
                tagList.appendTag(tagCompound);
            }
            nbt.setTag("Inventory", tagList);
        }
        return nbt;
    }

    public void readTag(NBTTagCompound nbt){
        if(nbt.hasKey("Inventory", Constants.NBT.TAG_LIST)){
            if(this.getSizeInventory() > 0){
                NBTTagList tagList = nbt.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
                for(int i = 0; i < this.getSizeInventory(); i++){
                    NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                    this.setInventorySlotContents(i, tagCompound.hasKey("id") ? new ItemStack(tagCompound) : ItemStack.EMPTY);
                }
            }
        }
    }

    /**
     * This flag is for ignoring whenever a {@link ItemStack} can be put into a slot or not.
     * You should use this if you override the {@link SidedInventory#isItemValidForSlot(int, ItemStack)} method!
     * @param ignoreValidify The new flag value. True means that it is forced to be interact with the slot.
     */
    public void setIgnoreValidifyFlag(boolean ignoreValidify){
        this.ignoreValidify = ignoreValidify;
    }
}
