package org.purr.backend;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StereoscopeController
{
    private BufferedImage OrigImg, ZMap, LeftImg, RightImg;
    private boolean areImagesDone;
    private int w, h;
    private static final int maxParallax = 16, depthScale = 128;
    private PixelFilter pixelFilter;

    /**
     * maxParallax: maximum parallax value (in pixels)
     * depthScale: depth of the 3D effect
     **/

    public StereoscopeController(BufferedImage origImg, BufferedImage ZMap, BufferedImage leftImg,
                                 BufferedImage rightImg)
    {
        OrigImg = origImg;
        this.ZMap = ZMap;
        LeftImg = leftImg;
        RightImg = rightImg;
        pixelFilter = new PixelFilter();
        areImagesDone = false;
        w = OrigImg.getWidth();
        h = OrigImg.getHeight();
    }

    public BufferedImage getLeftImg()
    {
        return LeftImg;
    }

    public BufferedImage getRightImg()
    {
        return RightImg;
    }

    public void createBaseImages() //should change to bool to indicate success
    {
        //region Prepare images (fill with magenta)
        setMagentaBackground(LeftImg);
        setMagentaBackground(RightImg);
        //endregion
        int pixel, parallax, ZValue, newX;
        //foreach pixel
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                pixel = OrigImg.getRGB(x, y);
                //compute parallax value
                ZValue = pixelFilter.getGREYfromRGB(ZMap.getRGB(x, y));
                parallax = computeParallax(ZValue);
                //put pixel in left&right image
                newX = ((x - parallax) >= 0 ? (x - parallax) : 0);
                LeftImg.setRGB(newX, y, pixel);
                newX = ((x + parallax) < w ? (x + parallax) : w - 1);
                RightImg.setRGB(newX, y, pixel);
            }
        }

    }

    private void setMagentaBackground(BufferedImage img)
    {
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.setColor(Color.MAGENTA);
        graphics2D.fillRect(0, 0, w, h);
        graphics2D.dispose();
    }

    /**
     * Returns the parallax distance of a pixel from the original.
     *
     * @param ZValue: pixel value in the DFM
     * @return parallax: number of pixels to shift image (int)
     **/
    private int computeParallax(int ZValue)
    {
        int parallax = maxParallax * (1 - (ZValue / depthScale));
        return (int) Math.round(parallax / 2.0); // shifting value is actually parallax/2
    }

}
