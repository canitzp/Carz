package de.canitzp.carz.client.gui;

import de.canitzp.carz.items.ItemLicense;
import de.canitzp.carz.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.text.TextFormatting;

/**
 * @author canitzp
 */
public class GuiLicense extends GuiScreen {

    private int halfWidth;
    private int halfHeight;
    private int guiLeft, guiTop, xSize, ySize;
    private ItemLicense.Type type;
    private ItemLicense.State state;
    private final EntityCreeper fakeCreeper = new EntityCreeper(Minecraft.getMinecraft().world);

    public GuiLicense(ItemLicense.Type type, ItemLicense.State state){
        this.type = type;
        this.state = state;
    }

    @Override
    public void initGui() {
        halfWidth =  Math.round(this.width / 2);
        halfHeight = Math.round(this.height / 2);
        xSize = 250;
        ySize = 140;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0xFFA9CFA3);
        Gui.drawRect(halfWidth - 115, halfHeight - 45, halfWidth - 50, halfHeight + 55, 0xFF000000);
        GuiUtil.drawCenteredString(GuiUtil.getTextWithFormatting("Carz - Driver License", TextFormatting.BOLD, TextFormatting.UNDERLINE), guiLeft + 165, guiTop + 5, 0xFFE01F1F, false);
        GuiUtil.drawCenteredString(GuiUtil.getTextWithFormatting("Owner: ", TextFormatting.BOLD) + state.owner, guiLeft + 165, guiTop + 28, 0xF8000000, false);
        GuiUtil.drawCenteredString(GuiUtil.getTextWithFormatting("Expire Date: ", TextFormatting.BOLD) + state.expireDate, guiLeft + 165, guiTop + 42, 0xF8000000, false);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        EntityLivingBase entity = Minecraft.getMinecraft().world.getPlayerEntityByName(this.state.owner);
        GuiInventory.drawEntityOnScreen(guiLeft + 42, guiTop + 115, 50, guiLeft + 42 - mouseX, guiTop + 50 - mouseY, entity != null ? entity : fakeCreeper);

        GlStateManager.translate(guiLeft + 29, guiTop + 116, 100.0);
        GlStateManager.rotate(25, 0, 0, 1);
        drawSeal("BTM Moon", fontRenderer, 0, 0, 0xFF800088, 0xFF880080);

        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawSeal(String sealText, FontRenderer fontRenderer, int centerX, int centerY, int rectColor, int textColor){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int textWidth = fontRenderer.getStringWidth(sealText);
        GuiUtil.drawCage(centerX - (textWidth / 2) - 2, centerY - 7, textWidth + 4, 22, rectColor);
        Gui.drawRect(centerX - (textWidth / 2) - 1, centerY - 6, centerX - (textWidth / 2) - 1 + textWidth + 2, centerY + 14, 0x88FFFFFF);
        GuiUtil.drawCenteredString(sealText, centerX, centerY - 5, textColor, true);
        GuiUtil.drawCenteredString("approved", centerX, centerY + 5, textColor, true);
    }
}
