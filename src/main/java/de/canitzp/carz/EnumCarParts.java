package de.canitzp.carz;

/**
 * @author canitzp
 */
@SuppressWarnings("SameParameterValue")
public enum EnumCarParts {

    ENGINE(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    private float suspension;
    private float sideTraction;
    private float forwardTraction;
    private float mass;
    private float fuelUsageIdle;
    private float fuelUsageAccelerating;
    private float tankSize;
    private float hitDamage;
    private float health;
    private float frontalHealthModifier;
    private float steering;
    private float acceleration;
    private float reverseAcceleration;
    private int seatCount;

    EnumCarParts(float suspension, float sideTraction, float forwardTraction, float mass, float fuelUsageIdle, float fuelUsageAccelerating, float tankSize, float hitDamage, float health, float frontalHealthModifier, float steering, float acceleration, float reverseAcceleration, int seatCount) {
        this.suspension = suspension;
        this.sideTraction = sideTraction;
        this.forwardTraction = forwardTraction;
        this.mass = mass;
        this.fuelUsageIdle = fuelUsageIdle;
        this.fuelUsageAccelerating = fuelUsageAccelerating;
        this.tankSize = tankSize;
        this.hitDamage = hitDamage;
        this.health = health;
        this.frontalHealthModifier = frontalHealthModifier;
        this.steering = steering;
        this.acceleration = acceleration;
        this.reverseAcceleration = reverseAcceleration;
        this.seatCount = seatCount;
    }

    public float getSuspension() {
        return suspension;
    }

    public float getSideTraction() {
        return sideTraction;
    }

    public float getForwardTraction() {
        return forwardTraction;
    }

    public float getMass() {
        return mass;
    }

    public float getFuelUsageIdle() {
        return fuelUsageIdle;
    }

    public float getFuelUsageAccelerating() {
        return fuelUsageAccelerating;
    }

    public float getTankSize() {
        return tankSize;
    }

    public float getHitDamage() {
        return hitDamage;
    }

    public float getHealth() {
        return health;
    }

    public float getFrontalHealthModifier() {
        return frontalHealthModifier;
    }

    public float getSteering() {
        return steering;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getReverseAcceleration() {
        return reverseAcceleration;
    }

    public int getSeatCount() {
        return seatCount;
    }
}
