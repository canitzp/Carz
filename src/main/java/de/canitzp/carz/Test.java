package de.canitzp.carz;

import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by MisterErwin on 17.09.2017.
 * In case you need it, ask me ;)
 */
public class Test {
    public static void main(String[] a) {
        AxisAlignedBB bb1 = new AxisAlignedBB(-2, 0, -2, 2, 1.2, 2);
        AxisAlignedBB bb2 = new AxisAlignedBB(0, 1, 0, 1, 2, 1);
        AxisAlignedBB bb3 = new AxisAlignedBB(0, 2, 0, 1, 3, 1);
        AxisAlignedBB bb4 = new AxisAlignedBB(0, 0.5, 0, 1, 1, 1);
        AxisAlignedBB bb5 = new AxisAlignedBB(0, 0.5, 0, 1, 1, 5);
        AxisAlignedBB bb6 = new AxisAlignedBB(0, 1.3, 0, 1, 5, 1);
        System.out.println(onGround(bb1,bb2));
        System.out.println(onGround(bb1,bb3));
        System.out.println(onGround(bb1,bb4));
        System.out.println(onGround(bb1,bb5));
        System.out.println(onGround(bb1,bb6));
    }

    public static boolean onGround(AxisAlignedBB a, AxisAlignedBB b) {
        if (a.maxX > b.minX && a.minX < b.maxX && a.maxZ > b.minZ && a.minZ < b.maxZ) {
            if (a.minY < b.minY && a.maxY > b.minY-0.2)
                return true;
        }
        return false;
    }
}
