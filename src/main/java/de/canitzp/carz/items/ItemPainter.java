package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.IPaintableTile;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.gui.GuiMeshChooser;
import de.canitzp.carz.client.gui.GuiPixelMesher;
import de.canitzp.carz.events.WorldEvents;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author canitzp
 */
public class ItemPainter extends ItemBase<ItemPainter> {

    public ItemPainter() {
        this.setRegistryName(Carz.MODID, "painter");
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            if (player.isSneaking()) {
                this.openChooseGui(player.getHeldItem(hand), player, player.inventory.currentItem);
            } else {
                this.openCreationGui();
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            if (!player.isSneaking()) {
                if (world.getTileEntity(pos) instanceof IPaintableTile) {
                    PixelMesh mesh = getPixelMeshFromStack(player.getHeldItem(hand));
                    if (mesh != null) {
                        ((IPaintableTile) world.getTileEntity(pos)).setPixelMesh(mesh);
                    }
                } else {
                    return this.onItemRightClick(world, player, hand).getType();
                }
            } else {
                this.openChooseGui(player.getHeldItem(hand), player, player.inventory.currentItem);
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    private void openCreationGui() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiPixelMesher());
    }

    private void openChooseGui(ItemStack stack, EntityPlayer player, int painterSlot) {
        if (!stack.isEmpty() && stack.getItem() == this) {
            GuiMeshChooser gui = new GuiMeshChooser(player, painterSlot);
            PixelMesh mesh = getPixelMeshFromStack(stack);
            if (mesh != null) {
                gui.setCurrentMesh(mesh);
            }
            Minecraft.getMinecraft().displayGuiScreen(gui);
        }
    }

    @Nullable
    public static PixelMesh getPixelMeshFromStack(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("PixelMeshMost", Constants.NBT.TAG_LONG) && nbt.hasKey("PixelMeshLeast", Constants.NBT.TAG_LONG)) {
                return WorldEvents.getMeshByUUID(nbt.getUniqueId("PixelMesh"));
            }
        }
        return null;
    }
}