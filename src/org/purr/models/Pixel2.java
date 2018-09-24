package org.purr.models;

import java.awt.*;

/**
 * Created by Purr on 2017-06-06.
 */
public class Pixel2 {
    int rgb;
    Point position;

    public Pixel2(int rgb, Point position)
    {
        this.rgb = rgb;
        this.position = position;
    }

    public int getRgb()
    {
        return rgb;
    }

    public void setRgb(int rgb)
    {
        this.rgb = rgb;
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return "Pixel2{" +
                       "rgb=" + rgb +
                       ", position=" + position +
                       '}';
    }
}
