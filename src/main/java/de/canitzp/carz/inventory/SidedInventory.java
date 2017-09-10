package de.canitzp.carz.inventory;

import com.google.common.collect.Collections2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author canitzp
 */
public abstract class SidedInventory extends Inventory implements ISidedInventory {

    public SidedInventory(String name, int slots){
        super(name, slots);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return new int[this.slots.size()];
    }

    public SidedInventoryWrapper[] getOneForAllSides(){
        SidedInventoryWrapper[] invs = new SidedInventoryWrapper[6];
        for(EnumFacing side : EnumFacing.values()){
            invs[side.ordinal()] = new SidedInventoryWrapper(this, side);
        }
        return invs;
    }

}
