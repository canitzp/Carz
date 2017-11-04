package de.canitzp.carz.util;

/**
 * Math utility class
 * http://planning.cs.uiuc.edu/node102.html
 * @author MisterErwin
 */
public class MathUtil {
    
    public static double rotX(double x, double y, double z, double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll) {
        return x * cosYaw * cosRoll
                + y * (cosYaw * sinRoll * cosPitch + sinYaw * sinPitch)
                + z * (cosYaw * sinRoll * sinPitch - sinYaw * cosPitch);
    }

    public static double rotY(double x, double y, double z, double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll) {
        return -x * sinRoll
                + y * cosRoll * cosPitch
                + z * cosRoll * sinPitch;
    }

    public static double rotZ(double x, double y, double z, double cosYaw, double sinYaw, double cosPitch, double sinPitch, double cosRoll, double sinRoll) {
        return x * sinYaw * cosRoll
                + y * (sinYaw * sinRoll * cosPitch - cosYaw * sinPitch)
                + z * (sinYaw * sinRoll * sinPitch + cosYaw * cosPitch);
    }

    public static double rotX(double x, double y, double z, double cosYaw, double sinYaw) {
        return x * cosYaw - z * sinYaw;
    }

    public static double rotY(double x, double y, double z, double cosYaw, double sinYaw) {
        return y;
    }

    public static double rotZ(double x, double y, double z, double cosYaw, double sinYaw) {
        return x * sinYaw + z * cosYaw;
    }
}
