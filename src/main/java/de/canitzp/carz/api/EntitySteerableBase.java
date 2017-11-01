package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represents steerable vehicles
 *
 * @author MisterErwin
 */
public abstract class EntitySteerableBase extends EntityWorldInteractionBase {

    private boolean inputLeftDown, inputRightDown, inputForwardDown, inputBackDown;

    protected double steeringMod = 0.7;
    protected double steeringMax = 5;

    protected boolean autoSnapping = true;

    protected double someOtherRandomRotModifier = 1;

    public EntitySteerableBase(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void onUpdate(boolean canPassengerSteer) {
        super.onUpdate(canPassengerSteer);
        if (canPassengerSteer) {
            if (this.world.isRemote) {
                this.controlVehicle();
            }
//            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ); moved to EntityMoveable
        }
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

    protected void controlVehicle() {
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
