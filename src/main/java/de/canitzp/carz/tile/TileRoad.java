package de.canitzp.carz.tile;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import de.canitzp.carz.util.TileUtil;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class TileRoad extends TileRoadBase{

    private PixelMesh mesh = null;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasUniqueId("MeshUUID")) {
            this.mesh = WorldEvents.getMeshByUUID(compound.getUniqueId("MeshUUID"));
        } else {
            this.mesh = null;
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if(this.mesh != null){
            compound.setUniqueId("MeshUUID", this.mesh.getId());
        }
        return super.writeToNBT(compound);
    }

    public void setMesh(PixelMesh mesh) {
        this.mesh = mesh;
        TileUtil.sync(this);
    }

    public PixelMesh getMesh() {
        return mesh;
    }
}
