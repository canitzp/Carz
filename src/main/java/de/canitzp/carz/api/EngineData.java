package de.canitzp.carz.api;

import de.canitzp.carz.math.Vector3;

/**
 * Created by MisterErwin on 19.08.2017.
 * In case you need it, ask me ;)
 */
public class EngineData {

    // All the wheels the drivetrain should power
    public WheelData[] poweredWheels;

    // The gear ratios, including neutral (0) and reverse (negative) gears
    public float[] gearRatios;

    // The final drive ratio, which is multiplied to each gear ratio
    public float finalDriveRatio = 3.23f;


    // powerband RPM range
    public float minRPM = 800;
    public float maxRPM = 6400;

    // engine's maximal torque (in Nm) and RPM.
    public float maxTorque = 664;
    public float torqueRPM = 4000;

    // engine's maximal power (in Watts) and RPM.
    public float maxPower = 317000;
    public float powerRPM = 5000;

    // engine inertia (how fast the engine spins up), in kg * m^2
    public float engineInertia = 0.3f;


    // engine's friction coefficients - these cause the engine to slow down, and cause engine braking.

    // constant friction coefficient
    public float engineBaseFriction = 25f;
    // linear friction coefficient (higher friction when engine spins higher)
    public float engineRPMFriction = 0.02f;

    // Engine orientation (typically either Vector3.forward or Vector3.right).
    // This determines how the car body moves as the engine revs up.
    public Vector3 engineOrientation = Vector3.forward.clone();

    // Coefficient determining how muchg torque is transfered between the wheels when they move at
    // different speeds, to simulate differential locking.
    public float differentialLockCoefficient = 0;

    // inputs
    // engine throttle
    public float throttle = 0;
    // engine throttle without traction control (used for automatic gear shifting)
    public float throttleInput = 0;

    // shift gears automatically?
    public boolean automatic = true;

    // state
    public int gear = 1/*2*/;
    public float rpm;
    public float slipRatio = 0.0f;
    float engineAngularVelo;

    public EngineData(WheelData[] poweredWheels) {
        this.poweredWheels = poweredWheels;
        this.gearRatios = new float[]{-10f, 9f, 6f, 4.5f, 3f, 2.5f};
    }

    float sqr(float x) {
        return x * x;
    }

    // Calculate engine torque for current rpm and throttle values.
    float CalcEngineTorque() {
        float result;
        if (rpm < torqueRPM)
            result = maxTorque * (-sqr(rpm / torqueRPM - 1) + 1);
        else {
            float maxPowerTorque = (float) (maxPower / (powerRPM * 2 * Math.PI / 60));
            float aproxFactor = (maxTorque - maxPowerTorque) / (2 * torqueRPM * powerRPM - sqr(powerRPM) - sqr(torqueRPM));
            float torque = aproxFactor * sqr(rpm - torqueRPM) + maxTorque;
            result = torque > 0 ? torque : 0;
        }
        if (rpm > maxRPM) {
            result *= 1 - ((rpm - maxRPM) * 0.006f);
            if (result < 0)
                result = 0;
        }
        if (rpm < 0)
            result = 0;
        return result;
    }

    void onUpdate() {
        float ratio = gearRatios[gear] * finalDriveRatio;
        float inertia = engineInertia * sqr(ratio);
        float engineFrictionTorque = engineBaseFriction + rpm * engineRPMFriction;
        float engineTorque = (CalcEngineTorque() + Math.abs(engineFrictionTorque)) * throttle;
        slipRatio = 0.0f;

        if (ratio == 0) {
            // Neutral gear - just rev up engine
            float engineAngularAcceleration = (engineTorque - engineFrictionTorque) / engineInertia;
            engineAngularVelo += engineAngularAcceleration;

            // Apply torque to car body
//            rigidbody.AddTorque(-engineOrientation * engineTorque);
            System.out.println("rev in neutral");
        } else {
            float drivetrainFraction = 1.0f / poweredWheels.length;
            float averageAngularVelo = 0;
            for (WheelData w : poweredWheels)
                averageAngularVelo += w.angularVelocity * drivetrainFraction;

            // Apply torque to wheels
            for (WheelData w : poweredWheels) {
                float lockingTorque = (averageAngularVelo - w.angularVelocity) * differentialLockCoefficient;
                w.drivetrainInertia = inertia * drivetrainFraction;
                w.driveFrictionTorque = engineFrictionTorque * Math.abs(ratio) * drivetrainFraction;
                w.driveTorque = engineTorque * ratio * drivetrainFraction + lockingTorque;

                slipRatio += w.slipRatio * drivetrainFraction;

                System.out.println(w.drivetrainInertia+ "//" + w.driveFrictionTorque + "//" + w.driveTorque);
            }

            // update engine angular velo
            engineAngularVelo = averageAngularVelo * ratio;
        }

        // update state
        slipRatio *= Math.signum(ratio);
        rpm = (float) (engineAngularVelo * (60.0f / (2 * Math.PI)));

        // very simple simulation of clutch - just pretend we are at a higher rpm.
        float minClutchRPM = minRPM;
        if (gear == 2)
            minClutchRPM += throttle * 3000;
        if (rpm < minClutchRPM)
            rpm = minClutchRPM;

        // Automatic gear shifting. Bases shift points on throttle input and rpm.
        if (automatic) {
            if (rpm >= maxRPM * (0.5f + 0.5f * throttleInput))
                shiftUp();
            else if (rpm <= maxRPM * (0.25f + 0.4f * throttleInput) && gear > 2)
                shiftDown();
            if (throttleInput < 0 && rpm <= minRPM)
                gear = (gear == 0 ? 2 : 0);
        }
    }

    public void shiftUp() {
        if (gear < gearRatios.length - 1)
            gear++;
    }

    public void shiftDown() {
        if (gear > 0)
            gear--;
    }
}
