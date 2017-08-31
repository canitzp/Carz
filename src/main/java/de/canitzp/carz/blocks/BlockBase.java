package de.canitzp.carz.blocks;

import de.canitzp.carz.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
public abstract class BlockBase<T extends BlockBase> extends Block {

    public BlockBase(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public BlockBase(Material materialIn) {
        super(materialIn);
    }

    @SuppressWarnings("unchecked")
    public T register() {
        Registry.BLOCKS_FOR_REGISTERING.add(this);
        return (T) this;
    }

    @SideOnly(Side.CLIENT)
    public void registerClient() {
    }

    @SideOnly(Side.CLIENT)
    public void registerClientInit() {
    }

    @SuppressWarnings("ConstantConditions")
    public ItemBlock getItemBlock() {
        return (ItemBlock) new ItemBlock(this).setRegistryName(this.getRegistryName());
    }

    @Nonnull
    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
        return Registry.TAB;
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "tile." + this.getRegistryName().toString();
    }
}
