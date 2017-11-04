package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityAIDriveableBase;
import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.api.IWheelClampable;
import de.canitzp.carz.network.NetworkHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author canitzp
 */
public class EntityDeliveryVan extends EntityAIDriveableBase implements IWheelClampable {
    private static EntityPartedBase.PartData partData;
    private boolean isClamped;

    private static final DataParameter<int[]> SEATING_DATA = EntityDataManager.createKey(EntityDeliveryVan.class, NetworkHandler.VARINT_ARRAY);
    private final static int SEAT_AMOUNT = 2;

    static {
        EntityPartedBase.PartBuilder builder = builder();
        builder.addInteractOnlyPart(0.4f, 0.3f, 0.6f, 0.7f, 1f);
        builder.addInteractOnlyPart(-0.4f, 0.3f, 0.6f, 0.7f, 1f);

        builder.addCollidingPart(0.35f, 0.3f, 1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, 1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, 0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, 0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, -0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, -0.35f, 0.7f, 0.4f);
        builder.addCollidingPart(0.35f, 0.3f, -1.05f, 0.7f, 0.4f);
        builder.addCollidingPart(-0.35f, 0.3f, -1.05f, 0.7f, 0.4f);

        builder.addCollidingPart(-0.65f, 0.3f, 1.55f, 0.4f, 1f);
        builder.addCollidingPart(0, 0.3f, 1.55f, 0.4f, 1f);
        builder.addCollidingPart(+0.65f, 0.3f, 1.55f, 0.4f, 1f);

        builder.addCollidingPart(-0.5f, 0.5f, 0.25f, 0.1f, 1.6f);
        builder.addCollidingPart(0f, 0.5f, 0.25f, 0.1f, 1.6f);
        builder.addCollidingPart(+0.5f, 0.5f, 0.25f, 0.1f, 1.6f);

        for (float z = -0.14f; z > -1.9; z -= 0.4) {
            builder.addCollidingPart(-0.94f, 0.5f, z, 0.1f, 2.3f);
            builder.addCollidingPart(+0.94f, 0.5f, z, 0.1f, 2.3f);
        }
        partData = builder.build();
    }

    public EntityDeliveryVan(World world) {
        super(world);
//        this.setSize(4.0F, 3.0F);
        this.setSize(0.2f, 0.2f);

        this.setDriverSeat(0.5, 0.3D, -0.4);
        this.addSeat(0.5, 0.3D, 0.4);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        int[] seats = new int[SEAT_AMOUNT];
        Arrays.fill(seats, -1);
        this.dataManager.register(SEATING_DATA, seats);
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
        return new ResourceLocation(Carz.MODID, "textures/cars/delivery_van.png");
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


    @Nullable
    public Entity getControllingPassenger() {
        int[] seats = this.dataManager.get(SEATING_DATA);
        return seats.length > 0 && seats[0] != -1 ? world.getEntityByID(seats[0]) : null;
    }


    @Override
    protected int getSeatByPassenger(Entity passenger) {
        int[] seats = this.dataManager.get(SEATING_DATA);
        for (int i = 0; i < seats.length; ++i) {
            if (seats[i] == passenger.getEntityId())
                return i;
        }
        return -1;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand, int partIndex) {
        //The DriverSeat is hitbox No 54
        if (!world.isRemote && /*partIndex >= 54 &&*/ !player.isSneaking()) {
            int seatIndex = partIndex/* - 54*/;
            int[] seating_data = this.dataManager.get(SEATING_DATA);
            if (seatIndex >= SEAT_AMOUNT)
                return false;
            if (seating_data.length > seatIndex && seating_data[seatIndex] != -1)
                return false;
            if (player.getHeldItem(player.getActiveHand()).getItem() instanceof ItemMonsterPlacer) {
                ItemStack spawnEgg = player.getHeldItem(player.getActiveHand());
                Entity entity = ItemMonsterPlacer.spawnCreature(this.world, ItemMonsterPlacer.getNamedIdFrom(spawnEgg), this.posX, this.posY, this.posZ);
                if (entity != null) {
                    if (entity.startRiding(this)) {
                        this.setSeatingData(seating_data, seatIndex, entity.getEntityId());
                        return true;
                    }
                }
            }

            boolean b = player.startRiding(this);
            if (b) {
                this.setSeatingData(seating_data, seatIndex, player.getEntityId());
            }
            return b;
        }

        return super.processInitialInteract(player, hand, partIndex);
    }

    private void setSeatingData(int[] seats, int index, int value) {
        //The EntityDataManager only syncs if the old data != new data, so we have to clone the array
        int[] newseats = new int[Math.max(index + 1, seats.length)];
        System.arraycopy(seats, 0, newseats, 0, seats.length);
        newseats[index] = value;
        this.dataManager.set(SEATING_DATA, newseats);
    }


    @Override
    protected void removePassenger(Entity passenger) {
        int seat = this.getSeatByPassenger(passenger);
        if (seat != -1) {
            this.setSeatingData(dataManager.get(SEATING_DATA), seat, -1);
        }
        super.removePassenger(passenger);
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
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("clamped", this.isClamped);
    }
}
