package org.purr.backend;

import java.awt.*;

/* get all computations done */
public class PixelFilter {
    protected final static int BITMASK_RED = 16711680;   // 00000000111111110000000000000000
    protected final static int BITMASK_GREEN = 65280;    // 00000000000000001111111100000000
    protected final static int BITMASK_BLUE = 255;       // 00000000000000000000000011111111


    public int[] convertToRGBArray(int rgb)
    {
        Color color = new Color(rgb);
        int[] RGB = new int[3];
        RGB[0] = color.getRed();
        RGB[1] = color.getGreen();
        RGB[2] = color.getBlue();
        return RGB;
    }

    public int[] convertToRGBArrayFast(int rgb)
    {
        int[] RGB = new int[3];
        RGB[0] = (rgb & BITMASK_RED) >>16;
        RGB[1] = (rgb & BITMASK_GREEN)>>8;
        RGB[2] = rgb & BITMASK_BLUE;
        return RGB;
    }

    public int convertToRGB_GREY(int rgb)
    {
        return rgb & BITMASK_BLUE;
    }

    public int convertToGREY(int rgb)
    {
        int[] RGB =convertToRGBArrayFast(rgb);
        return (RGB[0]+RGB[1]+RGB[2])/3;
    }

    public int getGREYfromRGB(int rgb)
    {
        return rgb & BITMASK_BLUE;
    }

    public int getRGBFromPixel_GREY(int pixel)
    {
        return ((pixel << 16) | (pixel << 8) | (pixel));
    }

    public int getRGBFromPixel(int[] pixel)
    {
        return ((pixel[0] << 16) | (pixel[1] << 8) | (pixel[2]));
    }

    /**
     * Propagates value to all 3 color channels, resulting in a grey pixel
     * @param value
     * @return
     */
    public int getGREYFromSingleComponent(int value)
    {
        return ((value << 16) | (value << 8) | (value));
    }

    public int getRED(int rgb)
    {
        return ((rgb & BITMASK_RED) >> 16);
    }

    public int getGREEN(int rgb)
    {
        return ((rgb & BITMASK_GREEN) >> 8);
    }

    public int getBLUE(int rgb)
    {
        return (rgb & BITMASK_BLUE);
    }

    public int getRGB(int red, int green, int blue)
    {
        return ((red << 16) | (green << 8) | (blue));
    }
}
