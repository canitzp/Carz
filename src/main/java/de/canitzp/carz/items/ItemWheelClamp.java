package de.canitzp.carz.items;

import de.canitzp.carz.api.IWheelClampable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

/**
 * @author MisterErwin
 */
public class ItemWheelClamp extends ItemBaseDefault<ItemWheelClamp> {
    public ItemWheelClamp() {
        super("wheel_clamp");
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof IWheelClampable) {
            if (((IWheelClampable) target).isClamped()) {
                return false;
            }

            if ((target.motionX * target.motionX) + (target.motionZ * target.motionZ) > 0.02) {
                playerIn.sendMessage(new TextComponentString("Car is moving too fast"));
                return false;
            }

            ItemStack itemstack = playerIn.getHeldItem(hand);
            if (!playerIn.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            ((IWheelClampable) target).setClamped(true);
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
