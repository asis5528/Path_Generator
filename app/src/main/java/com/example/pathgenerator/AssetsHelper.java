package com.example.pathgenerator;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsHelper {

    private Context context;

    public AssetsHelper(Context context) {
        this.context = context;
    }

    public String readFromfile(String fileName) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {

            fIn = context.getResources().getAssets().open(fileName);

            System.out.println("play");
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";

            while ((line = input.readLine()) != null) {
                returnString.append(System.getProperty("line.separator"));
                System.out.println(line);
                returnString.append(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getMessage();
            e.printStackTrace();

        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }
}
