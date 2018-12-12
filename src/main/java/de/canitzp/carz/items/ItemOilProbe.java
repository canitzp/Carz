package de.canitzp.carz.items;

import de.canitzp.carz.Carz;
import de.canitzp.carz.data.WorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class ItemOilProbe extends ItemBase<ItemOilProbe> {

    public ItemOilProbe(){
        this.setRegistryName(Carz.MODID, "oil_probe");
        this.setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            Chunk chunk = world.getChunk(pos);
            if(WorldData.hasChunkOil(world, chunk.x, chunk.z)){
                player.sendStatusMessage(new TextComponentTranslation("item.carz:oil_probe.found", WorldData.getOilInChunk(world, chunk.x, chunk.z)), false);
            }
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
}
