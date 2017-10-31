package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import de.canitzp.carz.blocks.BlockBoostingRoad;
import de.canitzp.carz.client.gui.GuiRoadConfigurator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author MisterErwin
 */
public class ItemRoadConfigurator extends ItemBase<ItemRoadConfigurator> {

    @SuppressWarnings("ConstantConditions")
    public ItemRoadConfigurator() {
        this.setRegistryName(Carz.MODID, "road_configurator");
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockBoostingRoad && world.isRemote) {
            this.openGui(player, pos);
//                ((BlockBoostingRoad) state.getBlock()).clickedWithConfigurator(world, pos, player, state, hand, facing, mesh, hitX, hitY, hitZ);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    private void openGui(EntityPlayer player, BlockPos pos) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiRoadConfigurator(player, pos));
    }
}