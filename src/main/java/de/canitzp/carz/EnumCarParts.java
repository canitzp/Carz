package de.canitzp.carz;

/**
 * @author canitzp
 */
public enum EnumCarParts {

    ;

    private float suspension;
    private float side_traction;
    private float forward_traction;
    private float mass;
    private float fuel_usage_idle;
    private float fuel_usage_accelerating;
    private float tank_size;
    private float hit_damage;
    private float health;
    private float frontal_health_modifier;
    private float steering;
    private float acceleration;
    private float reverse_acceleration;
    private int seat_count;

    EnumCarParts(float suspension, float side_traction, float forward_traction, float mass, float fuel_usage_idle, float fuel_usage_accelerating, float tank_size, float hit_damage, float health, float frontal_health_modifier, float steering, float acceleration, float reverse_acceleration, int seat_count) {
        this.suspension = suspension;
        this.side_traction = side_traction;
        this.forward_traction = forward_traction;
        this.mass = mass;
        this.fuel_usage_idle = fuel_usage_idle;
        this.fuel_usage_accelerating = fuel_usage_accelerating;
        this.tank_size = tank_size;
        this.hit_damage = hit_damage;
        this.health = health;
        this.frontal_health_modifier = frontal_health_modifier;
        this.steering = steering;
        this.acceleration = acceleration;
        this.reverse_acceleration = reverse_acceleration;
        this.seat_count = seat_count;
    }

    public float getSuspension() {
        return suspension;
    }

    public float getSideTraction() {
        return side_traction;
    }

    public float getForwardTraction() {
        return forward_traction;
    }

    public float getMass() {
        return mass;
    }

    public float getFuelUsageIdle() {
        return fuel_usage_idle;
    }

    public float getFuelUsageAccelerating() {
        return fuel_usage_accelerating;
    }

    public float getTankSize() {
        return tank_size;
    }

    public float getHitDamage() {
        return hit_damage;
    }

    public float getHealth() {
        return health;
    }

    public float getFrontalHealthModifier() {
        return frontal_health_modifier;
    }

    public float getSteering() {
        return steering;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getReverseAcceleration() {
        return reverse_acceleration;
    }

    public int getSeatCount() {
        return seat_count;
    }
}
