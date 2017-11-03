package de.canitzp.carz.entity;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityAIDriveableBase;
import de.canitzp.carz.api.EntityPartedBase;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author canitzp, MisterErwin
 */
public class EntityBus extends EntityAIDriveableBase {

    private static EntityPartedBase.PartData partData;

    private final static int SEAT_AMOUNT = 7;

    static {
        EntityPartedBase.PartBuilder builder = builder();

        //Driver
        builder.addInteractOnlyPart(.9f, 0.4f, 2.9f, .8f, 1.1f);

        //first row
        builder.addInteractOnlyPart(.9f, 0.4f, 1.4f, .8f, 1.1f);
        builder.addInteractOnlyPart(-.9f, 0.4f, 1.4f, .8f, 1.1f);

        //2nd row
        builder.addInteractOnlyPart(.9f, 0.3f, 0.2f, .8f, 1.1f);
        builder.addInteractOnlyPart(-.9f, 0.3f, 0.2f, .8f, 1.1f);

        builder.addInteractOnlyPart(.9f, 0.3f, -2.7f, .8f, 1.1f);
        builder.addInteractOnlyPart(-.9f, 0.3f, -2.7f, .8f, 1.1f);

        //Under**fucking**ground
        for (int z = -3; z <= 3; ++z)
            for (int x = -1; x <= 1; ++x)
                builder.addCollidingPart(x, 0, z, 1, 0.2f);

        float wh = 1.6f;
        float wo = 0.1f; //wall offset Y
        for (float z = -3; z <= 3; z += 0.5f)
            builder.addPart(1.5f, 0, z, 0.2f, wh);

        //Seitenwand
        builder.addPart(-1.5f, wo, -3.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, -2.5f, 0.2f, wh);
        builder.addPart(-1.5f, wo, -2.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, -1.65f, 0.2f, wh);
        //-1
        //-0.5
        builder.addPart(-1.5f, wo, +0.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +0.5f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +1.0f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +1.5f, 0.2f, wh);
        builder.addPart(-1.5f, wo, +1.85f, 0.2f, wh);
        //2.5
        builder.addPart(-1.5f, wo, +3.2f, 0.2f, wh);

        for (float x = -1f; x <= 1; x += 0.5f) {
            builder.addPart(x, wo, -3.5f, 0.2f, wh);
            if (x % 1 == 0)
                builder.addPart(x, wo, +3.5f, 0.2f, wh);
            else
                builder.addCollidingPart(x, wo, +3.5f, 0.2f, wh);
        }


        partData = builder.build();
    }

    private final static DataParameter<int[]> SEATING_DATA = EntityDataManager.createKey(EntityBus.class, NetworkHandler.VARINT_ARRAY);

    public EntityBus(World worldIn) {
        super(worldIn);
        this.setSize(1.75F, 0.2f); //1.8125F

        this.setDriverSeat(2.75F, -1.3F, -0.9F);
        this.addSeat(1.05F, -1.3F, -0.9F);
        this.addSeat(1.05F, -1.3F, 0.9F);

        this.addSeat(0.05F, -1.3F, -0.9F);
        this.addSeat(0.05F, -1.3F, 0.9F);

        this.addSeat(-2.7f, -1.3F, -0.9F);
        this.addSeat(-2.7f, -1.3F, 0.9F);

        this.wheelLength = 7 / 2;
        this.wheelWidth = 3 / 2;

        this.steeringMax = 2;
        this.steeringMod = 0.07;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        int[] seats = new int[SEAT_AMOUNT];
        Arrays.fill(seats, -1);
        this.dataManager.register(SEATING_DATA, seats);
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

//    @Override
//    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
//        return false; //Interact with main-box
//    }

    @Override
    public double getMountedYOffset() {
        return 1.45; //1.812*0.8
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
    public ModelBase getCarModel() {
        return Registry.MODEL_BUS;
    }

    @Nullable
    @Override
    public ResourceLocation getCarTexture() {
        return new ResourceLocation(Carz.MODID, "textures/cars/bus.png");
    }

    @Override
    public void setupGL(double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.translate(x, y + 1.5, z);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.rotationPitch, 1.0F, 0.0F, 0.0F);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

    }
}