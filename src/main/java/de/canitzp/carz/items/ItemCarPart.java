package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import de.canitzp.carz.EnumCarParts;
import de.canitzp.carz.Registry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class ItemCarPart extends ItemBase<ItemCarPart> {

    public ItemCarPart(){
        this.setRegistryName(Carz.MODID, "car_part");
        this.setCreativeTab(Registry.TAB);
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(@Nonnull ItemStack stack) {
        if(this.hasStackPart(stack)){
            return this.getUnlocalizedName() + "." + this.getPartFromStack(stack).name().toLowerCase();
        }
        return super.getUnlocalizedName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        if(this.isInCreativeTab(tab)){
            for(EnumCarParts part : EnumCarParts.values()){
                list.add(getStackFromPart(part));
            }
        }
    }

    @Nonnull
    public ItemStack getStackFromPart(@Nonnull EnumCarParts part){
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("PartType", part.ordinal());
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(nbt);
        return stack;
    }

    @Nullable
    public EnumCarParts getPartFromStack(@Nonnull ItemStack stack){
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt != null && nbt.hasKey("PartType", Constants.NBT.TAG_INT)){
            return EnumCarParts.values()[nbt.getInteger("PartType")];
        }
        return null;
    }

    public boolean hasStackPart(@Nonnull ItemStack stack){
        return getPartFromStack(stack) != null;
    }

}
