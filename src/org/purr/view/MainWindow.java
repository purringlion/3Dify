package org.purr.view;

import ij.ImagePlus;
import org.purr.enums.ImageBufferType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame
{
    private View view;
    private FileDialog dialog;
    private JButton button, doneButton, greyScale, focusMap, meanshift, dfMap, stereo, sbs, anaglyph;
    private JLabel textField, signField;
    private String imgPath;
    private final String homeDir;

    public MainWindow() throws HeadlessException
    {
        this.setTitle("3Dify by Judit Tovissy @ tovissy.com");
        setSize(500,525);
        textField = new JLabel();
        signField = new JLabel();
        button = new JButton("Open Image to 3Dify");
        doneButton = new JButton("Done with Image");
        greyScale = new JButton("Show Greyscale Image");
        focusMap = new JButton("Show Focus Map (by MTA SZTAKI)");
        meanshift= new JButton("Show Clusters");
        dfMap= new JButton("Show Depth - Focus Map");
        stereo = new JButton("Show Stereoscopic Images");
        sbs = new JButton("Show Side-By-Side Image");
        anaglyph = new JButton("Show Anaglyph Image");

        setLayout(new GridLayout(11,1));

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                imgPath = openAction();
                textField.setText(imgPath);
                doneButton.setEnabled(true);
                startProcessIfImageValid();
            }
        });

        doneButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (view!=null)
                {
                    deactivateProgressButtons();
                    view.dispose();
                }

            }
        });

        greyScale.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                view.displayFinishedImage(ImageBufferType.GREYSCALE_BUFFER);
            }
        });

        focusMap.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                view.displayFinishedImage(ImageBufferType.FOCUSMAP_BUFFER);
            }
        });

        meanshift.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                view.displayFinishedImage(ImageBufferType.MEANSHIFT_BUFFER);
            }
        });

        dfMap.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                view.displayFinishedImage(ImageBufferType.DFMAP_BUFFER);
            }
        });

        stereo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                view.displayFinishedImage(ImageBufferType.PARALLAX_LEFT_BUFFER);
                view.displayFinishedImage(ImageBufferType.PARALLAX_RIGHT_BUFFER);
            }
        });

        sbs.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                view.displayFinishedImage(ImageBufferType.SIDE_BY_SIDE_BUFFER);
            }
        });

        anaglyph.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                view.displayFinishedImage(ImageBufferType.ANAGLYPH_BUFFER);
            }
        });

        add(textField);
        add(greyScale);
        add(focusMap);
        add(meanshift);
        add(dfMap);
        add(stereo);
        add(sbs);
        add(anaglyph);
        add(button);
        add(doneButton);
        add(signField);
        doneButton.setEnabled(false);
        textField.setText("Now here is some text.");
        signField.setText("j u d i t @ t o v i s s y . c o m");
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        signField.setHorizontalAlignment(SwingConstants.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        deactivateProgressButtons();
        setVisible(true);
        homeDir="c:\\j";
//        imgPath = openAction();
        System.out.println("imgPath = " + imgPath);
    }

    private void startProcessIfImageValid()
    {
        if (imgPath=="err" || imgPath==null)
        {
            textField.setText(textField.getText()+"\nCannot convert.");
            return;
        }
        else
        {
            ImagePlus img = new ImagePlus(imgPath);
            img.show();

            view = new View(imgPath);
            Runnable r = new Runnable() {
                public void run() {
                    synchronizeWithView();
                }
            };

            new Thread(r).start();

//            synchronizeWithView();
//            view.makeGreyscale();
//            view.displayFinishedImage(ImageBufferType.GREYSCALE_BUFFER);
        }
    }

    protected String openAction ()
    {
        File file;
        String localDirectory;
        dialog = new FileDialog (this);
        dialog.setMode (FileDialog.LOAD);
        dialog.setTitle ("Open");
        dialog.setDirectory (homeDir);
        dialog.setVisible (true);
        if (null != dialog.getFile ())
        {
            localDirectory = dialog.getDirectory ();
            file = new File (localDirectory + dialog.getFile ());
            return file.getAbsolutePath ();
        }
        return "err";
    }

    private void deactivateProgressButtons()
    {
        greyScale.setEnabled(false);
        focusMap.setEnabled(false);
        meanshift.setEnabled(false);
        dfMap.setEnabled(false);
        stereo.setEnabled(false);
        sbs.setEnabled(false);
        anaglyph.setEnabled(false);
//        doneButton.setEnabled(true);
    }

    private void synchronizeWithView()
    {
        deactivateProgressButtons();
//        view.displayFinishedImage(ImageBufferType.STARTIMAGE_BUFFER);

        view.makeGreyscale();
        greyScale.setEnabled(true);
//        view.displayFinishedImage(ImageBufferType.GREYSCALE_BUFFER);

        view.makeFocusMap();
        focusMap.setEnabled(true);

        view.makeMeanShift();
        meanshift.setEnabled(true);

        view.makeRegionMap();
        view.makeBrightnessAndDFMap();
        dfMap.setEnabled(true);

        view.makeStereoscopicImagePair();
        stereo.setEnabled(true);

        view.exportSBS();
        sbs.setEnabled(true);

        view.makeAnaglyph();
        anaglyph.setEnabled(true);
    }
}
