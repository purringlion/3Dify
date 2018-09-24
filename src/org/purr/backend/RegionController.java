package org.purr.backend;

import org.purr.enums.FilteringKernel;
import org.purr.enums.ImageBufferType;
import org.purr.models.ImageBuffer;
import org.purr.models.Pixel2;
import org.purr.models.RegionMap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Purr on 2017-06-26.
 */
public class RegionController {
    private ImageBuffer meanShiftImg, focusMapImg;
    private RegionMap fromRegionMap, toRegionMap;
    private RegionFilter regionFilter;
    private int w,h;
    private Pixel2[][] test;

    public RegionController(ImageBuffer meanShift, ImageBuffer focusMap)
    {
        meanShiftImg = meanShift;
        focusMapImg = focusMap;
        fromRegionMap = new RegionMap();
        toRegionMap = new RegionMap();
        regionFilter = new RegionFilter();
        w=focusMap.getImg().getWidth();
        h=focusMap.getImg().getHeight();
    }

//    public RegionController(RegionMap fromRegionMap, RegionMap toRegionMap) {
//        this.fromRegionMap = fromRegionMap;
//        this.toRegionMap = toRegionMap;
//    }

//    public RegionController(RegionMap fromRegionMap, Pixel2[][] test) {
//        this.fromRegionMap = fromRegionMap;
//        this.toRegionMap = new RegionMap();
//        this.test=test;
//    }

    public BufferedImage run()
    {
        // fill FROM RegionMap
        addAllPixels();
        // Fill TO map from FROM map
        fillToMap();
        //compute filtered region map (final focus map)
        filterRegionMaps();
        //build image from filtered region map
        return buildResultingImage();
    }

    private BufferedImage buildResultingImage()
    {
        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (RegionMap.Region region : toRegionMap.getRegions())
        {
            for (Pixel2 pixel : region.getPoints())
            {
                    buffer.setRGB(
                            pixel.getPosition().x,
                            pixel.getPosition().y,
                            pixel.getRgb());
            }
        }
        return buffer;
    }

    /* Override toRegionMap in the process to conserve memory */
    private void filterRegionMaps()
    {
        int result=0;
        for (RegionMap.Region region : toRegionMap.getRegions())
        {
                result=regionFilter.computeResultingRGB(region, FilteringKernel.GREYSCALE_SIMPLE_AVG);
            //result=regionFilter.computeResultingRGB(region, FilteringKernel.MEDIAN);
            for (Pixel2 pixel : region.getPoints())
            {
                pixel.setRgb(result);
            }
        }
    }

    private void addAllPixels()
    {
//        get the ID from a pixel
        int ID;
//        add it to the FROM regionMap
        int w = meanShiftImg.getImg().getWidth();
        int h = meanShiftImg.getImg().getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                ID=meanShiftImg.getImg().getRGB(i, j);
                fromRegionMap.addPixelByID(new Pixel2(ID, new Point(i, j)),ID);
            }
        }
    }

    private void fillToMap()
    {

            // get all regions from FROM map
            //go thru each region
            int regionID, x, y, rgb;
            for (RegionMap.Region region : fromRegionMap.getRegions())
            {

                //get regionID
                regionID = region.getID();

                //go thru each pixel in region
                for (Pixel2 pixel : region.getPoints())
                {
                    try
                    {
                    x = pixel.getPosition().x;
                    y = pixel.getPosition().y;
                    rgb = focusMapImg.getImg().getRGB(x, y);
//                rgb=test[x][y].getRgb();
                    //put new pixel into TO map under regionID above
                    toRegionMap.addPixelByID(new Pixel2(rgb, new Point(x, y)), regionID);
                }
                    catch (Exception e)
                    {
//                        e.printStackTrace();
                    }
            }

        }
        System.out.println("fillToMap done!");

    }

//    public void prettyPrint(String map)
//    {
//        if (map.equalsIgnoreCase("from"))
//        {
//            fromRegionMap.prettyPrint();
//        }
//        if (map.equalsIgnoreCase("to"))
//        {
//            toRegionMap.prettyPrint();
//        }
//        else
//        {
//            System.out.println("Bad parameter.");
//        }
//    }

}
