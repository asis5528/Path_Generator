package com.example.pathgenerator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.opengl.GLES30;
import android.opengl.GLES31;
import android.opengl.GLES31Ext;
import android.opengl.GLSurfaceView;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private int ShaderProgram;
    private int vaoID[];
    private int count;
    private float width;
    private float height;

    private static Context context;
    private int line_location;
    private int count_location;
    public  Bitmap sb;

    private   int gr[];
    private int vboID[];
    public  float coordinates[];
    public  float subdivision_data[];
    public float line_width;
    public int division_count;
    public boolean opengl_initialization = false;

    private int vertexShader;
    private int fragmentShader;
    private int geometryShader;

    @Override


    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        int ii[]=new int[1];
        //Checking max count of vertex attributes available for this device
        GLES31.glGetIntegerv(GLES31.GL_MAX_VERTEX_ATTRIBS,ii,0);
        com.example.pathgenerator.Bitam.max_att = ii[0];


        vaoID = new int[1];
        vboID = new int[1];
        gr = new int[1];

        //generation of buffer data for subdivision path data
        GLES31.glGenBuffers(1,gr,0);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,gr[0]);
        //Uploading buffer data for subdivision path data to gpu
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,0);

        //enable blending for transparency
        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA,GLES31.GL_ONE_MINUS_SRC_ALPHA);

       //generating vertex array object
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
        GLES31.glBindVertexArray(vaoID[0]);
        GLES31.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0);
        GLES31.glBindVertexArray(0);

        //vertex shader source
        String VertexSource = ReadFromfile("shaders/path.vert",context);
        //fragment shader source
        String FragmentSource = ReadFromfile("shaders/path.frag",context);
        //geometry shader source
        String GeometrySource = ReadFromfile("shaders/path.glsl",context);


        //creation of vertex shader
        //creation of vertex shader
        vertexShader =  createShader(VertexSource, GLES31.GL_VERTEX_SHADER);
        fragmentShader =  createShader(FragmentSource, GLES31.GL_FRAGMENT_SHADER);
        geometryShader =  createShader(GeometrySource, GLES31Ext.GL_GEOMETRY_SHADER_EXT);

        //shader program
        ShaderProgram = GLES31.glCreateProgram();
        GLES31.glAttachShader(ShaderProgram,vertexShader);
        GLES31.glAttachShader(ShaderProgram,fragmentShader);
        GLES31.glAttachShader(ShaderProgram,geometryShader);

        int[] compiled = new int[1];







        //binding vertex attribute location
        bindLocations(ShaderProgram);

        GLES31.glLinkProgram(ShaderProgram);

        GLES31.glGetProgramiv(ShaderProgram,GLES31.GL_LINK_STATUS,compiled,0);
        //Path width
        line_location = GLES31.glGetUniformLocation(ShaderProgram,"line_width");
        //Subdivision count per coordinates
        count_location = GLES31.glGetUniformLocation(ShaderProgram,"division_count");



        //check if shader linking is compiled or not
        if(compiled[0]==0){
            Log.e ( "not", GLES31.glGetProgramInfoLog ( ShaderProgram ) );

        }



    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

        width = i;
        height = i1;
        //resolution of opengl
        GLES31.glViewport(0,0,512,512);
        GLES31.glUseProgram(ShaderProgram);
        int res = GLES31.glGetUniformLocation(ShaderProgram,"res");
        System.out.println(i);
        System.out.println(i1);

        GLES31.glUniform2f(res,width,height);



    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        boolean state = com.example.pathgenerator.Bitam.getState();
        if(!state) {

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


            GLES31.glClearColor(0.0f, 0.0f, 0.0f, 0);
            GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);
            GLES31.glBindVertexArray(vaoID[0]);
            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, gr[0]);
            setupAttributes(division_count);
            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);
            GLES31.glDrawArraysInstanced(GLES31Ext.GL_LINE_STRIP_ADJACENCY_EXT, 0, count, 1);
            GLES31.glUseProgram(ShaderProgram);


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
                    int pb = (pix >> 16) & 0xff;
                    int pr = (pix << 16) & 0x00ff0000;
                    int pix1 = (pix & 0xff00ff00) | pr | pb;
                    bt[(h - k - 1) * w + j] = pix1;
                }
            }
            //creating bitmap from buffer

            sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
            //putting generated bitmap to static function from class Bitam.java
            com.example.pathgenerator.Bitam.bi(sb);
            //setting state true as generation is completed
            com.example.pathgenerator.Bitam.setState(true);

        }
        opengl_initialization = true;


    }
    public OpenGLRenderer(Context context){
        this.context = context;
    }
    public OpenGLRenderer(){

    }
    private void bindLocations(int shaderProgram){
        GLES31.glBindAttribLocation ( ShaderProgram, 0, "pos" );
        GLES31.glBindAttribLocation ( ShaderProgram, 1, "a" );
        GLES31.glBindAttribLocation ( ShaderProgram, 2, "b" );
        GLES31.glBindAttribLocation ( ShaderProgram, 3, "c" );
        GLES31.glBindAttribLocation ( ShaderProgram, 4, "d" );
        GLES31.glBindAttribLocation ( ShaderProgram, 5, "e" );
        GLES31.glBindAttribLocation ( ShaderProgram, 6, "f" );
        GLES31.glBindAttribLocation ( ShaderProgram, 7, "g" );
        GLES31.glBindAttribLocation ( ShaderProgram, 8, "h" );
        GLES31.glBindAttribLocation ( ShaderProgram, 9, "i" );
        GLES31.glBindAttribLocation ( ShaderProgram, 10,"j" );
        GLES31.glBindAttribLocation ( ShaderProgram, 11,"k" );
        GLES31.glBindAttribLocation ( ShaderProgram, 12,"l" );
        GLES31.glBindAttribLocation ( ShaderProgram, 13,"m" );
        GLES31.glBindAttribLocation ( ShaderProgram, 14,"n" );
        GLES31.glBindAttribLocation ( ShaderProgram, 15,"o" );
        GLES31.glBindAttribLocation ( ShaderProgram, 16,"p" );

    }
    private void setupAttributes(int division_count){
        //If subdivison data is greater than 48,we provide 64 subdivision data to gpu
        if(division_count>48){
            for(int i =1;i<17;i++){
                GLES31.glEnableVertexAttribArray(i);
                GLES31.glVertexAttribPointer(i,4,GLES31.GL_FLOAT,false,4*4*4*3,4*4*(i-1));
            }
        }
        //If subdivison data is greater than 32,we provide 48 subdivision data to gpu
        else if(division_count > 32){
            for(int i =1;i<13;i++){
                GLES31.glEnableVertexAttribArray(i);
                GLES31.glVertexAttribPointer(i,4,GLES31.GL_FLOAT,false,4*4*4*3,4*4*(i-1));
            }
            //disable unnecessary vertex attributes
            GLES31.glDisableVertexAttribArray(13);
            GLES31.glDisableVertexAttribArray(14);
            GLES31.glDisableVertexAttribArray(15);
            GLES31.glDisableVertexAttribArray(16);


        }
        //If subdivison data is greater than 16,we provide 32 subdivision data to gpu
        else if(division_count >16){
            for(int i = 1;i<9;i++){
                GLES31.glEnableVertexAttribArray(i);
                GLES31.glVertexAttribPointer(i,4,GLES31.GL_FLOAT,false,4*4*4*2,4*4*(i-1));
            }
            //disable unnecessary vertex attributes
            GLES31.glDisableVertexAttribArray(9);
            GLES31.glDisableVertexAttribArray(10);
            GLES31.glDisableVertexAttribArray(11);
            GLES31.glDisableVertexAttribArray(12);
            GLES31.glDisableVertexAttribArray(13);
            GLES31.glDisableVertexAttribArray(14);
            GLES31.glDisableVertexAttribArray(15);
            GLES31.glDisableVertexAttribArray(16);
        }
        //we provide 16 subdivision data to gpu
        else {
            for(int i=1;i<5;i++){

                GLES31.glEnableVertexAttribArray(i);
                GLES31.glVertexAttribPointer(i,4,GLES31.GL_FLOAT,false,4*4*4*1,4*4*(i-1));
            }
            //disable unnecessary vertex attributes
            GLES31.glDisableVertexAttribArray(5);
            GLES31.glDisableVertexAttribArray(6);
            GLES31.glDisableVertexAttribArray(7);
            GLES31.glDisableVertexAttribArray(8);
            GLES31.glDisableVertexAttribArray(9);
            GLES31.glDisableVertexAttribArray(10);
            GLES31.glDisableVertexAttribArray(11);
            GLES31.glDisableVertexAttribArray(12);
            GLES31.glDisableVertexAttribArray(13);
            GLES31.glDisableVertexAttribArray(14);
            GLES31.glDisableVertexAttribArray(15);
            GLES31.glDisableVertexAttribArray(16);
        }
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

    private   String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {

            fIn = context.getResources().getAssets()
                    .open(fileName);

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
