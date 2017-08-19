package de.canitzp.carz.items;

import de.canitzp.carz.Registry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public abstract class ItemBase<T extends ItemBase> extends Item {

    public T register() {
        Registry.ITEMS_FOR_REGISTERING.add(this);
        return (T) this;
    }

    @SideOnly(Side.CLIENT)
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public void registerClientInit() {
    }

    @Nullable
    @Override
    public CreativeTabs getCreativeTab() {
        return Registry.TAB;
    }

    @Override
    public String getUnlocalizedName() {
        return "item." + this.getRegistryName().toString();
    }
}
