package de.canitzp.carz.network;

import de.canitzp.carz.client.gui.GuiCar;
import de.canitzp.carz.client.gui.GuiPixelMesher;
import de.canitzp.carz.client.gui.GuiPlantFermenter;
import de.canitzp.carz.client.gui.GuiRoadConfigurator;
import de.canitzp.carz.inventory.ContainerCar;
import de.canitzp.carz.inventory.ContainerPlantFermenter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
public class GuiHandler implements IGuiHandler {

    public static final int ID_PIXELMESHER = 0;
    public static final int ID_PLANT_FERMENTER = 1;
    public static final int ID_CAR = 2;
    public static final int ID_BOOSTING_ROAD = 3;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case ID_PLANT_FERMENTER:{
                return new ContainerPlantFermenter(player, x, y, z);
            }
            case ID_CAR:{
                return new ContainerCar(player, x);
            }
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
            case ID_PLANT_FERMENTER: {
                return new GuiPlantFermenter(player, x, y, z);
            }
            case ID_CAR: {
                return new GuiCar(player, x);
            }
            case ID_BOOSTING_ROAD: {
                return new GuiRoadConfigurator(player, new BlockPos(x, y, z));
            }
            default: return null;
        }
    }
}
