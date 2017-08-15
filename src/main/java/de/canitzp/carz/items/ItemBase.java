package de.canitzp.carz.items;

import de.canitzp.carz.Registry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public abstract class ItemBase<T extends ItemBase> extends Item {

    public T register(){
        Registry.ITEMS_FOR_REGISTERING.add(this);
        return (T) this;
    }

    @SideOnly(Side.CLIENT)
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation(this.getRegistryName().toString()), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public void registerClientInit() {
    }

}
