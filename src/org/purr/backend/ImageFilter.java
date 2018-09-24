package org.purr.backend;

import org.purr.enums.FilteringKernel;

import java.awt.image.BufferedImage;

public abstract class ImageFilter
{
    protected BufferedImage meanShiftImg, regionFocusImg, outputImg;
    protected FilteringKernel currentKernel;
    protected PixelFilter pixelFilter;
    protected static final int FACTOR_MS=1, FACTOR_RF=1;
    protected static final float GLOBAL_STRENGTH=1.1f;

    public ImageFilter(BufferedImage meanShiftImg, BufferedImage regionFocusImg, BufferedImage output)
    {
        this.meanShiftImg = meanShiftImg;
        this.regionFocusImg = regionFocusImg;
        this.outputImg = output;
        currentKernel = FilteringKernel.EUCLID_DIST_RGB;
        pixelFilter = new PixelFilter();
    }

    public FilteringKernel getCurrentKernel()
    {
        return currentKernel;
    }

    public void setCurrentKernel(FilteringKernel currentKernel)
    {
        this.currentKernel = currentKernel;
    }

    public BufferedImage computeFilter()
    {
        int h = meanShiftImg.getHeight();
        int w = meanShiftImg.getWidth();

        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                try
                {
                    int pixelRGB_MS = meanShiftImg.getRGB(x, y);
                    int pixelRGB_RF = regionFocusImg.getRGB(x,y);
                    outputImg.setRGB(x, y, outputKernel(pixelRGB_MS,pixelRGB_RF));
                } catch (Exception e)
                {
//                    e.printStackTrace();
                }
            }
        }
        return outputImg;
    }

    protected abstract int outputKernel(int pixelRGB_MS, int pixelRGB_RF);
}
