package de.canitzp.carz.items;

import de.canitzp.carz.api.EntityAIDriveableBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

/**
 * @author MisterErwin
 */
public class ItemCardLinkedDriver extends ItemBaseDefault<ItemCardLinkedDriver> {
    public ItemCardLinkedDriver() {
        super("card_linked_driver");
    }

    public UUID getEntityID(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasUniqueId("car") ? itemStack.getTagCompound()
                .getUniqueId("car") : null;
    }

    public static boolean doInteract(ItemStack stack, EntityPlayer playerIn, Entity target) {
        if (target instanceof EntityAIDriveableBase) {
            if (stack.getTagCompound() == null)
                stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setUniqueId("car", target.getPersistentID());
            playerIn.sendMessage(new TextComponentString("Stored car UUID"));
            return true;
        }
        return false;
    }

}
