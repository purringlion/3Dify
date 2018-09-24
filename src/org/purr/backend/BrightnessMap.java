package org.purr.backend;

import org.purr.models.ImageBuffer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BrightnessMap extends ImageFilter
{


    public BrightnessMap(BufferedImage meanShiftImg, BufferedImage regionFocusImg, BufferedImage output)
    {
        super(meanShiftImg, regionFocusImg, output);
    }

    @Override
    protected int outputKernel(int pixelRGB_MS, int pixelRGB_RF)
    {
        int brightness = 0;
        int focus = pixelFilter.getGREYfromRGB(pixelRGB_RF);
//        int focus = (pixelRGB_RF & pixelFilter.BITMASK_BLUE);
        switch (currentKernel)
        {
            case EUCLID_DIST_RGB:
                brightness = kernel_euclid(pixelRGB_MS);
                break;
            default:
                return 0;
        }

        return pixelFilter.getGREYFromSingleComponent(linearCombination(brightness, focus));
    }

    private int linearCombination(int brightness, int focus)
    {
        int comb = (int) (((FACTOR_MS * brightness) + (FACTOR_RF * focus)) * GLOBAL_STRENGTH);
        comb =  (comb < 50 ? 0 : comb ); /** to better separate the background*/
//        comb = (int) (comb < 50 ? 0 : comb * 1.25);
        comb = (comb > 255 ? 255 : comb);
//        System.out.print(" " + comb);
        return comb;
    }

    private int kernel_euclid(int pixelRGB_MS)
    {
//        System.out.print(""+pixelRGB+" ");
        /**
         * SQRT of (255-R+255-G+255-B)
         **/
//        double brightness = Math.sqrt(
//                (  255 - ((pixelRGB & pixelFilter.BITMASK_RED) >> 16)) +
//                        ( 255 -  ((pixelRGB & pixelFilter.BITMASK_GREEN) >> 8)) +
//                        ( 255 -  (pixelRGB & pixelFilter.BITMASK_RED))
//        );
        Color c = new Color(pixelRGB_MS);
        return (int) Math.sqrt((255 - c.getRed()) + (255 - c.getGreen()) + (255 - c.getBlue())); //brightness
//        System.out.print(""+brightness+" ");
//        return pixelFilter.getGREYFromSingleComponent((int)brightness);
    }

    private int kernel_psychedelic(int pixelRGB)
    {
        /**
         * SQRT of (R^2+G^2+B^2)
         **/
        return (int) Math.sqrt(
                (((pixelRGB & pixelFilter.BITMASK_RED) >> 16) * ((pixelRGB & pixelFilter.BITMASK_RED) >> 16)) +
                        (((pixelRGB & pixelFilter.BITMASK_GREEN) >> 8) * ((pixelRGB & pixelFilter.BITMASK_GREEN) >> 8)) +
                        ((pixelRGB & pixelFilter.BITMASK_BLUE) * (pixelRGB & pixelFilter.BITMASK_RED))
        );
    }
}
