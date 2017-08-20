package de.canitzp.carz.math;

import net.minecraft.util.math.Vec3d;

/**
 * Created by MisterErwin on 19.08.2017.
 * In case you need it, ask me ;)
 */
public class Vector3 {
    public float x, y, z;

    public final static Vector3 up = new Vector3(0, 1, 0);
    public final static Vector3 forward = new Vector3(0, 0, 1);
    public final static Vector3 right = new Vector3(1, 0, 0);
    public final static Vector3 zero = new Vector3(0, 0, 0);

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 addSelf(float ox, float oy, float oz) {
        this.x += ox;
        this.y += oy;
        this.z += oz;
        return this;
    }

    public Vector3 addSelf(Vector3 other) {
        return this.addSelf(other.x, other.y, other.z);
    }


    public Vector3 add(float ox, float oy, float oz) {
        return new Vector3(x + ox, y + oy, z + oz);
    }

    public Vector3 add(Vector3 other) {
        return this.add(other.x, other.y, other.z);
    }


    public Vector3 addMult(float ox, float oy, float oz, float m) {
        return new Vector3(x + ox * m, y + oy * m, z + oz * m);
    }

    public Vector3 addMult(Vector3 other, float m) {
        return this.addMult(other.x, other.y, other.z, m);
    }


    public Vector3 mulSelf(float m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public Vector3 mul(float m) {
        return new Vector3(x * m, y * m, z * m);
    }

    public float dotProduct(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public float magnitudeSq() {
        return x * x + y * y + z * z;
    }

    public float magnitude() {
        return (float) Math.sqrt(this.magnitudeSq());
    }

    public Vector3 normalized() {
        return this.mul(1 / magnitude());
    }

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public static Vec3d cross(Vector3 a, Vector3 b) {
        return new Vec3d(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    public Vector3 rotateAroundY(float angle) {
        double cos = Math.cos(angle * (Math.PI / 180.0F));
        double sin = Math.sin(angle * (Math.PI / 180.0F));
        return new Vector3((float) (
                x * cos - y * sin),
                y,
                (float) (x * sin + z * cos));
    }

    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
