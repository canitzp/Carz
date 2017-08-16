package de.canitzp.carz.client;

import io.netty.buffer.ByteBuf;

/**
 * @author canitzp
 */
public class Pixel {

    public static final Pixel EMPTY = new Pixel(-1, -1, -1, -1);

    private int r, g, b, a;

    public Pixel(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Pixel(ByteBuf buf) {
        this.fromBytes(buf);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.r);
        buf.writeInt(this.g);
        buf.writeInt(this.b);
        buf.writeInt(this.a);
    }

    public void fromBytes(ByteBuf buf) {
        this.r = buf.readInt();
        this.g = buf.readInt();
        this.b = buf.readInt();
        this.a = buf.readInt();
    }
}
