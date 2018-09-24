package org.purr.view;

import com.jtattoo.plaf.JTattooUtilities;
import ij.ImagePlus;
import ij.io.Opener;

import javax.swing.*;
import java.io.IOException;

public class Main
{
//    JTattooUtilities laf = new JTattooUtilities();

    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
//            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
//            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        MainWindow mainWindow = new MainWindow();
//        run2();
//        run1();
        System.out.println("Done.");
        System.out.println();
        System.out.println("Press any key to exit.");
        try
        {
            System.in.read();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void run1()
    {
        // ImageJ window=new ImageJ();
//        Opener opener = new Opener();
//        String imgPath = "c:\\j\\profile.jpg";
//        String imgPath = "c:\\j\\scene01s.jpg";
//        String imgPath = "c:\\j\\ju.jpg";
//        ImagePlus img = opener.openImage(imgPath);
//        View view = new View(imgPath);
        //Image img = new Image(imgPath);
//        BufferedImage img=null;
//        try {
//             img = ImageIO.read(new File(imgPath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        view.makeGreyscale();
//        view.makeFocusMap();
//        view.makeMeanShift();
//        try {
//            img.show();
////            view.makeGreyscale();
////            System.in.read();
//            view.makeFocusMap();
////            System.in.read();
//            view.makeMeanShift();
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Pixel2[][] pixels =new Pixel2[2][3];
////
////        RegionMap regionMap=new RegionMap();
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
//                pixels[i][j]=new Pixel2(0,new Point(3,3));
//            }
//        }
//        pixels[0][0].setPosition(new Point(0,0));
//        pixels[0][1].setPosition(new Point(0,1));
//        pixels[0][2].setPosition(new Point(0,2));
//        pixels[1][0].setPosition(new Point(1,0));
//        pixels[1][1].setPosition(new Point(1,1));
//        pixels[1][2].setPosition(new Point(1,2));
////
//        pixels[0][0].setRgb(111111);
//        pixels[0][1].setRgb(111111);
//        pixels[0][2].setRgb(222222);
//        pixels[1][0].setRgb(111111);
//        pixels[1][1].setRgb(222222);
//        pixels[1][2].setRgb(222222);
//
//        RegionMap FRegionMap=new RegionMap();
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
////                regionMap.addNewPixel(pixels[i][j]);
//                FRegionMap.addPixelByID(pixels[i][j],pixels[i][j].getRgb());
//            }
//        }
////
//        System.out.println("RegionMap filled.");
//
//        System.out.println();

//        FRegionMap.prettyPrint();
//
//        RegionController regionController = new RegionController();
//
//        regionController.fillFromMap(regionMap);
//
//        System.out.println("RegionMap passed to RegionController.");
//
//        regionController.prettyPrint("from");
//
//        System.out.println();
//
//        Pixel2[][] pixels2 =new Pixel2[2][3];
//
////        RegionMap focusMap=new RegionMap();
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
//                pixels2[i][j]=new Pixel2(0,new Point(3,3));
//            }
//        }
//        pixels2[0][0].setPosition(new Point(0,0));
//        pixels2[0][1].setPosition(new Point(0,1));
//        pixels2[0][2].setPosition(new Point(0,2));
//        pixels2[1][0].setPosition(new Point(1,0));
//        pixels2[1][1].setPosition(new Point(1,1));
//        pixels2[1][2].setPosition(new Point(1,2));
//
//        pixels2[0][0].setRgb(333333);
//        pixels2[0][1].setRgb(444444);
//        pixels2[0][2].setRgb(444444);
//        pixels2[1][0].setRgb(444444);
//        pixels2[1][1].setRgb(333333);
//        pixels2[1][2].setRgb(333333);
//
////        RegionMap TRegionMap=new RegionMap();
////        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
////                regionMap.addNewPixel(pixels[i][j]);
//                TRegionMap.addPixelByID(pixels[i][j],pixels[i][j].getRgb());
//            }
//        }

//        System.out.println("FocusArray done.");
////        System.out.println("Pivoting map...");
////
//        RegionController regionController = new RegionController(FRegionMap,pixels2);
//        regionController.fillToMap();
//
////        regionController.pivotMaps(pixels2);
//        System.out.println("Pivoting done.");
////
//        regionController.prettyPrint("from");
//        System.out.println();
//        regionController.prettyPrint("to");


//        System.out.println();
//        System.out.println("Press any key.");
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        System.exit(0);
//        view.getImg().show();
//        view.makeGreyscale();
////        window.setVisible(true);
//        img.show();


//        focusExecutor.runFocusExecution();
    }

    private static void run2()
    {
        // ImageJ window=new ImageJ();
        Opener opener = new Opener();
//        String imgPath = "c:\\j\\profile.jpg";
//        String imgPath = "c:\\j\\scene01s.jpg";
//        String imgPath = "c:\\j\\babyPanther_lg.jpg";
//        String imgPath = "c:\\j\\babyPanther_sm.jpg";
        String imgPath = "c:\\j\\ju.jpg";
//        String imgPath = "c:\\j\\xw01S.jpg";
//        String imgPath = "c:\\j\\xw.jpg";
        ImagePlus img = opener.openImage(imgPath);
        View view = new View(imgPath);
        //Image img = new Image(imgPath);

        view.getImg().show();
//        System.out.println("Greyscale ...");
//        view.makeGreyscale();
        System.out.println("Focus map ...");
        view.makeFocusMap();
        System.out.println("Mean shift ....");
        view.makeMeanShift();
        System.out.println("Region map ....");
        view.makeRegionMap();
        System.out.println("Brightness & DF map ...."); //Combined Brightness & DF computation to optimize performance
        view.makeBrightnessAndDFMap();
        System.out.println("Stereoscopic images ....");
        view.makeStereoscopicImagePair();
//        img.show();
    }
}
