package de.canitzp.carz.api;

import de.canitzp.carz.math.Vector3;
import net.minecraft.util.math.MathHelper;

/**
 * Created by MisterErwin on 19.08.2017.
 * In case you need it, ask me ;)
 */
public class WheelData {
    public float angularVelocity; //output
    public float drivetrainInertia;    // drivetrain inertia as currently connected to this wheel
    public float driveFrictionTorque;    // engine braking and other drivetrain friction torques applied to this wheel
    public float driveTorque; //in - 	// engine torque applied to this wheel
    public float slipRatio; //output

    // brake input
    public float brake = 0;
    // handbrake input
    public float handbrake = 0;
    // steering input
    public float steering = 0;
    // Maximal steering angle (in degrees)
    public float maxSteeringAngle = 28f;


    // Wheel angular inertia in kg * m^2
    public float inertia = 2.2f;

    // Maximal braking torque (in Nm)
    public float brakeFrictionTorque = 4000;
    // Maximal handbrake torque (in Nm)
    public float handbrakeFrictionTorque = 0;
    // Base friction torque (in Nm)
    public float frictionTorque = 10;

    Vector3 wheelVelo, localVelo;
    Vector3 forward, right;

    public float slipVelo;

    //cached
    float maxSlip;
    float maxAngle;
    float oldAngle;

    float rpm;

    void onUpdate(EntityMoveAbleV3Base base) {
        if (base.onGround) {
            Vector3 groundNormal = new Vector3(0, 1, 0);
            wheelVelo = base.getVelocity();
            localVelo = transform.InverseTransformDirection(inverseLocalRotation * wheelVelo);
//            suspensionForce = SuspensionForce ();
//            roadForce = RoadForce ();
            base.addForce(RoadForce(base));
        }
    }

    Vector3 RoadForce(EntityMoveAbleV3Base base) {
        int slipRes = (int) ((100.0f - Math.abs(angularVelocity)) / (10.0f));
        if (slipRes < 1)
            slipRes = 1;
        float invSlipRes = (1.0f / (float) slipRes);

        float totalInertia = inertia + drivetrainInertia;
        float driveAngularDelta = driveTorque * invSlipRes / totalInertia;
        float totalFrictionTorque = brakeFrictionTorque * brake + handbrakeFrictionTorque * handbrake + frictionTorque + driveFrictionTorque;
        float frictionAngularDelta = totalFrictionTorque * invSlipRes / totalInertia;

        Vector3 totalForce = Vector3.zero;
        float newAngle = maxSteeringAngle * steering;
        for (int i = 0; i < slipRes; i++) {
            float f = i * 1.0f / (float) slipRes;
            float localRotationY = oldAngle + (newAngle - oldAngle) * f;

            float mX = (float) (MathHelper.sin(-localRotationY * 0.017453292F));
            float mZ = (float) (MathHelper.cos(localRotationY * 0.017453292F));

//            inverseLocalRotation = Quaternion.Inverse(localRotation);
//            forward = transform.TransformDirection(localRotation * Vector3.forward);
//            right = transform.TransformDirection(localRotation * Vector3.right);

            forward = Vector3.forward.rotateAroundY(localRotationY);
            right = Vector3.right.rotateAroundY(localRotationY);

//            slipRatio = SlipRatio();
//            slipAngle = SlipAngle();
            Vector3 force = invSlipRes * grip * CombinedForce(normalForce, slipRatio, slipAngle);
//            Vector3 force = invSlipRes
//            Vector3 worldForce = transform.TransformDirection(localRotationY * force);
            Vector3 worldForce = force.rotateAroundY(localRotationY);
            angularVelocity -= (force.z * 2) / totalInertia;
            angularVelocity += driveAngularDelta;
            if (Math.abs(angularVelocity) > frictionAngularDelta)
                angularVelocity -= frictionAngularDelta * Math.signum(angularVelocity);
            else
                angularVelocity = 0;

            wheelVelo.addSelf(worldForce.mulSelf(1 / base.mass * invSlipRes));
            totalForce.addSelf(worldForce);
        }

        float longitunalSlipVelo = Math.abs(angularVelocity * 2 - wheelVelo.dotProduct(forward));
        float lateralSlipVelo = wheelVelo.dotProduct(right);
        slipVelo = (float) Math.sqrt(longitunalSlipVelo * longitunalSlipVelo + lateralSlipVelo * lateralSlipVelo);
        oldAngle = newAngle;
        return totalForce;
    }

    Vector3 CombinedForce(float Fz, float slip, float slipAngle) {
        float unitSlip = slip / maxSlip;
        float unitAngle = slipAngle / maxAngle;
        float p = (float) Math.sqrt(unitSlip * unitSlip + unitAngle * unitAngle);
        if (p > 0.0001) {
            if (slip < -0.8f)
                return localVelo.normalized().mulSelf (-1*Math.abs(unitAngle / p * CalcLateralForceUnit(Fz, p)) + Math.abs(unitSlip / p * CalcLongitudinalForceUnit(Fz, p)));
//                return -localVelo.normalized * (Mathf.Abs(unitAngle / p * CalcLateralForceUnit(Fz, p)) + Mathf.Abs(unitSlip / p * CalcLongitudinalForceUnit(Fz, p)));
            else {
//                Vector3 forward = new Vector3(0, -groundNormal.z, groundNormal.y);
                Vector3 forward = new Vector3(0, 0, 1);
                return Vector3.right.mul(unitAngle / p * CalcLateralForceUnit(Fz, p)).addSelf(forward.mul(unitSlip / p * CalcLongitudinalForceUnit(Fz, p)));
            }
        } else
            return Vector3.zero;
    }


    float CalcLongitudinalForce(float Fz, float slip) {
        Fz *= 0.001f;//convert to kN
        slip *= 100f; //covert to %
        float uP = b[1] * Fz + b[2];
        float D = uP * Fz;
        float B = ((b[3] * Fz + b[4]) * Mathf.Exp(-b[5] * Fz)) / (b[0] * uP);
        float S = slip + b[9] * Fz + b[10];
        float E = b[6] * Fz * Fz + b[7] * Fz + b[8];
        float Fx = D * Mathf.Sin(b[0] * Mathf.Atan(S * B + E * (Mathf.Atan(S * B) - S * B)));
        return Fx;
    }

    float CalcLateralForce(float Fz, float slipAngle) {
        Fz *= 0.001f;//convert to kN
        slipAngle *= (360f / (2 * Math.PI)); //convert angle to deg
        float uP = a[1] * Fz + a[2];
        float D = uP * Fz;
        float B = (float) ((a[3] * Math.sin(2 * Math.atan(Fz / a[4]))) / (a[0] * uP * Fz));
        float S = slipAngle + a[9] * Fz + a[10];
        float E = a[6] * Fz + a[7];
        float Sv = a[12] * Fz + a[13];
        float Fy = (float) (D * Math.sin(a[0] * Math.atan(S * B + E * (Math.atan(S * B) - S * B))) + Sv);
        return Fy;
    }

    float CalcLongitudinalForceUnit(float Fz, float slip) {
        return CalcLongitudinalForce(Fz, slip * maxSlip);
    }

    float CalcLateralForceUnit(float Fz, float slipAngle) {
        return CalcLongitudinalForce(Fz, slipAngle * maxAngle);
    }

    // Pacejka coefficients
    public float[] a = {1.0f, -60f, 1688f, 4140f, 6.026f, 0f, -0.3589f, 1f, 0f, -6.111f / 1000f, -3.244f / 100f, 0f, 0f, 0f, 0f};
    public float[] b = {1.0f, -60f, 1588f, 0f, 229f, 0f, 0f, 0f, -10f, 0f, 0f};

}
