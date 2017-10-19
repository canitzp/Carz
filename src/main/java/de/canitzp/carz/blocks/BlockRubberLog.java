package de.canitzp.carz.blocks;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author canitzp
 */
public class BlockRubberLog extends BlockLog {

    public static final PropertyDirection FACING = PropertyDirection.create("rubber_side", input -> input == EnumFacing.NORTH || input == EnumFacing.SOUTH || input == EnumFacing.WEST || input == EnumFacing.EAST);
    public static final PropertyBool RUBBER = PropertyBool.create("rubber");
    public static final PropertyBool CURRENT_RUBBER = PropertyBool.create("rubbery");

    public BlockRubberLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(RUBBER, false).withProperty(FACING, EnumFacing.NORTH).withProperty(LOG_AXIS, EnumAxis.NONE).withProperty(CURRENT_RUBBER, false));
        this.setRegistryName(Carz.MODID, "block_log");
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Registry.TAB_GENERAL);
        this.setTickRandomly(true);
    }

    public BlockRubberLog register(){
        Registry.BLOCKS.add(this);
        return this;
    }

    @Override //0b rubbery , isRubber , axis/side,axis/side
    public IBlockState getStateFromMeta(int meta) {
        boolean isRubber = ((meta >> 2) & 1) == 1;
        if(isRubber){
            return this.getDefaultState().withProperty(RUBBER, true).withProperty(CURRENT_RUBBER, (meta >> 3) == 1).withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(LOG_AXIS, EnumAxis.Y);
        } else {
            return this.getDefaultState().withProperty(RUBBER, false).withProperty(LOG_AXIS, EnumAxis.values()[meta & 3]);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(state.getValue(RUBBER)){
            return 4 | state.getValue(FACING).getHorizontalIndex() | (state.getValue(CURRENT_RUBBER) ? 8 : 0);
        } else {
            return state.getValue(LOG_AXIS).ordinal();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, RUBBER, LOG_AXIS, CURRENT_RUBBER);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = this.getStateFromMeta(meta);
        if(state.getValue(RUBBER)){
            return state;
        }
        return state.withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(facing == state.getValue(FACING) && player.getHeldItem(hand).getItem() == Registry.itemTreeTap){
            if(!world.isRemote){
                if(state.getValue(CURRENT_RUBBER)){
                    world.spawnEntity(new EntityItem(world, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, new ItemStack(Registry.itemRawRubber, world.rand.nextInt(1) + 1)));
                    world.setBlockState(pos, state.withProperty(CURRENT_RUBBER, false));
                } else {
                    world.setBlockState(pos, state.withProperty(RUBBER, false).withProperty(CURRENT_RUBBER, false).withProperty(LOG_AXIS, EnumAxis.NONE));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if(random.nextInt(2) == 0){
            if(state.getValue(RUBBER) && !state.getValue(CURRENT_RUBBER)){
                world.setBlockState(pos, state.withProperty(CURRENT_RUBBER, true));
            }
        }
    }
}
