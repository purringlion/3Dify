package org.purr.backend;

import org.purr.enums.DIRECTION_2D;
import org.purr.exceptions.MissingArgumentException;
import org.purr.models.Pixel2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class StencilController
{
    /**
     * Compute stencil filtering
     */

    private BufferedImage origImg, objectiveImg;
    private final int w;
    private final int h;
    private final int MAX_NEIGHBORS;
    private final int INDICATOR_COLOR;
    private final int TEST_COLOR;
    private final PixelFilter pixelFilter;

    public StencilController(BufferedImage origImg, BufferedImage objectiveImg)
    {
        this.origImg = origImg;
        this.objectiveImg = objectiveImg;
        w = objectiveImg.getWidth();
        h = objectiveImg.getHeight();
        MAX_NEIGHBORS = 4;
        INDICATOR_COLOR = Color.MAGENTA.getRGB();
        TEST_COLOR = Color.CYAN.getRGB();
        pixelFilter = new PixelFilter();
    }

    public void runStencilFiltering()
    {
//        int[] tally = new int[5];
        int stencilResult=0;
        // for each pixel (magenta)
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                if (objectiveImg.getRGB(x, y) == INDICATOR_COLOR)
                {
                    // run VN neighbour discovery
                    ArrayList<Pixel2> neighbors = VonNeumannNeighbors(x, y);
//                    tally[neighbors.size()]++;
//                    for (Pixel2 p : neighbors)
//                    {
////                        //TEST: color all neighbors CYAN
//                        objectiveImg.setRGB(p.getPosition().x, p.getPosition().y, TEST_COLOR);
//                    }
                    // compute the resulting pixel
                    neighbors.add(new Pixel2(origImg.getRGB(x,y),new Point(x,y)));
                    stencilResult = computeStencilValue(neighbors);
                    // put result pixel back into original position
                    objectiveImg.setRGB(x,y,stencilResult);
                }
            }
        }
//        for (int i = 0; i < tally.length; i++)
//        {
//            System.out.println("tally [" + i + "]= " + tally[i]);
//        }
    }

    private ArrayList<Pixel2> VonNeumannNeighbors(int x, int y)
    {
        ArrayList<Pixel2> VNN = new ArrayList<>();
        for (DIRECTION_2D direction : DIRECTION_2D.values())
        {
            Pixel2 p = getNeighborInDirection(x, y, direction);
            if (p != null)
            {
                VNN.add(p);
            }
        }
        return VNN;
    }

    private Pixel2 getNeighborInDirection(int x, int y, DIRECTION_2D direction)
    {
        switch (direction)
        {
            case NORTH:
                return getNeighborNORTH_linear(x, y);
            case SOUTH:
                return getNeighborSOUTH_linear(x, y);
            case EAST:
                return getNeighborEAST_linear(x, y);
            case WEST:
                return getNeighborWEST_linear(x, y);
        }
        return null;
    }

//    private Pixel2 getNeighborSOUTH(int x, int y) throws MissingArgumentException
//    {
////        System.out.print("StencilController.getNeighborSOUTH # ");
////        System.out.println("x = [" + x + "], y = [" + y + "]");
//        if (objectiveImg.getRGB(x, h - 1) == INDICATOR_COLOR)
//        {
//            /**
//             * special case: no neighbors in this direction
//             */
//            return null;
//        }
//        int high = h - 1, low = y;
//        int newY, rgb;
//        while (high >= low)
//        {
//            newY = (high + low) / 2;
//            rgb = objectiveImg.getRGB(x, newY);
//            if (rgb == INDICATOR_COLOR)
//            {
//                /**
//                 * Pixel is part of the hole, try further
//                 */
//                low = newY + 1;
//            }
//            else
//            {
//                /**
//                 * Pixel is part of the actual image. But is it on the boundary?
//                 */
//                if (objectiveImg.getRGB(x, newY - 1) == INDICATOR_COLOR)
//                {
//                    /**
//                     * If the candidate pixel has a neighboring pixel of the indicator color,
//                     * it has to be the one on the boundary -> that's what we're looking for
//                     */
//                    return new Pixel2(rgb, new Point(x, newY));
//
//                }
//                else
//                {
//                    /**
//                     * Pixel not boundary, try closer to original pixel
//                     */
//                    high = newY - 1;
//                }
//            }
//        }
//        throw new MissingArgumentException();
//    }
//
//    private Pixel2 getNeighborNORTH(int x, int y) throws MissingArgumentException
//    {
////        System.out.print("StencilController.getNeighborNORTH # ");
////        System.out.println("x = [" + x + "], y = [" + y + "]");
//        if (objectiveImg.getRGB(x, 0) == INDICATOR_COLOR)
//        {
//            System.out.println("182");
////            System.out.println("StencilController.getNeighborNORTH");
////            System.out.println("x = [" + x + "], y = [" + y + "]");
//            /**
//             * special case: no neighbors in this direction
//             */
//            return null;
//        }
//        int high = y, low = 0;
//        int newY, rgb;
//        while (cnt < 10 && high >= low)
//        {
//            cnt++;
////            System.out.print("high = " + high);
////            System.out.print(" low = " + low);
//            newY = (high + low) / 2;
////            System.out.println(" newY = " + newY);
//            rgb = objectiveImg.getRGB(x, newY);
//            if (rgb == INDICATOR_COLOR)
//            {
////                System.out.println("199 rgb = " + rgb);
//                /**
//                 * Pixel is part of the hole, try further
//                 */
//                low = newY + 1;
//            }
//            else
//            {
////                System.out.println("207");
//                /**
//                 * Pixel is part of the actual image. But is it on the boundary?
//                 */
//                if (objectiveImg.getRGB(x, newY + 1) == INDICATOR_COLOR)
//                {
////                    System.out.println("213");
//                    /**
//                     * If the candidate pixel has a neighboring pixel of the indicator color,
//                     * it has to be the one on the boundary -> that's what we're looking for
//                     */
//                    return new Pixel2(rgb, new Point(x, newY));
//
//                }
//                else
//                {
////                    System.out.println("223");
//                    /**
//                     * Pixel not boundary, try closer to original pixel
//                     */
//                    high = newY - 1;
//                }
//            }
//        }
////        System.out.println("231, MAE");
//        throw new MissingArgumentException();
//    }
//
//    private Pixel2 getNeighborEAST(int x, int y) throws MissingArgumentException
//    {
////        System.out.print("StencilController.getNeighborEAST # ");
////        System.out.println("x = [" + x + "], y = [" + y + "]");
//        if (objectiveImg.getRGB(w - 1, y) == INDICATOR_COLOR)
//        {
//            /**
//             * special case: no neighbors in this direction
//             */
//            return null;
//        }
//        int high = w - 1, low = x;
//        int newX, rgb;
//        while (high >= low)
//        {
//            newX = (high + low) / 2;
//            rgb = objectiveImg.getRGB(newX, y);
//            if (rgb == INDICATOR_COLOR)
//            {
//                /**
//                 * Pixel is part of the hole, try further
//                 */
//                low = newX + 1;
//            }
//            else
//            {
//                /**
//                 * Pixel is part of the actual image. But is it on the boundary?
//                 */
//                if (objectiveImg.getRGB(newX - 1, y) == INDICATOR_COLOR)
//                {
//                    /**
//                     * If the candidate pixel has a neighboring pixel of the indicator color,
//                     * it has to be the one on the boundary -> that's what we're looking for
//                     */
//                    return new Pixel2(rgb, new Point(newX, y));
//
//                }
//                else
//                {
//                    /**
//                     * Pixel not boundary, try closer to original pixel
//                     */
//                    high = newX - 1;
//                }
//            }
//        }
//        throw new MissingArgumentException();
//    }
//
//    private Pixel2 getNeighborWEST(int x, int y) throws MissingArgumentException
//    {
////        System.out.print("StencilController.getNeighborWEST # ");
////        System.out.println("x = [" + x + "], y = [" + y + "]");
//        if (objectiveImg.getRGB(0, y) == INDICATOR_COLOR)
//        {
//            /**
//             * special case: no neighbors in this direction
//             */
//            return null;
//        }
//        int high = x, low = 0;
//        int newX, rgb;
//        while (high >= low)
//        {
//            newX = (high + low) / 2;
//            rgb = objectiveImg.getRGB(newX, y);
//            if (rgb == INDICATOR_COLOR)
//            {
//                /**
//                 * Pixel is part of the hole, try further
//                 */
//                low = newX + 1;
//            }
//            else
//            {
//                /**
//                 * Pixel is part of the actual image. But is it on the boundary?
//                 */
//                if (objectiveImg.getRGB(newX + 1, y) == INDICATOR_COLOR)
//                {
//                    /**
//                     * If the candidate pixel has a neighboring pixel of the indicator color,
//                     * it has to be the one on the boundary -> that's what we're looking for
//                     */
//                    return new Pixel2(rgb, new Point(newX, y));
//
//                }
//                else
//                {
//                    /**
//                     * Pixel not boundary, try closer to original pixel
//                     */
//                    high = newX - 1;
//                }
//            }
//        }
//        throw new MissingArgumentException();
//    }

    private Pixel2 getNeighborNORTH_linear(int x, int y)
    {
        int newY = y - 1;
        while (newY > 0)
        {
            int rgb = objectiveImg.getRGB(x, newY);
            if (rgb != INDICATOR_COLOR)
            {
                return new Pixel2(rgb, new Point(x, newY));
            }
            newY--;
        }
        return null;
    }

    private Pixel2 getNeighborSOUTH_linear(int x, int y)
    {
        int newY = y + 1;
        while (newY < h)
        {
            int rgb = objectiveImg.getRGB(x, newY);
            if (rgb != INDICATOR_COLOR)
            {
                return new Pixel2(rgb, new Point(x, newY));
            }
            newY++;
        }
        return null;
    }

    private Pixel2 getNeighborWEST_linear(int x, int y)
    {
        int newX = x - 1;
        while (newX > 0)
        {
            int rgb = objectiveImg.getRGB(newX, y);
            if (rgb != INDICATOR_COLOR)
            {
                return new Pixel2(rgb, new Point(newX, y));
            }
            newX--;
        }
        return null;
    }

    private Pixel2 getNeighborEAST_linear(int x, int y)
    {
        int newX = x + 1;
        while (newX < w)
        {
            int rgb = objectiveImg.getRGB(newX, y);
            if (rgb != INDICATOR_COLOR)
            {
                return new Pixel2(rgb, new Point(newX, y));
            }
            newX++;
        }
        return null;
    }

    private int computeStencilValue(ArrayList<Pixel2> stencil)
    {
        int len = stencil.size();

        if (len==1)
        {
            return stencil.get(0).getRgb();
        }

        int[] R = new int[len];
        int[] G = new int[len];
        int[] B = new int[len];

        for (int i = 0; i < len; i++)
        {
            R[i] = pixelFilter.getRED(stencil.get(i).getRgb());
            G[i] = pixelFilter.getGREEN(stencil.get(i).getRgb());
            B[i] = pixelFilter.getBLUE(stencil.get(i).getRgb());
        }

        Arrays.sort(R);
        Arrays.sort(G);
        Arrays.sort(B);

        int median = len/2;

        if (len%2==0)
        {
            return pixelFilter.getRGB((R[median]+R[median-1])/2,
                                      (G[median]+G[median-1])/2,
                                      (B[median]+B[median-1])/2);
        }
        else
        {
            return pixelFilter.getRGB(R[median],G[median], B[median]);
        }

    }

}

