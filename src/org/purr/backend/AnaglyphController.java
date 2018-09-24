package org.purr.backend;

import java.awt.image.BufferedImage;

public class AnaglyphController
{
    private BufferedImage leftImg, rightImg, anaglyphImg;
    private int width, height;
    private PixelFilter pixelFilter;

    public AnaglyphController(BufferedImage leftImg, BufferedImage rightImg, BufferedImage anaglyphImg)
    {
        this.leftImg = leftImg;
        this.rightImg = rightImg;
        this.anaglyphImg = anaglyphImg;
        pixelFilter = new PixelFilter();
        width = leftImg.getWidth();
        height = leftImg.getHeight();
    }

    public void computeAnaglyphImg()
    {
        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                anaglyphImg.setRGB(w,h,
                                   mixAnaglyphChannels(
                                        leftImg.getRGB(w,h),
                                        rightImg.getRGB(w,h)));
            }
        }
    }

    private int mixAnaglyphChannels(int leftRGB, int rightRGB)
    {
        int r = pixelFilter.getRED(rightRGB);
        int g = pixelFilter.getGREEN(leftRGB);
        int b = pixelFilter.getBLUE(leftRGB);
        return pixelFilter.getRGB(r,g,b);
    }
}
