package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import de.canitzp.carz.blocks.BlockRoadSign;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.gui.GuiMeshChooser;
import de.canitzp.carz.client.gui.GuiPixelMesher;
import de.canitzp.carz.events.WorldEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

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
                this.openCreationGui(player);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockRoadSign) {
            PixelMesh mesh = getPixelMeshFromStack(player.getHeldItem(hand));
            if (mesh != null) {
                ((BlockRoadSign) state.getBlock()).clickedWithPainter(world, pos, state, mesh);
                return EnumActionResult.SUCCESS;
            }
            return this.onItemRightClick(world, player, hand).getType();
        }
        if (world.isRemote) {
            this.openChooseGui(player.getHeldItem(hand), player, player.inventory.currentItem);
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(!player.world.isRemote && target instanceof EntityPlayer){
            PixelMesh mesh = getPixelMeshFromStack(stack);
            if(mesh != null){
                if(mesh.canBeEditedBy(player)){
                    mesh.setOwner(((EntityPlayer) target).getGameProfile().getId());
                    return true;
                }
            }
        }
        return super.itemInteractionForEntity(stack, player, target, hand);
    }

    @SideOnly(Side.CLIENT)
    private void openCreationGui(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiPixelMesher(player));
    }

    @SideOnly(Side.CLIENT)
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
            if (nbt.hasUniqueId("PixelMeshUUID")) {
                return WorldEvents.getMeshByUUID(nbt.getUniqueId("PixelMeshUUID"));
            }
        }
        return null;
    }
}