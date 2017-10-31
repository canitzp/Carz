package de.canitzp.carz.items;

import de.canitzp.carz.api.IWheelClampable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

/**
 * @author MisterErwin
 */
public class ItemWheelClamp extends ItemBaseDefault<ItemWheelClamp> {
    public ItemWheelClamp() {
        super("wheel_clamp");
    }

    public static boolean doInteract(ItemStack stack, EntityPlayer playerIn, Entity target) {
        if (target instanceof IWheelClampable) {
            if (((IWheelClampable) target).isClamped()) {
                playerIn.sendMessage(new TextComponentString("One clamp is enough, eh?"));
                return true;
            }

            if ((target.motionX * target.motionX) + (target.motionZ * target.motionZ) > 0.02) {
                playerIn.sendMessage(new TextComponentString("Car is moving too fast"));
                return true;
            }

            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
            ((IWheelClampable) target).setClamped(true);
            return true;
        }
        return false;
    }
}
