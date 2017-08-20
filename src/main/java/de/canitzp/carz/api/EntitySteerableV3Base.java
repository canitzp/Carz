package de.canitzp.carz.api;

import de.canitzp.carz.math.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by MisterErwin on 19.08.2017.
 * In case you need it, ask me ;)
 */
public abstract class EntitySteerableV3Base extends EntityRideableBaseV3 {

    private final static float Deg2Rad = (float) (Math.PI * 2) / 360;

    private boolean inputLeftDown, inputRightDown, inputForwardDown, inputBackDown, inputThrottleDown, inputHandBrakeDown;


    // current input state
    float brake;
    float throttle;
    float throttleInput;
    float steering;
    float lastShiftTime = -1;
    float handbrake;


    // How long the car takes to shift gears
    public float shiftSpeed = 0.8f;


    // These values determine how fast throttle value is changed when the accelerate keys are pressed or released.
    // Getting these right is important to make the car controllable, as keyboard input does not allow analogue input.
    // There are different values for when the wheels have full traction and when there are spinning, to implement
    // traction control schemes.

    // How long it takes to fully engage the throttle
    public float throttleTime = 1.0f;
    // How long it takes to fully engage the throttle
    // when the wheels are spinning (and traction control is disabled)
    public float throttleTimeTraction = 10.0f;
    // How long it takes to fully release the throttle
    public float throttleReleaseTime = 0.5f;
    // How long it takes to fully release the throttle
    // when the wheels are spinning.
    public float throttleReleaseTimeTraction = 0.1f;

    // Turn traction control on or off
    public boolean tractionControl = true;


    // These values determine how fast steering value is changed when the steering keys are pressed or released.
    // Getting these right is important to make the car controllable, as keyboard input does not allow analogue input.

    // How long it takes to fully turn the steering wheel from center to full lock
    public float steerTime = 1.2f;
    // This is added to steerTime per m/s of velocity, so steering is slower when the car is moving faster.
    public float veloSteerTime = 0.1f;

    // How long it takes to fully turn the steering wheel from full lock to center
    public float steerReleaseTime = 0.6f;
    // This is added to steerReleaseTime per m/s of velocity, so steering is slower when the car is moving faster.
    public float veloSteerReleaseTime = 0f;
    // When detecting a situation where the player tries to counter steer to correct an oversteer situation,
    // steering speed will be multiplied by the difference between optimal and current steering times this
    // factor, to make the correction easier.
    public float steerCorrectionFactor = 4.0f;


    public EntitySteerableV3Base(World worldIn) {
        super(worldIn);

    }


    @Override
    public void onUpdate() {
        if (this.world.isRemote && this.canPassengerSteer()){
            this.controlVehicle();
        }

        super.onUpdate();
    }

    private void controlVehicle(){
        this.drivetrain.onUpdate();
        // Steering
        Vector3 carDir = getForwardDirection();
        float fVelo = getVelocity().magnitude();
        Vector3 veloDir = getVelocity().mul(1 / fVelo);
        float angle = (float) -Math.asin(MathHelper.clamp(Vector3.cross(veloDir, carDir).y, -1, 1));
        float optimalSteering = angle / (wheels[0].maxSteeringAngle * Deg2Rad);
        if (fVelo < 1)
            optimalSteering = 0;

        float steerInput = 0;
        if (inputLeftDown)
            steerInput = -1;
        if (inputRightDown)
            steerInput = 1;

        if (steerInput < steering) {
            float steerSpeed = (steering > 0) ? (1 / (steerReleaseTime + veloSteerReleaseTime * fVelo)) : (1 / (steerTime + veloSteerTime * fVelo));
            if (steering > optimalSteering)
                steerSpeed *= 1 + (steering - optimalSteering) * steerCorrectionFactor;
            steering -= steerSpeed;
            if (steerInput > steering)
                steering = steerInput;
        } else if (steerInput > steering) {
            float steerSpeed = (steering < 0) ? (1 / (steerReleaseTime + veloSteerReleaseTime * fVelo)) : (1 / (steerTime + veloSteerTime * fVelo));
            if (steering < optimalSteering)
                steerSpeed *= 1 + (optimalSteering - steering) * steerCorrectionFactor;
            steering += steerSpeed;
            if (steerInput < steering)
                steering = steerInput;
        }

        // Throttle/Brake

        boolean accelKey = inputForwardDown;
        boolean brakeKey = inputBackDown;

        if (drivetrain.automatic && drivetrain.gear == 0) {
            accelKey = inputBackDown;
            brakeKey = inputForwardDown;
        }

        if (inputThrottleDown) {
            throttle += 1 / throttleTime;
            throttleInput += 1 / throttleTime;
        } else if (accelKey) {
            if (drivetrain.slipRatio < 0.10f)
                throttle += 1 / throttleTime;
            else if (!tractionControl)
                throttle += 1 / throttleTimeTraction;
            else
                throttle -= 1 / throttleReleaseTime;

            if (throttleInput < 0)
                throttleInput = 0;
            throttleInput += 1 / throttleTime;
            brake = 0;
        } else {
            if (drivetrain.slipRatio < 0.2f)
                throttle -= 1 / throttleReleaseTime;
            else
                throttle -= 1 / throttleReleaseTimeTraction;
        }
        throttle = MathHelper.clamp(throttle, 0, 1);

        if (brakeKey) {
            if (drivetrain.slipRatio < 0.2f)
                brake += 1 / throttleTime;
            else
                brake += 1 / throttleTimeTraction;
            throttle = 0;
            throttleInput -= 1 / throttleTime;
        } else {
            if (drivetrain.slipRatio < 0.2f)
                brake -= 1 / throttleReleaseTime;
            else
                brake -= 1 / throttleReleaseTimeTraction;
        }
        brake = MathHelper.clamp(brake, 0, 1);
        throttleInput = MathHelper.clamp(throttleInput, -1, 1);

        // Handbrake
        handbrake = MathHelper.clamp(handbrake + (inputHandBrakeDown ? 1 : -1), 0, 1);

        // Gear shifting
        float shiftThrottleFactor = MathHelper.clamp((1 - lastShiftTime) / shiftSpeed, 0, 1);
        drivetrain.throttle = throttle * shiftThrottleFactor;
        drivetrain.throttleInput = throttleInput;

//        if (Input.GetKeyDown(KeyCode.A)) {
//            lastShiftTime = Time.time;
//            drivetrain.ShiftUp();
//        }
//        if (Input.GetKeyDown(KeyCode.Z)) {
//            lastShiftTime = Time.time;
//            drivetrain.ShiftDown();
//        }

        // Apply inputs
        for (WheelData w : wheels) {
            w.brake = brake;
            w.handbrake = handbrake;
            w.steering = steering;
        }
        System.out.println(brake+ "//" + handbrake + "//" + steering);
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


}
