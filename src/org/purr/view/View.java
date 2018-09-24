package org.purr.view;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.purr.backend.*;
import org.purr.enums.ImageBufferType;
import org.purr.exceptions.MissingArgumentException;
import org.purr.models.ImageBuffer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Purr on 2017-05-29.
 */
public class View extends JFrame
{
    private String imgPath;
    private ImagePlus img;
    private int width = 0, height = 0;
    private ArrayList<ImageBuffer> imageBuffer;

    public View(String imgPath) throws HeadlessException
    {
        this.imgPath = imgPath;
        this.img = new ImagePlus(imgPath);
        width = img.getWidth();
        height = img.getHeight();
        imageBuffer = new ArrayList<>();
        imageBuffer.add(new ImageBuffer(ImageBufferType.STARTIMAGE_BUFFER, openImage(imgPath)));
    }

    public ImagePlus getImg()
    {
        return img;
    }

    public void setImg(ImagePlus img)
    {
        this.img = img;
    }

    public boolean displayFinishedImage(ImageBufferType type)
    {
        int index = isInBuffer(type);
        if (index != -1)
        {
            showBuffer(index);
            return true;
        }
        return false;
    }

    public void makeGreyscale()
    {
        makeImage(ImageBufferType.GREYSCALE_BUFFER);
//        ImageBuffer buffer;
//        int index = isInBuffer(ImageBufferType.GREYSCALE_BUFFER);
//        if (index > -1) {
//            showBuffer(index);
//            return;
//        } else {
//            buffer = new ImageBuffer(ImageBufferType.GREYSCALE_BUFFER,
//                    new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
//            imageBuffer.add(buffer);
//        }
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                buffer.getImg().setRGB(x, y, getRGBFromPixel(GreyScale.getGreyPixel(img.getPixel(x, y))));
//            }
//        }
//        showBuffer(imageBuffer.size() - 1);
    }

    private void makeImage(ImageBufferType type)
    {
        ImageBuffer buffer = null;
        int index = addToBuffer(type, buffer);
        buffer = imageBuffer.get(index);
        try
        {
            getResult(type, buffer);
        } catch (MissingArgumentException e)
        {
            e.printStackTrace();
        }
//        showBuffer(index);
    }

    private int addToBuffer(ImageBufferType type, ImageBuffer buffer)
    {
        int index = isInBuffer(type);
        if (index > -1)
        {
            return index;
        }
        else
        {
            if (type==ImageBufferType.SIDE_BY_SIDE_BUFFER)
            {
                buffer = new ImageBuffer(type, new BufferedImage(width*2,height,BufferedImage.TYPE_INT_RGB));
            }
            else
            {
                buffer = new ImageBuffer(type, new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
            }

            imageBuffer.add(buffer);
            return isInBuffer(type);
        }
    }

    private void getResult(ImageBufferType type, ImageBuffer buffer) throws MissingArgumentException
    {
//        System.out.println("BufferSize: "+imageBuffer.size());
        if (buffer == null) throw new MissingArgumentException();
        switch (type)
        {
            case STARTIMAGE_BUFFER:
                break;
            case GREYSCALE_BUFFER:
                putGreyscale(buffer);
                break;
            case MEANSHIFT_BUFFER:
                putMeanShiftBuffer(buffer);
                //regionController.addNewBuffer(buffer);
                break;
            case FOCUSMAP_BUFFER:
                putFocusMap(buffer);
                break;
            case REGION_BUFFER:
                putRegionMapResult(buffer);
                break;
            case BRIGHTNESSMAP_BUFFER: // fall through
            case DFMAP_BUFFER:
                putBrightnessAndDFMap(buffer);
                break;
            case PARALLAX_LEFT_BUFFER:
                break;
            case PARALLAX_RIGHT_BUFFER:
                putStereoscopicPair();
                computeStencilFiltering();
                break;
            case SIDE_BY_SIDE_BUFFER:
                putSideBySide(buffer);
                break;
            case ANAGLYPH_BUFFER:
                putAnaglyph(buffer);
            default:
        }
    }

    private void putAnaglyph(ImageBuffer buffer) throws MissingArgumentException
    {
        BufferedImage LeftImg = getBufferOfType(ImageBufferType.PARALLAX_LEFT_BUFFER).getImg();
        BufferedImage RightImg = getBufferOfType(ImageBufferType.PARALLAX_RIGHT_BUFFER).getImg();
        AnaglyphController anaglyphController = new AnaglyphController(LeftImg,RightImg,buffer.getImg());
        anaglyphController.computeAnaglyphImg();
        saveImage(buffer.getImg(),"_8_anaglyph_RED_CYAN");
    }

    private void putSideBySide(ImageBuffer SBSBuffer) throws MissingArgumentException
    {
        BufferedImage LeftImg = getBufferOfType(ImageBufferType.PARALLAX_LEFT_BUFFER).getImg();
        BufferedImage RightImg = getBufferOfType(ImageBufferType.PARALLAX_RIGHT_BUFFER).getImg();

            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    try
                    {
                        SBSBuffer.getImg().setRGB(x,y,LeftImg.getRGB(x,y));
                        SBSBuffer.getImg().setRGB(width+x,y,RightImg.getRGB(x,y));
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        e.printStackTrace();
                        System.out.println("x = " + x +", y = " + y +", w+x = " + (width+x));
                    }
                }
            }
        saveImage(SBSBuffer.getImg(),"_6_SBS_");
        BufferedImage hd= new BufferedImage(1920,1079,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = hd.createGraphics();
        Image scaledImage = SBSBuffer.getImg().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        graphics2D.drawImage(SBSBuffer.getImg(),0,0,1920,1079,null);
//        graphics2D.drawImage(SBSBuffer.getImg(),0,0,1920,1079,null);
        graphics2D.dispose();
        saveImage(hd,"_7_HD_");
    }

    private void computeStencilFiltering() throws MissingArgumentException
    {
        BufferedImage OrigImg = getBufferOfType(ImageBufferType.STARTIMAGE_BUFFER).getImg();
        BufferedImage LeftImg = getBufferOfType(ImageBufferType.PARALLAX_LEFT_BUFFER).getImg();
        BufferedImage RightImg = getBufferOfType(ImageBufferType.PARALLAX_RIGHT_BUFFER).getImg();
        StencilController stencilController = new StencilController(OrigImg,LeftImg);
        stencilController.runStencilFiltering();
        stencilController = new StencilController(OrigImg,RightImg);
        stencilController.runStencilFiltering();
        //showBuffer(isInBuffer(ImageBufferType.PARALLAX_LEFT_BUFFER));
//        showBuffer(isInBuffer(ImageBufferType.PARALLAX_RIGHT_BUFFER));
        saveImage(LeftImg,"_5_stencilFiltered_FINAL_LEFT");
        saveImage(RightImg,"_5_stencilFiltered_FINAL_RIGHT");
    }

    private void putStereoscopicPair() throws MissingArgumentException
    {

        BufferedImage OrigImg = getBufferOfType(ImageBufferType.STARTIMAGE_BUFFER).getImg();
        BufferedImage DFMap = getBufferOfType(ImageBufferType.DFMAP_BUFFER).getImg();
        BufferedImage LeftImg = getBufferOfType(ImageBufferType.PARALLAX_LEFT_BUFFER).getImg();
        BufferedImage RightImg = getBufferOfType(ImageBufferType.PARALLAX_RIGHT_BUFFER).getImg();
        StereoscopeController stereoscopeController = new StereoscopeController(OrigImg, DFMap, LeftImg, RightImg);
        stereoscopeController.createBaseImages();
//        showBuffer(isInBuffer(ImageBufferType.PARALLAX_LEFT_BUFFER));
//        showBuffer(isInBuffer(ImageBufferType.PARALLAX_RIGHT_BUFFER));
        saveImage(LeftImg,"_4_parallaxLEFT");
        saveImage(RightImg,"_4_parallaxRIGHT");
    }

    private void putBrightnessAndDFMap(ImageBuffer buffer) throws MissingArgumentException
    {
        if (buffer == null) throw new MissingArgumentException();
        BrightnessMap brightnessMap = new BrightnessMap(getBufferOfType(ImageBufferType.MEANSHIFT_BUFFER).getImg(),
                                                        getBufferOfType(ImageBufferType.REGION_BUFFER).getImg(),
                                                        buffer.getImg());
        buffer.setImg(brightnessMap.computeFilter());
        saveImage(buffer.getImg(),"_3_brightness_DF_MapResult");
    }

    private void putRegionMapResult(ImageBuffer buffer) throws MissingArgumentException
    {
        if (buffer == null) throw new MissingArgumentException();
        RegionController regionController = new RegionController(getBufferOfType(ImageBufferType.MEANSHIFT_BUFFER),
                                                                 getBufferOfType(ImageBufferType.FOCUSMAP_BUFFER));
        buffer.setImg(regionController.run());
        saveImage(buffer.getImg(),"_2_regionMapResult");
    }

    private void putGreyscale(ImageBuffer buffer) throws MissingArgumentException
    {
        if (buffer == null) throw new MissingArgumentException();
        PixelFilter pixelFilter = new PixelFilter();
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                buffer.getImg().setRGB(x, y, pixelFilter.getRGBFromPixel(GreyScale.getGreyPixel(img.getPixel(x, y))));
            }
        }
        saveImage(buffer.getImg(), "_greyscale");
//        showBuffer(isInBuffer(ImageBufferType.GREYSCALE_BUFFER));
    }

    private void putFocusMap(ImageBuffer buffer) throws MissingArgumentException
    {
        if (buffer == null) throw new MissingArgumentException();
        FocusExecutor focusExecutor = new FocusExecutor(imgPath);
        try
        {
            focusExecutor.run();
            String focusMapPath = focusExecutor.getResultImagePath();
            if (focusMapPath != null)
            {
                buffer.setImg(openImage(focusMapPath));
//                ImagePlus focusMap = new ImagePlus(focusMapPath);
//                focusMap.show();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void putMeanShiftBuffer(ImageBuffer buffer) throws MissingArgumentException
    {
        if (buffer == null) throw new MissingArgumentException();
        buffer.setImg(MeanShift.filterRGBImage(img));
        saveImage(buffer.getImg(),"_1_meanShift");
    }

    public void makeMeanShift()
    {
        makeImage(ImageBufferType.MEANSHIFT_BUFFER);
    }

    public void makeFocusMap()
    {
        makeImage(ImageBufferType.FOCUSMAP_BUFFER);
    }

    public void makeRegionMap()
    {
        makeImage(ImageBufferType.REGION_BUFFER);
    }

    public void makeBrightnessAndDFMap()
    {
        makeImage(ImageBufferType.DFMAP_BUFFER);
    }

    public void makeStereoscopicImagePair()
    {
        makeImage(ImageBufferType.PARALLAX_LEFT_BUFFER);
        makeImage(ImageBufferType.PARALLAX_RIGHT_BUFFER);
    }

    public void makeAnaglyph()
    {
        makeImage(ImageBufferType.ANAGLYPH_BUFFER);
    }
    private int isInBuffer(ImageBufferType type)
    {
        int i = 0;
        while (i < imageBuffer.size())
        {
            if (imageBuffer.get(i).getID() == type)
            {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void showBuffer(int index)
    {
        ImagePlus newimg = new ImagePlus(imageBuffer.get(index).getID().toString(), imageBuffer.get(index).getImg());
//        System.out.println(imageBuffer.get(index).getImg());
        newimg.show();
    }

    private BufferedImage openImage(String filePath)
    {
        try
        {
            File file= new File(filePath);
            System.out.println("file = " + file.getCanonicalPath());
            return ImageIO.read(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private ImageBuffer getBufferOfType(ImageBufferType type) throws MissingArgumentException
    {
        for (ImageBuffer buf : imageBuffer)
        {
            if (buf.getID() == type)
            {
                return buf;
            }
        }
        throw new MissingArgumentException(); //if no such buffer
    }

    private void saveImage (BufferedImage image, String suffix)
    {
        String outputFilePath = imgPath + suffix + ".jpg";
        File outputfile = new File(outputFilePath);
        try
        {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void exportSBS()
    {
        makeImage(ImageBufferType.SIDE_BY_SIDE_BUFFER);
    }
}
