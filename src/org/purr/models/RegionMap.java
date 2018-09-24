package org.purr.models;

import java.util.ArrayList;

/**
 * Created by Purr on 2017-05-24.
 */
public class RegionMap {

    public class Region{
        private int ID;
        private ArrayList<Pixel2> points;

        public Region(int ID) {
            this.ID = ID;
            points= new ArrayList<>();
        }
        public void addPoint(Pixel2 p)
        {
            points.add(p);
        }

        public ArrayList<Pixel2> getPoints()
        {
            return points;
        }

        public int getID() {
            return ID;
        }
        public void prettyPrint()
        {
            System.out.println("    [Region: "+ID);
            for (Pixel2 p : points)
            {
                System.out.println("        Pixel:");
                System.out.println("            pos: ("+p.getPosition().x+","+p.getPosition().y+")");
                System.out.println("            RGB: ("+p.getRgb()+")");

            }
            System.out.println("    ]");
        }
    }

    private ArrayList<Region> regions;

    public RegionMap() {
        regions = new ArrayList<>();
    }

    public int getNumRegions() {
        return regions.size();
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    //    public Region getRegionByIndex(int index) throws InvalidRegionIDException
//    {
//        if ((!(index > 0)) && (index > getNumRegions())) // is a valid index
//        {
//            return regions.get(index);
//        }
//        else
//        {
//            throw new InvalidRegionIDException();
//        }
//    }

//    public Region getRegionByID(int ID) throws InvalidRegionIDException
//    {
//        int index = regionIndex(ID);
//        if (index < 0)
//        {
//            throw new InvalidRegionIDException();
//        }
//        return regions.get(index);
//    }

    public void addPixelByID(Pixel2 pixel, int ID)
    {
        int index = regionIndex(ID);
        if (index<0)
        {
            index = newRegion(ID);
        }
        regions.get(index).addPoint(pixel);
    }




    /* Register new Region and return its index */
    private int newRegion(int ID) {
        Region region = new Region(ID);
        regions.add(region);
        return regionIndex(ID);
    }

    public int regionIndex(int regionID)
    {
        int i=0;
        while (i< regions.size())
        {
            if (regions.get(i).getID()==regionID)
            {
                return i;
            }
            i++;
        }
        return -1;
    }

//    public void prettyPrint()
//    {
//        System.out.println("{ RegionMap: ");
//        for (Region r : regions) {
//            r.prettyPrint();
//        }
//        System.out.println("}");
//    }
}
