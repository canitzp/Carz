package de.canitzp.carz.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
@SuppressWarnings({"deprecation", "WeakerAccess"})
public abstract class BlockContainerBase<T extends BlockContainerBase> extends BlockBase<T> implements ITileEntityProvider {

    private Class<? extends TileEntity> tileClass;

    public BlockContainerBase(Material blockMaterialIn, MapColor blockMapColorIn, Class<? extends TileEntity> tileClass) {
        super(blockMaterialIn, blockMapColorIn);
        this.hasTileEntity = true;
        this.tileClass = tileClass;
    }

    public BlockContainerBase(Material materialIn, Class<? extends TileEntity> tileClass) {
        this(materialIn, materialIn.getMaterialMapColor(), tileClass);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public T register() {
        GameRegistry.registerTileEntity(this.tileClass, this.getRegistryName().toString());
        return super.register();
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     */
    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        try {
            return this.tileClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected T overrideTileClass(Class<? extends TileEntity> tileClass){
        this.tileClass = tileClass;
        return (T) this;
    }
}