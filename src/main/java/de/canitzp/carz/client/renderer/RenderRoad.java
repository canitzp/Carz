package de.canitzp.carz.client.renderer;

import de.canitzp.carz.tile.TileRoad;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RenderRoad extends TileEntitySpecialRenderer<TileRoad> {

    @Override
    public void render(TileRoad te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.setLightmapDisabled(true);
        te.render(this, x, y, z, partialTicks, destroyStage, alpha);
        this.setLightmapDisabled(false);
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }

}
