package de.canitzp.carz.client.gui;

import com.google.common.collect.Lists;
import de.canitzp.carz.Carz;
import de.canitzp.carz.api.Safety;
import de.canitzp.carz.inventory.ContainerPlantFermenter;
import de.canitzp.carz.tile.TilePlantFermenter;
import de.canitzp.carz.util.GuiUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author canitzp
 */
public class GuiPlantFermenter extends GuiContainer {

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_plant_fermenter.png");
    private TilePlantFermenter tile;

    public GuiPlantFermenter(EntityPlayer player, int x, int y, int z) {
        super(new ContainerPlantFermenter(player, x, y, z));
        this.tile = Safety.getTile(player.world, new BlockPos(x, y, z), TilePlantFermenter.class);
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        if(GuiUtil.isMouseBetween(this.guiLeft, this.guiTop, mouseX, mouseY, 150, 8, 16, 65)){
            FluidStack fluid = this.tile.tank.getFluid();
            if(fluid != null && fluid.getFluid() != null && fluid.amount > 0){
                this.drawHoveringText(Lists.newArrayList(fluid.getLocalizedName(), TextFormatting.GRAY.toString() + fluid.amount + "/" + this.tile.tank.getCapacity()), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if(this.tile.maxTicks > 0){
            int progress = Math.round((this.tile.ticksLeft / (this.tile.maxTicks * 1.0F)) * 25F);
            if(progress > 0){
                this.drawTexturedModalRect(this.guiLeft + 61, this.guiTop + 27, 176, 0, 15, 25 - progress);
            }
        }

        GuiUtil.drawFluid(this.tile.tank, this.guiLeft + 150, this.guiTop + 8, 16, 65);

        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft + 150, this.guiTop + 8, 176, 25, 16, 65);
    }
}
