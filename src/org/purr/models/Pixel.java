package org.purr.models;

/**
 * Created by Purr on 2017-05-24.
 */
public class Pixel {
    private int r,g,b;

    public Pixel getPixel()
    {
        return this; //TODO find a sensible solution
    }

    public void setPixel(int R, int G, int B)
    {
        r=R;
        g=G;
        b=B;
    }

    public Pixel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Pixel(int[] rgb)
    {
//        if (rgb.length < 3) {
//            this.r = 0;
//            this.g = 0;
//            this.b = 0;
//        } else {
//            this.r = rgb[0];
//            this.g = rgb[1];
//            this.b = rgb[2];
//        }
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
