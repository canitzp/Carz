package de.canitzp.carz.client.gui;

import de.canitzp.carz.Carz;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.PixelMeshScrollPane;
import de.canitzp.carz.client.gui.meshpane.PixelMeshPane;
import de.canitzp.carz.events.WorldEvents;
import de.canitzp.carz.network.MessageUpdatePainter;
import de.canitzp.carz.network.NetworkHandler;
import de.canitzp.carz.util.GuiUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.*;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiMeshChooser extends GuiScreen{

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_mesh_chooser.png");
    public static final ResourceLocation TAB = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    private Map<UUID, Map<Integer, List<PixelMesh>>> lines = new HashMap<>();
    private Map<Pair<Integer, Integer>, UUID> tabCoords = new HashMap<>();
    private UUID activeTab = new UUID(0, 1);


    public int guiLeft, guiTop, xSize = 195, ySize = 136, painterSlot;
    private PixelMesh currentMesh = null;
    private EntityPlayer player;
    private PixelMeshPane pane;

    public GuiMeshChooser(EntityPlayer player, int painterSlot) {
        this.painterSlot = painterSlot;
        this.player = player;
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.lines.clear();
        List<PixelMesh> meshes = new ArrayList<>(WorldEvents.MESHES_LOADED_INTO_WORLD.values());
        for(int i = 0; i < meshes.size(); i++){
            PixelMesh mesh = meshes.get(i);
            Map<Integer, List<PixelMesh>> tab = this.lines.getOrDefault(mesh.getOwner(), new HashMap<>());
            List<PixelMesh> lineMeshes = tab.getOrDefault(i / 8, new ArrayList<>());
            lineMeshes.add(meshes.get(i));
            tab.put(i / 8, lineMeshes);
            this.lines.put(mesh.getOwner(), tab);
        }

        this.tabCoords.clear();
        int x = this.guiLeft;
        int y = this.guiTop - 28;
        for(UUID tab : this.lines.keySet()){
            this.tabCoords.put(Pair.of(x, y), tab);
            x += 30;
        }

        //this.pane = new PixelMeshPane(this, this.guiLeft + 9, this.guiTop + 18, 160, 110);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mc.getTextureManager().bindTexture(TAB);
        for(Map.Entry<Pair<Integer, Integer>, UUID> entry : this.tabCoords.entrySet()){
            boolean isActive = entry.getValue().equals(this.activeTab);
            if(!isActive){
                int x = entry.getKey().getKey();
                int y = entry.getKey().getValue();
                boolean isTop = y <= this.guiTop;
                boolean isLeft = x == this.guiLeft;
                boolean isRight = false;

                this.drawTexturedModalRect(x, y, isLeft ? 0 : isRight ? 140 : 28, isTop ? 2 : 64, 28, isLeft ? 31 : 28);
                List<PixelMesh> meshes = this.lines.get(entry.getValue()).getOrDefault(0, new ArrayList<>());
                if(!meshes.isEmpty()){
                    GlStateManager.pushMatrix();
                    meshes.get(0).render(x + 6, y + 8);
                    GlStateManager.popMatrix();
                }
            }
        }
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 195, 136);
        this.mc.getTextureManager().bindTexture(TAB);
        for(Map.Entry<Pair<Integer, Integer>, UUID> entry : this.tabCoords.entrySet()){
            boolean isActive = entry.getValue().equals(this.activeTab);
            if(isActive){
                int x = entry.getKey().getKey();
                int y = entry.getKey().getValue();
                boolean isTop = y <= this.guiTop;
                boolean isLeft = x == this.guiLeft;
                boolean isRight = false;
                this.drawTexturedModalRect(x, y, isLeft ? 0 : isRight ? 140 : 28, isTop ? 32 : 96, 28, 32);
                List<PixelMesh> meshes = this.lines.get(entry.getValue()).getOrDefault(0, new ArrayList<>());
                if(!meshes.isEmpty()){
                    GlStateManager.pushMatrix();
                    meshes.get(0).render(x + 6, y + 8);
                    GlStateManager.popMatrix();
                }
            }
        }
        for(Map.Entry<UUID, Map<Integer, List<PixelMesh>>> uuidMapEntry : this.lines.entrySet()){
           if(this.activeTab.equals(uuidMapEntry.getKey())){
               for(Map.Entry<Integer, List<PixelMesh>> entry : uuidMapEntry.getValue().entrySet()){
                   List<PixelMesh> value = entry.getValue();
                   for (int i = 0; i < value.size(); i++) {
                       PixelMesh mesh = value.get(i);
                       int x = this.guiLeft + 9 + i * 20 + 1;
                       int y = this.guiTop + 18 + entry.getKey() * 20 + 1;
                       GuiUtil.drawCage(x, y, 18, 18, 0xFF494949);
                       mesh.render(x + 1, y + 1);
                   }
               }
           }
        }

        //this.pane.draw(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        //if(this.scrollPane.getCurrentMesh() != null){
          //  NetworkHandler.net.sendToServer(new MessageUpdatePainter(this.painterSlot, this.player.getEntityId(), this.scrollPane.getCurrentMesh().getId()));
        //}
        super.onGuiClosed();
    }

    public void setCurrentMesh(PixelMesh currentMesh) {
        this.currentMesh = currentMesh;
    }
}
