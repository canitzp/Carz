package de.canitzp.carz.client.gui;

import de.canitzp.carz.Carz;
import de.canitzp.carz.network.MessageUpdateRoadConfigurator;
import de.canitzp.carz.network.NetworkHandler;
import de.canitzp.carz.tile.TileBoostingRoad;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;

/**
 * @author MisterErwin
 */
@SuppressWarnings("WeakerAccess")
@SideOnly(Side.CLIENT)
public class GuiRoadConfigurator extends GuiScreen implements GuiPageButtonList.GuiResponder {

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_road_configurator.png");

    public int guiLeft, guiTop, currentColor = 0xFFFFFFFF;
    public GuiButton absoluteField;
    public GuiSlider xSliderField, ySliderField, zSliderField;
    private Robot robot;
    private EntityPlayer player;
    private BlockPos pos;


    public GuiRoadConfigurator(EntityPlayer player, BlockPos pos) {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        this.player = player;
        this.pos = pos;
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width / 2) - 64;
//        this.guiTop = (this.height - 256) / 2;
        this.guiTop = (this.height / 2) - 64;


        TileEntity tile = mc.world.getTileEntity(pos);

        if (!(tile instanceof TileBoostingRoad)) return;


        this.buttonList.add(this.absoluteField = new GuiButton(0, this.guiLeft + 12, this.guiTop + 13, 150, 20,
                ((TileBoostingRoad) tile).isAbsolute() ? "Absolute" : "Relative"));
        this.buttonList.add(this.xSliderField = new GuiSlider(this, 1, this.guiLeft + 12, this.guiTop + 40,
                "x", -100, 100, ((TileBoostingRoad) tile).getX(), IntFormatter.INSTANCE));
        this.buttonList.add(this.ySliderField = new GuiSlider(this, 2, this.guiLeft + 12, this.guiTop + 70,
                "y", -100, 100, ((TileBoostingRoad) tile).getY(), IntFormatter.INSTANCE));
        this.buttonList.add(this.zSliderField = new GuiSlider(this, 3, this.guiLeft + 12, this.guiTop + 100,
                "z", -100, 100, ((TileBoostingRoad) tile).getZ(), IntFormatter.INSTANCE));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 256, 256);
        this.absoluteField.drawButton(this.mc, mouseX, mouseY, partialTicks);
        this.xSliderField.drawButton(this.mc, mouseX, mouseY, partialTicks);
        this.ySliderField.drawButton(this.mc, mouseX, mouseY, partialTicks);
        this.zSliderField.drawButton(this.mc, mouseX, mouseY, partialTicks);


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (absoluteField.isMouseOver()) {
            TileBoostingRoad tile = getTile();
            tile.setAbsolute(!tile.isAbsolute());
            NetworkHandler.net.sendToServer(new MessageUpdateRoadConfigurator(this.player.getEntityId(), pos, tile));
            absoluteField.displayString = tile.isAbsolute() ? "Absolute" : "Relative";
        }
    }

    private TileBoostingRoad getTile() {
        return (TileBoostingRoad) this.mc.world.getTileEntity(pos);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void setEntryValue(int id, boolean value) {
    }

    @Override
    public void setEntryValue(int id, float value) {
        TileBoostingRoad tile = getTile();
        switch (id) {
            case 1:
                tile.setX((int) value);
                break;
            case 2:
                tile.setY((int) value);
                break;
            case 3:
                tile.setZ((int) value);
                break;
            default:
                return;
        }
        NetworkHandler.net.sendToServer(new MessageUpdateRoadConfigurator(this.player.getEntityId(), pos, tile));
    }

    @Override
    public void setEntryValue(int id, @Nonnull String value) {
    }

    private static class IntFormatter implements GuiSlider.FormatHelper {
        public static final IntFormatter INSTANCE = new IntFormatter();

        @Override
        public @Nonnull
        String getText(int id, @Nonnull String name, float value) {
            return name + ": " + (int) value;
        }
    }
}
