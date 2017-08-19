package de.canitzp.carz.network;

import de.canitzp.carz.client.gui.GuiPixelMesher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class GuiHandler implements IGuiHandler {

    public static final int ID_PIXELMESHER = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            default: return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case ID_PIXELMESHER: {
                return new GuiPixelMesher(player);
            }
            default: return null;
        }
    }
}
