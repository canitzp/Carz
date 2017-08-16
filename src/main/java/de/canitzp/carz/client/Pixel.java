package de.canitzp.carz.client;

import io.netty.buffer.ByteBuf;

import java.awt.*;

/**
 * @author canitzp
 */
public class Pixel {

    public static final Pixel EMPTY = new Pixel(-1, -1, -1, -1);

    private int r = -1, g = -1, b = -1, a = -1;

    public Pixel(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Pixel(ByteBuf buf) {
        this.fromBytes(buf);
    }

    public Pixel(String codedString){
        this.fromString(codedString);
    }

    public Pixel(int hex){
        Color c = new Color(hex);
        this.r = c.getRed();
        this.g = c.getGreen();
        this.b = c.getBlue();
        this.a = c.getAlpha();
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

    public int toHex(){
        if(isValid()){
            return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
        }
        return -1;
    }

    private static final String pad(String s) {
        return (s.length() == 1) ? "0" + s : s;
    }

    public boolean isValid(){
        return this != EMPTY && this.r >= 0 && this.g >= 0 && this.b >= 0 && this.a >= 0;
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

    public String writeString(){
        return String.format("%d,%d,%d,%d;", r, g, b, a);
    }

    public void fromString(String s){
        s = s.replace(";", "");
        String[] split = s.split(",");
        if(split.length == 4){
            this.r = Integer.parseInt(split[0]);
            this.g = Integer.parseInt(split[1]);
            this.b = Integer.parseInt(split[2]);
            this.a = Integer.parseInt(split[3]);
        }
    }
}
