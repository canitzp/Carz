package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import de.canitzp.carz.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represents steerable vehicles
 * //ToDo: Linke Kurve - Links Hinten ROtieren
 *
 * @author MisterErwin
 */
public abstract class EntitySteerableBase extends EntityWorldInteractionBase {

    private boolean inputLeftDown, inputRightDown, inputForwardDown, inputBackDown;

    protected double steeringMod = 0.7;
    protected double steeringMax = 5;

    protected boolean autoSnapping = true;

    protected double someOtherRandomRotModifier = 1;

    public double rotationTranslationX, rotationTranslationY, rotationTranslationZ;
    //    public double localRotationTranslationX, localRotationTranslationY, localRotationTranslationZ;
    protected final static DataParameter<Float> localRotationTranslationX = EntityDataManager.createKey(EntitySteerableBase.class, DataSerializers.FLOAT);
    //    protected final static DataParameter<Float> localRotationTranslationY = EntityDataManager.createKey(EntitySteerableBase.class, DataSerializers.FLOAT);
    protected final static DataParameter<Float> localRotationTranslationZ = EntityDataManager.createKey(EntitySteerableBase.class, DataSerializers.FLOAT);

    private double lastRotationTranslationX, lastRotationTranslationY, lastRotationTranslationZ;


    public EntitySteerableBase(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(localRotationTranslationX, 0f);
        dataManager.register(localRotationTranslationZ, 0f);
        lastRotationYawForRotation = rotationYaw;
    }


    private double lastRotationYawForRotation;

    @Override
    protected void onUpdate(boolean canPassengerSteer) {
        super.onUpdate(canPassengerSteer);
        if (canPassengerSteer) {
            if (this.world.isRemote) {
                this.controlVehicle();
            }
//            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ); moved to EntityMoveable
        }
        if (!world.isRemote) {
            double offset = 0.01;
            float localRotX = dataManager.get(localRotationTranslationX);
            float localRotZ = dataManager.get(localRotationTranslationZ);
            if (lastRotationYawForRotation - rotationYaw < 0) {
                localRotX -= offset;
                localRotZ -= offset * 3;
            } else if (lastRotationYawForRotation - rotationYaw > 0) {
                localRotX += offset;
                localRotZ += offset * 3;
            } else if (localRotX < 0) {
                localRotX += offset;
                localRotZ += offset * 3;
            } else if (localRotX > 0) {
                localRotX -= offset;
                localRotZ -= offset * 3;
            }
            lastRotationYawForRotation = rotationYaw;

            dataManager.set(localRotationTranslationX, Math.max(-1, Math.min(1, localRotX)));
            dataManager.set(localRotationTranslationZ, Math.max(-2, Math.min(2, localRotZ)));
        }
        updateRotationTranslation();
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (world.isRemote) {
            this.updateInputs(false, false, false, false);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateInputs(boolean left, boolean right, boolean forward, boolean back) {
        this.inputLeftDown = left;
        this.inputRightDown = right;
        this.inputForwardDown = forward;
        this.inputBackDown = back;
    }

    private void updateRotationTranslation() {
        double cosYaw = Math.cos(-rotationYaw * 0.017453292F);
        double sinYaw = Math.sin(rotationYaw * 0.017453292F);

        float localRotX = dataManager.get(localRotationTranslationX);
        float localRotZ = dataManager.get(localRotationTranslationZ);
        this.rotationTranslationX = MathUtil.rotX(localRotX, 0, localRotZ,
                cosYaw, sinYaw);
//        this.rotationTranslationY = localRotationTranslationY;
        this.rotationTranslationZ = MathUtil.rotZ(localRotX, 0, localRotZ,
                cosYaw, sinYaw);

//        this.posX += lastRotationTranslationX-rotationTranslationX;
//        this.posZ += lastRotationTranslationZ-rotationTranslationZ;
//
//        this.posZ+=0.01;

        setPosition(this.posX + lastRotationTranslationX - rotationTranslationX,
                this.posY,
                this.posZ + lastRotationTranslationZ - rotationTranslationZ);

        this.lastRotationTranslationX = rotationTranslationX;
        this.lastRotationTranslationY = rotationTranslationY;
        this.lastRotationTranslationZ = rotationTranslationZ;
    }



    protected void controlVehicle() {
//        updateRotationTranslation();

        if (this.isBeingRidden()) {
            world.spawnParticle(EnumParticleTypes.FOOTSTEP, posX, posY, posZ, 0.1, 0.1, 0.1);
            float fwd = 0.0F; //Forward movement?

            if (this.spinningTicks <= 10) {
                double deltaR = 0; //rotation

                if (this.inputForwardDown)
                    fwd += 0.04; //OPTION: forward (0.04)
                if (this.inputBackDown)
                    fwd -= 0.015; //OPTION: backward (0.005)

                setSpeed(getSpeed() + fwd);

                //Steering
                if (this.speedSqAbs > 0) {
                    deltaR = Math.abs(steeringMod / this.speedSqAbs);

                    //Maybe use this for something?
                    double segment = Math.PI * 6 * deltaR / 180;

                    deltaR = Math.min(deltaR, someOtherRandomRotModifier * Math.sqrt(this.speedSqAbs));

                    //Rotate if driving backwards
                    deltaR *= this.speedSq > 0 ? 1 : -1;

                    if (inputLeftDown)
                        deltaR = -deltaR;
                    else if (inputRightDown)
                        deltaR = deltaR;
                    else
                        deltaR = 0;
                }

                if (this.speedSq > 0 && deltaR == 0 && autoSnapping) {
                    double rotYaw = MathHelper.wrapDegrees(this.rotationYaw);
                    double rotmod = MathHelper.positiveModulo(rotYaw, 90);

                    if (rotmod < 5) {
                        deltaR -= 0.03 * (rotmod + 0.00001);//  -0.15;
                    } else if (rotmod > 85) {
                        deltaR += 0.03 * (90 - rotmod);//  +0.15;
                    }
                }

                if (this.centrifugalForce > 2) { //OPTION: go into spinning mode (2)
                    Carz.LOG.info("Too fast around that corner");
                    deltaR = 0;
                    this.spinningTicks = 40;
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 2, this.posZ, 0.1, 0.1, 0.1);
                }
                if (speedSqAbs > 0.001) {
                    //Apply the rotation if the car is moving.
                    this.deltaRotationYaw += deltaR;
//                    this.rotationYaw += this.deltaRotationYaw; //MOVED: EntityMoveABle
                }
            } else {
                this.deltaRotationYaw += 0.01;
                this.rotationYaw += this.deltaRotationYaw; //Spin me around
            }
            //Apply movement
//            this.motionX += (double) (MathHelper.sin(-this.rotationYaw * 0.017453292F) * fwd);
//            this.motionZ += (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * fwd);
            //moved to EntityMoveable
        }
    }
}
