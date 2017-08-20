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
public abstract class EntitySteerableBase extends EntityRideableBase {

    private boolean inputLeftDown, inputRightDown, inputForwardDown, inputBackDown;


    protected boolean autoSnapping = true;

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
        if (world.isRemote){
            this.updateInputs(false,false,false,false);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateInputs(boolean left, boolean right, boolean forward, boolean back) {
        this.inputLeftDown = left;
        this.inputRightDown = right;
        this.inputForwardDown = forward;
        this.inputBackDown = back;
    }

    private void controlVehicle() {
        if (this.isBeingRidden()) {
            world.spawnParticle(EnumParticleTypes.FOOTSTEP, posX, posY, posZ, 0.1, 0.1, 0.1);
            float fwd = 0.0F; //Forward movement?

            if (this.spinningTicks <= 10) {
                double deltaR = 0; //rotation
                if (this.inputLeftDown)
                    deltaR -= 1;
                if (this.inputRightDown)
                    deltaR += 1;

                if (this.inputForwardDown)
                    fwd += 0.04; //OPTION: forward (0.04)
                if (this.inputBackDown)
                    fwd -= 0.015; //OPTION: backward (0.005)

                setSpeed(getSpeed() + fwd);

//                double speedSqAbs = this.motionZ * this.motionZ + this.motionX * this.motionX;

//                double momYaw = MathHelper.wrapDegrees(MathHelper.atan2(this.motionZ, this.motionX) * 180 / Math.PI) - 90;
//                double rotYaw = MathHelper.wrapDegrees(this.rotationYaw);
//                double angle = MathHelper.wrapDegrees(rotYaw - momYaw);
//                double speedSq = (angle > 170 || angle < -170) ? -speedSqAbs : speedSqAbs;


                //times 2.5 and reverse if driving backwards
                deltaR *= 2.5 * this.speedSq > 0 ? 1 : -1;
                if (this.speedSq > 0) {
                    //TODO: Yeah - find some actual good numbers
                    //OPTION: steering
                    deltaR *= 3 * Math.pow(Math.max(5, Math.min(30, Math.abs(angle)) - 4), -1.001);
//                    deltaR *= speedSqAbs;//(float)Math.pow(speedSqAbs, 2.0D);
//                    deltaR = MathHelper.clamp(deltaR, 0f,5f);
//                    System.out.println(deltaR);
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
                    this.deltaRotation += deltaR;
//                    this.rotationYaw += this.deltaRotation; //MOVED: EntityMoveABle
                }
            } else {
                this.deltaRotation += 0.01;
                this.rotationYaw += this.deltaRotation; //Spin me around
            }
            //Apply movement
//            this.motionX += (double) (MathHelper.sin(-this.rotationYaw * 0.017453292F) * fwd);
//            this.motionZ += (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * fwd);
            //moved to EntityMoveable
        }
    }
}
