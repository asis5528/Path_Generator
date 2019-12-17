package com.example.pathgenerator;

import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLES31;
import android.opengl.GLES31Ext;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//import android.opengl.GLES31Ext;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    public interface TileProviderInterface {
        void bitmapDone(Bitmap bitmap);
    }

    private int shaderProgram;

    private int vaoID[];
    private int count;
    private float width;
    private float height;
    private float t = 0f;
    private int vb = 0;

    private   int gr[];
    private int vboID[];
    public  float coordinates[];
    public  float subdivision_data[];
    public float line_width;
    public int division_count;
    public boolean opengl_initialization = false;
    private int line_location;
    private int count_location;

    private AssetsHelper assetsHelper;

    private int vertexShader;
    private int fragmentShader;
    private int geometryShader;


    private Semaphore semaphore = new Semaphore(1);
    private CountDownLatch countDownLatch;
    private TileProviderInterface providerInterface;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        vaoID = new int[1];
        vboID = new int[1];

        gr = new int[1];


        //generation of buffer data for subdivision path data
        GLES31.glGenBuffers(1,gr,0);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,gr[0]);
        //Uploading buffer data for subdivision path data to gpu
        // GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER,0,0,GLES31.GL_DYNAMIC_DRAW);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,0);


        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA,GLES31.GL_ONE_MINUS_SRC_ALPHA);
        //GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA,GLES31.GL_ONE_MINUS_DST_COLOR);
        GLES31.glGenVertexArrays(1,vaoID,0);
        GLES31.glBindVertexArray(vaoID[0]);
        //generation of buffer data for path's coordinate(vertex data)
        GLES31.glGenBuffers(1,vboID,0);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,vboID[0]);
        //Uploading buffer data for path's coordinate to gpu
        // GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER,0,0,GLES31.GL_DYNAMIC_DRAW);
        GLES31.glVertexAttribPointer(0,2,GLES31.GL_FLOAT,false,2*4,0);
        GLES31.glEnableVertexAttribArray(0);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,0);

        GLES31.glBindVertexArray(0);

        //sending 4 different vector 4 to shader to make 16 float data for each coordinates(subdivision path data)
        GLES31.glBindVertexArray(vaoID[0]);
        GLES31.glEnableVertexAttribArray(2);
        GLES31.glEnableVertexAttribArray(3);
        GLES31.glEnableVertexAttribArray(4);
        GLES31.glEnableVertexAttribArray(5);
        GLES31.glBindBuffer(GLES30.GL_ARRAY_BUFFER,gr[0]);
        GLES31.glVertexAttribPointer(2,4,GLES31.GL_FLOAT,false,4*4*4,0);
        GLES31.glVertexAttribPointer(3,4,GLES31.GL_FLOAT,false,4*4*4,4*4);
        GLES31.glVertexAttribPointer(4,4,GLES31.GL_FLOAT,false,4*4*4,4*8);
        GLES31.glVertexAttribPointer(5,4,GLES31.GL_FLOAT,false,4*4*4,4*12);

        GLES31.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0);
        GLES31.glBindVertexArray(0);

        String vertexShaderSource = assetsHelper.readFromfile("shaders/path.vert");
        String fragmentShaderSource = assetsHelper.readFromfile("shaders/path.frag");
        String geometryShaderSource = assetsHelper.readFromfile("shaders/path.glsl");

        //creation of vertex shader
        vertexShader =  createShader(vertexShaderSource, GLES31.GL_VERTEX_SHADER);
        fragmentShader =  createShader(fragmentShaderSource, GLES31.GL_FRAGMENT_SHADER);
        geometryShader =  createShader(geometryShaderSource, GLES31Ext.GL_GEOMETRY_SHADER_EXT);

        //shader program
        shaderProgram = GLES31.glCreateProgram();
        GLES31.glAttachShader(shaderProgram,vertexShader);
        GLES31.glAttachShader(shaderProgram,fragmentShader);
        GLES31.glAttachShader(shaderProgram,geometryShader);

        GLES31.glBindAttribLocation ( shaderProgram, 0, "pos" );
        GLES31.glBindAttribLocation ( shaderProgram, 1, "gr" );
        GLES31.glBindAttribLocation ( shaderProgram, 2, "a" );
        GLES31.glBindAttribLocation ( shaderProgram, 3, "b" );
        GLES31.glBindAttribLocation ( shaderProgram, 4, "c" );
        GLES31.glBindAttribLocation ( shaderProgram, 5, "d" );

        GLES31.glLinkProgram(shaderProgram);


        int[] compiled = new int[1];
        GLES31.glGetProgramiv(shaderProgram,GLES31.GL_LINK_STATUS,compiled,0);

        GLES31.glGetProgramiv(shaderProgram,GLES31.GL_LINK_STATUS,compiled,0);

        line_location = GLES31.glGetUniformLocation(shaderProgram,"line_width");
        count_location = GLES31.glGetUniformLocation(shaderProgram,"division_count");

        //check if shader linking is compiled or not
        if(compiled[0]==0){
            throw new RuntimeException("compilation failed:" + GLES31.glGetProgramInfoLog(shaderProgram));
        }
    }


    public void pushNewDataset(float[] vertices, float[] subdivisions, float lineWidth, TileProviderInterface providerInterface) throws InterruptedException {
        semaphore.acquire();
        subdivision_data = subdivisions;
        coordinates = vertices;
        line_width = lineWidth;
        division_count = 1;
        this.providerInterface = providerInterface;
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }


    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        width = i;
        height = i1;
        //resolution of opengl
        GLES31.glViewport(0,0,512,512);
        GLES31.glUseProgram(shaderProgram);
        int res = GLES31.glGetUniformLocation(shaderProgram,"res");
        System.out.println(i);
        System.out.println(i1);

        GLES31.glUniform2f(res,width,height);
    }


    private Bitmap extractAndroidBitmapFromGLES() {
        int w = 512;
        int h = 512;
        int b[]=new int[w*(h)];
        int bt[]=new int[w*h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        //reading image from gpu and writing to intbuffer
        GLES31.glReadPixels(0, 0, w, h, GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, ib);
        int k;
        int i;
        for( i=0,  k=0; i<h; i++, k++)
        {//remember, that OpenGL bitmap is incompatible with Android bitmap
            //and so, some correction need.
            for(int j=0; j<w; j++)
            {
                int pix=b[i*w+j];
                int pb=(pix>>16)&0xff;
                int pr=(pix<<16)&0x00ff0000;
                int pix1=(pix&0xff00ff00) | pr | pb;
                bt[(h-k-1)*w+j]=pix1;
            }
        }
        Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;
    }


    private int createShader(String source, int type) {
        int shader = GLES31.glCreateShader(type);
        GLES31.glShaderSource(shader,source);
        GLES31.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES31.glGetShaderiv(shader,GLES31.GL_COMPILE_STATUS,compiled,0);
        if(compiled[0]==0){
            throw new RuntimeException("failed To compile shader " + type + "\n" + GLES31.glGetShaderInfoLog(shader));
        }
        return shader;
    }


    @Override
    public void onDrawFrame(GL10 gl10) {

        if(semaphore.availablePermits() == 0) {
            GLES31.glUniform1f(line_location,line_width);
            GLES31.glUniform1i(count_location,division_count);
            FloatBuffer grbuffer = ByteBuffer.allocateDirect(subdivision_data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            grbuffer.put(subdivision_data).position(0);
            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, gr[0]);
            //Uploading buffer data for subdivision path data to gpu
            GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, grbuffer.capacity() * 4, grbuffer, GLES31.GL_DYNAMIC_DRAW);
            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);

            count = coordinates.length / 2;

            FloatBuffer cbuffer = ByteBuffer.allocateDirect(coordinates.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            cbuffer.put(coordinates).position(0);

            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, vboID[0]);
            //Uploading buffer data for path's coordinate to gpu
            GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, cbuffer.capacity() * 4, cbuffer, GLES31.GL_DYNAMIC_DRAW);

            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);

            GLES31.glClearColor(0, 0, 0, 0f);
            GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);
            GLES31.glBindVertexArray(vaoID[0]);
            GLES31.glDrawArraysInstanced(GLES31Ext.GL_LINE_STRIP_ADJACENCY_EXT, 0, count, 1);
            GLES31.glUseProgram(shaderProgram);
            int time = GLES31.glGetUniformLocation(shaderProgram, "time");
            GLES31.glUniform1f(time, t);

            t += 0.1;


            int w = 512;
            int h = 512;
            int b[] = new int[w * (h)];
            int bt[] = new int[w * h];
            IntBuffer ib = IntBuffer.wrap(b);
            ib.position(0);
            //reading image from gpu and writing to intbuffer
            GLES31.glReadPixels(0, 0, w, h, GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, ib);
            //int k;
            for (int i = 0, k = 0; i < h; i++, k++) {//remember, that OpenGL bitmap is incompatible with Android bitmap
                //and so, some correction need.
                for (int j = 0; j < w; j++) {
                    int pix = b[i * w + j];
                    int pix1;
                    int pb = (pix >> 16) & 0xff;
                    int pr = (pix << 16) & 0x00ff0000;
                    pix1 = (pix & 0xff00ff00) | pr | pb;
                    bt[(h - k - 1) * w + j] = pix1;
                }
            }
            //creating bitmap from buffer

            Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
            providerInterface.bitmapDone(sb);
            countDownLatch.countDown();
            semaphore.release();
        }

        opengl_initialization = true;
    }



    public OpenGLRenderer(AssetsHelper assetsHelper){
        this.assetsHelper = assetsHelper;
    }

}
