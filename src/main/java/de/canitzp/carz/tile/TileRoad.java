package de.canitzp.carz.tile;

import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.events.WorldEvents;
import de.canitzp.carz.util.TileUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class TileRoad extends TileRoadBase{

    private PixelMesh mesh = null;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        PixelMesh oldMesh = mesh;
        EnumFacing oldMeshFacing = getMeshFacing();
        super.readFromNBT(compound);
        if (compound.hasUniqueId("MeshUUID")) {
            this.mesh = WorldEvents.getMeshByUUID(compound.getUniqueId("MeshUUID"));
        } else {
            this.mesh = null;
        }
        if (world!=null&&world.isRemote&&(oldMesh!=mesh||oldMeshFacing!=getMeshFacing())) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
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
