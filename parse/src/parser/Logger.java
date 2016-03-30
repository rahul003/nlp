package parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by rahulrh on 3/30/16.
 */
public class Logger {
    public static void openOutfile(BufferedWriter out, String savePath){
        try
        {
            FileWriter fstream = new FileWriter(savePath, true); //true tells to append data.
            out = new BufferedWriter(fstream);
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void log(BufferedWriter out, String s){
        if(out==null)
            System.out.println(s);
        else {
            try {
                out.write(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeOutfile(BufferedWriter out){
        //close testScores output file
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
