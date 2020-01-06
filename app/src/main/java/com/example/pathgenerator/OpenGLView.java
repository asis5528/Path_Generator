package com.example.pathgenerator;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.opengles.GL10;

public class OpenGLView extends GLSurfaceView{
    public  OpenGLRenderer renderer;
    public OpenGLView(Context context) {
        super(context);
        init();
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        setEGLContextClientVersion(3);
        setPreserveEGLContextOnPause(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);

        renderer = new OpenGLRenderer(getContext());
       // GL10 gl;

        setRenderer(renderer);
    }
}
