package org.purr.backend;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Purr on 2017-05-31.
 */
public class FocusExecutor {
    private String imgPath;
    private boolean isReady;
    private final static String rootdir = "C:\\j\\debixcon_bin";
    private final static String relativedir = "\\debixcon";
    private static String workingDir;

    public void runFocusExecution() throws IOException
    {
        String s = " This is ProcessBuilder Example";
        String[] cmd = {"cmd.exe", " /c echo ", s};
        // Process p = Runtime.getRuntime().exec(cmd);
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            System.out.println("Run echo command");
            //pb.directory(new File(rootdir));
            pb.directory(new File(relativedir));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int errCode = process.waitFor();
            System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
            System.out.println("Echo Output:\n" + output(process.getInputStream()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String output(InputStream inputStream) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            if (br != null) br.close();
        }
        return sb.toString();
    }

    public String getResultImagePath() //TODO: implement properly
    {
        return isReady ? imgPath + ".output_bl.png" : null;
    }

    public FocusExecutor(String imgPath)
    {
        this.imgPath = imgPath;
        isReady = false;
    }

    public void run() throws InterruptedException
    {
        workingDir = System.getProperty("user.dir");
        ProcessBuilder pb = new ProcessBuilder("cmd.exe");
        System.out.println("Run echo command");
        //pb.directory(new File(rootdir));
        pb.directory(new File(workingDir+relativedir));
        pb.redirectErrorStream(true);
        System.out.println(pb.directory().getAbsolutePath());
        Process p = null;
//        int errCode = p.waitFor();
        try {
            p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get stdin of shell
        BufferedWriter p_stdin =
                new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        // execute the desired command (here: ls) n times
        int n = 1;
        for (int i = 0; i < n; i++) {
            try {
                //single execution
                p_stdin.write("debixcon " + imgPath + " 0");
                p_stdin.newLine();
                p_stdin.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // finally close the shell by execution exit command
        try {
            p_stdin.write("exit");
            p_stdin.newLine();
            p_stdin.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write stdout of shell (=output of all commands)
        Scanner s = new Scanner(p.getInputStream());
        while (s.hasNext()) {
            System.out.println(s.next());
        }
        s.close();

//        if (errCode==0)
//        {
        isReady = true;
//        }
    }

}
