package org.purr.backend;


/**
 * Created by Purr on 2017-05-30.
 */
public class GreyScale {
    public static int[] getGreyPixel(int[] original)
    {
        if (original == null) throw new IllegalArgumentException("Pixel null");
        else if (original.length == 1) return original;
        else if (original.length < 3) throw new IllegalArgumentException("Pixel length too short");
        int[] res = new int[3];
        return kernel(original, res);
    }

    private static int[] kernel(int[] original, int[] res)
    {
        return averageGreyKernel(original, res);
    }

    private static int[] averageGreyKernel(int[] original, int[] res)
    {
        int g = (original[0] + original[1] + original[2]) / 3;
        res[0] = g;
        res[1] = g;
        res[2] = g;
        return res;
    }
}
