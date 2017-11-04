package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import de.canitzp.carz.client.gui.GuiLicense;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * @author canitzp
 */
public class ItemLicense extends ItemBaseDefault<ItemLicense> {

    public enum Type{
        DRIVER
    }

    public static class State{
        public boolean isValid = false;
        public boolean isBTMMoonApproved = false;
        public String expireDate = "DD.MM.YYYY";
        public String owner = "Herobrine";
        public UUID ownerId = UUID.randomUUID();
        public State setValid(boolean valid) {
            isValid = valid;
            return this;
        }
        public State setExpireDate(String expireDate) {
            this.expireDate = expireDate;
            return this;
        }
        public void setOwner(String owner) {
            this.owner = owner;
        }
        public void setBTMMoonApproved(boolean BTMMoonApproved) {
            isBTMMoonApproved = BTMMoonApproved;
        }
        public NBTTagCompound getNBT(){
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("IsValid", this.isValid);
            nbt.setBoolean("BTMMoonApproved", this.isBTMMoonApproved);
            nbt.setString("ExpireDate", this.expireDate);
            nbt.setString("Owner", this.owner);
            nbt.setUniqueId("OwnerId", this.ownerId);
            return nbt;
        }
        public State readFromNBT(NBTTagCompound nbt){
            this.isValid = nbt.getBoolean("IsValid");
            this.isBTMMoonApproved = nbt.getBoolean("BTMMoonApproved");
            this.expireDate = nbt.hasKey("ExpireDate", Constants.NBT.TAG_STRING) ? nbt.getString("ExpireDate") : "DD.MM.YYYY";
            this.owner = nbt.hasKey("Owner", Constants.NBT.TAG_STRING) ? nbt.getString("Owner") : "Herobrine";
            this.ownerId = nbt.hasUniqueId("OwnerId") ? nbt.getUniqueId("OwnerId") : UUID.randomUUID();
            return this;
        }
    }

    public ItemLicense() {
        super("license");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        for(Type type : Type.values()){
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation(Carz.MODID, "license_" + type.name().toLowerCase()), "inventory"));
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)){
            for(Type type : Type.values()){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("Type", type.ordinal());
                nbt.setTag("State", new State().getNBT());
                ItemStack stack = new ItemStack(this);
                stack.setTagCompound(nbt);
                items.add(stack);
            }
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if(stack.hasTagCompound()){
            return "item." + Carz.MODID + ":license_" + Type.values()[stack.getTagCompound().getInteger("Type")].name().toLowerCase();
        }
        return super.getUnlocalizedName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override // TODO localisation
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        State state = getLicenseState(stack);
        if(state != null){
            tooltip.add("Owner: " + state.owner);
            tooltip.add("Expire Date: " + state.expireDate);
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(!player.world.isRemote && target instanceof EntityPlayer){
            tryToSetStateForBTM(player, (EntityPlayer) target, stack);
        }
        return super.itemInteractionForEntity(stack, player, target, hand);
    }

    @SideOnly(Side.CLIENT) // since we only need it on the client anyway
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if(world.isRemote && !player.isSneaking()){
            ItemStack stack = player.getHeldItem(hand);
            State state = getLicenseState(stack);
            if(state != null){
                Type type = Type.values()[stack.getTagCompound().getInteger("Type")];
                Minecraft.getMinecraft().displayGuiScreen(new GuiLicense(type, state));
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        } else if(player.isSneaking()){
            tryToSetStateForBTM(player, player, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    private void tryToSetStateForBTM(EntityPlayer player, EntityPlayer target, ItemStack stack){
        if(Carz.MODVERSION.equals("@VERSION@") || player.getDisplayNameString().equals("canitzp") || player.getDisplayNameString().equals("MisterErwin") || player.getDisplayNameString().equals("Zombiefleischer")){
            State state = new State();
            state.setOwner(target.getDisplayName().getFormattedText());
            state.ownerId = target.getUniqueID();
            state.setExpireDate("Does not apply");
            state.setValid(true);
            state.setBTMMoonApproved(true);
            setLicenseState(stack, state);
        }
    }

    public static State getLicenseState(@Nonnull ItemStack stack){
        if(!stack.isEmpty() && stack.hasTagCompound() && stack.getItem() instanceof ItemLicense){
            State state = new State();
            state.readFromNBT(stack.getTagCompound().getCompoundTag("State"));
            return state;
        }
        return null;
    }

    public static void setLicenseState(@Nonnull ItemStack stack, State state){
        if(!stack.isEmpty() && stack.getItem() instanceof ItemLicense){
            NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            nbt.setTag("State", state.getNBT());
            stack.setTagCompound(nbt);
        }
    }
}
