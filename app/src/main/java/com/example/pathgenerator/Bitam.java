package com.example.pathgenerator;

import android.graphics.Bitmap;

public class Bitam {
    //bitmap variable
    private static Bitmap a;
    private static boolean state = false;
    private static boolean supportsGeometryShader = false;
    public static int max_att = 0;
    public Bitam(){

    }
    //setting bitmap
    public static void bi(Bitmap val){

        a = val;
    }
    public static void setState(boolean stat){
        state = stat;
    }
    //getting state value whether opengl wrote generated bitmap or not
    public static boolean getState(){
        return state;
    }

    public static void setCompatibilityStatus(boolean state){
        supportsGeometryShader = state;
    }

    public static boolean getCompatibilityStatus(){
        return  supportsGeometryShader;
    }

    //getting opengl generated bitmap
    public static Bitmap getVal(){
        return a;
    }
}
