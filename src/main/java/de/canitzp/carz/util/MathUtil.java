package de.canitzp.carz.util;

/**
 * Math utility class
 * http://planning.cs.uiuc.edu/node102.html
 * @author MisterErwin
 */
public class MathUtil {

    /**
     * Rotate a vector around the origin and get the x-part
     *
     * @param x        x-part of input vector
     * @param y        y-part of input vector
     * @param z        z-part of input vector
     * @param cosYaw   cos of yaw
     * @param sinYaw   sin of yaw
     * @param cosPitch cos of pitch
     * @param sinPitch sin of pitch
     * @param cosRoll  cos of roll
     * @param sinRoll  sin of roll
     * @return the x-part
     */
    public static double rotX(double x, double y, double z, double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll) {
        return x * cosYaw * cosRoll
                + y * (cosYaw * sinRoll * cosPitch + sinYaw * sinPitch)
                + z * (cosYaw * sinRoll * sinPitch - sinYaw * cosPitch);
    }

    /**
     * Rotate a vector around the origin and get the y-part
     * @param x x-part of input vector
     * @param y y-part of input vector
     * @param z z-part of input vector
     * @param cosYaw cos of yaw
     * @param sinYaw sin of yaw
     * @param cosPitch cos of pitch
     * @param sinPitch sin of pitch
     * @param cosRoll cos of roll
     * @param sinRoll sin of roll
     * @return the y-part
     */
    public static double rotY(double x, double y, double z, double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll) {
        return -x * sinRoll
                + y * cosRoll * cosPitch
                + z * cosRoll * sinPitch;
    }

    /**
     * Rotate a vector around the origin and get the z-part
     * @param x x-part of input vector
     * @param y y-part of input vector
     * @param z z-part of input vector
     * @param cosYaw cos of yaw
     * @param sinYaw sin of yaw
     * @param cosPitch cos of pitch
     * @param sinPitch sin of pitch
     * @param cosRoll cos of roll
     * @param sinRoll sin of roll
     * @return the z-part
     */
    public static double rotZ(double x, double y, double z, double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll) {
        return x * sinYaw * cosRoll
                + y * (sinYaw * sinRoll * cosPitch - cosYaw * sinPitch)
                + z * (sinYaw * sinRoll * sinPitch + cosYaw * cosPitch);
    }

    /**
     * Rotate a vector around the origin and get the x-part (only a yaw-rotation/y-Axis)
     * @param x x-part of input vector
     * @param y y-part of input vector
     * @param z z-part of input vector
     * @param cosYaw cos of yaw
     * @param sinYaw sin of yaw
     * @return the x-part
     */
    public static double rotX(double x, double y, double z, double cosYaw, double sinYaw) {
        return x * cosYaw - z * sinYaw;
    }

    /**
     * Rotate a vector around the origin and get the y-part (only a yaw-rotation/y-Axis)
     * @param x x-part of input vector
     * @param y y-part of input vector
     * @param z z-part of input vector
     * @param cosYaw cos of yaw
     * @param sinYaw sin of yaw
     * @return the y-part
     */
    public static double rotY(double x, double y, double z, double cosYaw, double sinYaw) {
        return y;
    }

    /**
     * Rotate a vector around the origin and get the z-part (only a yaw-rotation/y-Axis)
     * @param x x-part of input vector
     * @param y y-part of input vector
     * @param z z-part of input vector
     * @param cosYaw cos of yaw
     * @param sinYaw sin of yaw
     * @return the z-part
     */
    public static double rotZ(double x, double y, double z, double cosYaw, double sinYaw) {
        return x * sinYaw + z * cosYaw;
    }
}
