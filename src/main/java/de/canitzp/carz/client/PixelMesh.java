package de.canitzp.carz.client;


/**
 * @author canitzp
 */
public class PixelMesh {

    private Pixel[][] pixels;

    public PixelMesh(int size){
        this.pixels = new Pixel[size][size];
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    private boolean isInRange(int line){
        return line >= this.pixels.length;
    }

    private void checkRange(int line){
        if(isInRange(line)){
            throw new RuntimeException("You're trying to get a line of a PixelMesh that hasn't the needed size! You want: " + line + "  We have as maximum: " + (this.pixels.length - 1));
        }
    }

    public Pixel[] getLine(int line){
        checkRange(line);
        return this.pixels[line];
    }

    public PixelMesh setLine(int line, Pixel[] pixels){
        checkRange(line);
        if(this.pixels.length != pixels.length){
            throw new RuntimeException("You want to set a line of Pixels to a PixelMesh, but the sizes are different! Your size: " + pixels.length + "  Size of the Mesh: " + this.pixels.length);
        }
        this.pixels[line] = pixels;
        return this;
    }
}
