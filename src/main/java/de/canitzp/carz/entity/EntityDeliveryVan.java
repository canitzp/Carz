package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityMultiSeatsBase;
import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.api.IColorableCar;
import de.canitzp.carz.api.IWheelClampable;
import de.canitzp.carz.items.ItemPainter;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class EntityDeliveryVan extends EntityMultiSeatsBase implements IWheelClampable, IColorableCar {
    private static EntityPartedBase.PartData partData;
    private boolean isClamped;
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityDeliveryVan.class, DataSerializers.VARINT);

    static {
        EntityPartedBase.PartBuilder builder = builder();
        builder.addInteractOnlyPart(0.4f, 0.3f, 0.6f, 0.7f, 1f);
        builder.addInteractOnlyPart(-0.4f, 0.3f, 0.6f, 0.7f, 1f);
        //backseats
        builder.addInteractOnlyPart(-0.4f, 0.3f, -1.6f, 0.7f, 0.5f);
        builder.addInteractOnlyPart(0.4f, 0.3f, -1.6f, 0.7f, 0.5f);
        //Floor
        builder.addCollidingPart(0.35f, 0.3f, 1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, 1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, 0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, 0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, -0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, -0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, -1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, -1.05f, 0.7f, 0.4f);

        //Motor block
        builder.addCollidingPart(-0.65f, 0.3f, 1.55f, 0.4f, 1f);
        builder.addCollidingPart(0, 0.3f, 1.55f, 0.4f, 1f);
        builder.addCollidingPart(+0.65f, 0.3f, 1.55f, 0.4f, 1f);

        //Seperator
        builder.addCollidingPart(-0.5f, 0.5f, 0.25f, 0.1f, 1.6f);
        builder.addCollidingPart(0f, 0.5f, 0.25f, 0.1f, 1.6f);
        builder.addCollidingPart(+0.5f, 0.5f, 0.25f, 0.1f, 1.6f);

        //Walls
        for (float z = -0.14f; z > -1.9; z -= 0.4) {
            builder.addPart(-0.94f, 0.5f, z, 0.1f, 2.3f);
            builder.addPart(+0.94f, 0.5f, z, 0.1f, 2.3f);
        }


        partData = builder.build();
    }

    public EntityDeliveryVan(World world) {
        super(world, 4);
//        this.setSize(4.0F, 3.0F);
        this.setSize(0.2f, 0.2f);

        this.setDriverSeat(0.5, 0.3D, -0.4);
        this.addSeat(0.5, 0.3D, 0.4);

        //Seats in the back
        this.addSeat(-1.9, 0.3D, .5, 180);
        this.addSeat(-1.9, 0.3D, -.5, 180);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(COLOR, 0xF76900);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBase getCarModel() {
        return Registry.MODEL_DELIVERY_VAN;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return new ResourceLocation(Carz.MODID, "textures/cars/delivery_van_base.png");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.translate(x, y + 1.5, z);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.rotationPitch, 1.0F, 0.0F, 0.0F);
    }

    @Override
    protected EntityInvisibleCarPart[] constructPartArray() {
        return partData.spawnInvisibleParts(this);
    }

    @Override
    protected int[] constructCollidingPartIndizes() {
        return partData.getCollidingPartIndizes();
    }

    @Override
    public void setClamped(boolean clamped) {
        this.isClamped = clamped;
    }

    @Override
    public boolean isClamped() {
        return this.isClamped;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.isClamped = compound.getBoolean("clamped");
        if (compound.hasKey("Color", Constants.NBT.TAG_INT)) {
            this.dataManager.set(COLOR, compound.getInteger("Color"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("clamped", this.isClamped);
        compound.setInteger("Color", this.dataManager.get(COLOR));
    }

    @Override
    public ResourceLocation getOverlayTexture() {
        return new ResourceLocation(Carz.MODID, "textures/cars/delivery_van_overlay.png");
    }

    @Override
    public int getCurrentColor() {
        return this.dataManager.get(COLOR);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty()) {
            if(stack.getItem() instanceof ItemDye){
                if (!player.world.isRemote) {
                    EnumDyeColor color = EnumDyeColor.byDyeDamage(stack.getMetadata());
                    this.dataManager.set(COLOR, color.getColorValue());
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
                return true;
            }/* else if(stack.getItem() instanceof ItemPainter){
                if (!player.world.isRemote) {
                    this.dataManager.set(MESH, ItemPainter.getPixelMeshFromStack(stack).getId());
                }
                return true;
            }*/
        }
        return super.processInitialInteract(player, hand);
    }
}
