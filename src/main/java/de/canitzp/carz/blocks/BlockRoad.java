package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.api.IPaintableBlock;
import de.canitzp.carz.client.PixelMesh;
import de.canitzp.carz.client.renderer.RenderRoad;
import de.canitzp.carz.items.ItemPainter;
import de.canitzp.carz.tile.TileRoad;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class BlockRoad<T extends BlockRoad> extends BlockContainerBase<T> implements IPaintableBlock{

    public BlockRoad(String name) {
        super(Material.ROCK, MapColor.BLACK, TileRoad.class);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setRegistryName(new ResourceLocation(Carz.MODID, name));
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileRoad.class, new RenderRoad());
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileRoad){
            if(((TileRoad) tile).getMesh() != null){
                ((TileRoad) tile).setMesh(null);
                return false;
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileRoad) {
            if (((TileRoad) tile).getMesh() != null) {
                return ItemPainter.getStackWithMesh(((TileRoad) tile).getMesh());
            }
        }
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            entity.motionX *= 1.25D;
            entity.motionZ *= 1.25D;
        }
    }

    @Override
    public void clickedWithPainter(World world, BlockPos pos, EntityPlayer player, IBlockState state, EnumHand hand, EnumFacing facing, PixelMesh mesh, float hitX, float hitY, float hitZ){
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileRoad){
            ((TileRoad) tile).setMesh(mesh);
            ((TileRoad) tile).setMeshFacing(player.getHorizontalFacing());
        }
    }

}
