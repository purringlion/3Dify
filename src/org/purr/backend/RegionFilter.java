package org.purr.backend;

import org.purr.enums.FilteringKernel;
import org.purr.models.Pixel2;
import org.purr.models.RegionMap;
import org.purr.models.RegionMap.Region;

import java.awt.*;
import java.util.Arrays;

public class RegionFilter extends PixelFilter{
    /* Take a region and compute a pixel value from all of its pixels */

    public int computeResultingRGB(Region region, FilteringKernel kernel)
    {
        int result;
        switch (kernel)
        {
            /*case SIMPLE_AVG:
                result = computeSimpleAvg(region);
                break;*/
            case MEDIAN:
                result = computeMedian(region);
                break;
            case MODE:
                result = computeMode(region);
                break;
            case GREYSCALE_SIMPLE_AVG:
                result = computeSimpleAvg_GREY(region);
                break;
            default:
                result=0;
                break;
        }
        return result;
    }

    private int computeSimpleAvg_GREY(Region region) {
        int result=0, i=0;
        for (Pixel2 pixel : region.getPoints())
        {
            //result += convertToRGB_GREY(pixel.getRgb());
            result += convertToGREY(pixel.getRgb());
            i++;
        }
        /* If no pixels were found, avoid zero division */
        return ( i==0 ? 0 : getRGBFromPixel_GREY(result/i) );
    }

    /*
    private int computeSimpleAvg(Region region) {
        int result=0;
        int[] RGB = new int[3];
        int points = 0;
        for (Pixel2 pixel : region.getPoints())
        {
            RGB = convertToRGBArrayFast(pixel.getRgb());

        }

        return result;
    }
    */
    private int computeMedian(Region region)
    {
        int size=region.getPoints().size();
        int[] values = new int[size];
        for (int i = 0; i < region.getPoints().size(); i++)
        {
            values[i]= convertToRGB_GREY(region.getPoints().get(i).getRgb());
        }

        Arrays.sort(values);

        int median = size/2;

        if (size%2==0)
        {
            return getGREYFromSingleComponent((values[median]+values[median-1])/2);
        }
        else
        {
            return getGREYFromSingleComponent(values[median]);
        }
    }

    private int computeMode(Region region)
    {
        return 0;
    }


}
