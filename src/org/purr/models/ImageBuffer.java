package org.purr.models;

import org.purr.enums.ImageBufferType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Purr on 2017-05-24.
 */
public class ImageBuffer {
    private BufferedImage img;
    private ImageBufferType ID;

    public ImageBuffer(ImageBufferType ID, BufferedImage buf)
    {
        this.img = buf;
        this.ID = ID;
    }

    public BufferedImage getImg()
    {
        return img;
    }

    public void setImg(BufferedImage img)
    {
        this.img = img;
    }

    public ImageBufferType getID()
    {
        return ID;
    }

//    public ArrayList<Pixel2> parseToPixel2d ()
//    {
//        int w=img.getWidth();
//        int h=img.getHeight();
//        ArrayList<Pixel2> pixels = new ArrayList<>();
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                pixels.add(new Pixel2(img.getRGB(i,j),new Point(i,j)));
//            }
//        }
//        return pixels;
//    }
}

