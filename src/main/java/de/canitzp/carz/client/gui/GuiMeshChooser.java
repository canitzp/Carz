package de.canitzp.carz.client.gui;

import de.canitzp.carz.Carz;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import de.canitzp.carz.items.ItemPainter;
import de.canitzp.carz.network.MessageUpdatePainter;
import de.canitzp.carz.network.NetworkHandler;
import de.canitzp.carz.util.GuiUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
@SideOnly(Side.CLIENT)
public class GuiMeshChooser extends GuiScreen{

    public static final ResourceLocation LOC = new ResourceLocation(Carz.MODID, "textures/gui/gui_mesh_chooser.png");
    public static final ResourceLocation TAB = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    private Map<UUID, Map<Integer, List<PixelMesh>>> lines = new HashMap<>();
    private Map<Pair<Integer, Integer>, UUID> tabCoords = new HashMap<>();
    private UUID activeTab = PixelMesh.INTERNAL_UUID;


    public int guiLeft, guiTop, xSize = 195, ySize = 136, painterSlot;
    private EntityPlayer player;

    public GuiMeshChooser(EntityPlayer player, int painterSlot) {
        this.painterSlot = painterSlot;
        this.player = player;
        PixelMesh mesh = ItemPainter.getPixelMeshFromStack(player.inventory.getStackInSlot(painterSlot));
        if(mesh != null){
            this.activeTab = mesh.getOwner();
        }
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.lines.clear();
        List<PixelMesh> meshes = new ArrayList<>(WorldEvents.MESHES_LOADED_INTO_WORLD.values());
        Map<UUID, Integer> tabRowMap = new HashMap<>();
        for (PixelMesh mesh : meshes) {
            int tabIndex = tabRowMap.getOrDefault(mesh.getOwner(), 0);
            int row = tabIndex / 8;
            tabRowMap.put(mesh.getOwner(), ++tabIndex);
            Map<Integer, List<PixelMesh>> tab = this.lines.getOrDefault(mesh.getOwner(), new HashMap<>());
            List<PixelMesh> lineMeshes = tab.getOrDefault(row, new ArrayList<>());
            lineMeshes.add(mesh);
            tab.put(row, lineMeshes);
            this.lines.put(mesh.getOwner(), tab);
        }

        this.tabCoords.clear();
        int x = this.guiLeft;
        int y = this.guiTop - 28;
        for(UUID tab : this.lines.keySet()){
            this.tabCoords.put(Pair.of(x, y), tab);
            x += 30;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        String tooltip = "";
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
                    meshes.get(0).render(x + 6, y + 8);
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
                    meshes.get(0).render(x + 6, y + 8);
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
        PixelMesh underMouse = getMeshUnderMouse(mouseX, mouseY);
        if(underMouse != null){
            this.drawHoveringText(underMouse.getName(), mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton == 0){
            if(mouseY < this.guiTop || mouseY > this.guiTop + this.ySize){
                for(Map.Entry<Pair<Integer, Integer>, UUID> entry : this.tabCoords.entrySet()){
                    int x = entry.getKey().getKey();
                    int y = entry.getKey().getValue();
                    if(mouseX >= x && mouseX <= x + 28){
                        this.activeTab = entry.getValue();
                    }
                }
            }
            PixelMesh underMouse = getMeshUnderMouse(mouseX, mouseY);
            if(underMouse != null){
                NetworkHandler.net.sendToServer(new MessageUpdatePainter(this.painterSlot, this.player.getEntityId(), underMouse.getId()));
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private PixelMesh getMeshUnderMouse(int mouseX, int mouseY){
        if(GuiUtil.isMouseBetween(this.guiLeft, this.guiTop, mouseX, mouseY, 9, 18, 160, 110)){
            int row = (mouseY - this.guiTop - 18) / 20;
            int col = (mouseX - this.guiLeft - 9) / 20;
            Map<Integer, List<PixelMesh>> tabContent = this.lines.getOrDefault(this.activeTab, new HashMap<>());
            List<PixelMesh> meshesInRow = tabContent.getOrDefault(row, new ArrayList<>());
            return meshesInRow.size() > col ? meshesInRow.get(col) : null;
        }
        return null;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
