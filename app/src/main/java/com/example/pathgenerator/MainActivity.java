package com.example.pathgenerator;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private OpenGLView openGLView;
    static boolean closeLoop = false;

    ImageView image;

    private static int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for making window fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //getting openglView
        openGLView = (OpenGLView) findViewById(R.id.openGLView);
        //openGLView.setAlpha(0.8888f);

        //getting imageview
        image = (ImageView) findViewById(R.id.imageView5);
        //passing context to openglrenderer for importing shader files from assets folde
        startThread();
    }


    void startThread() {
        new Thread(threadRunnable).start();
        new Thread(threadRunnable).start();
        new Thread(threadRunnable).start();
        new Thread(threadRunnable).start();
    }


    Runnable threadRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {

                try {

                    float[] updated_coordinates;
                    float[] updated_subdivision;

//                    updated_coordinates = new float[]{0.0f, 0.0f, -0.0f, -0.0f, 1.0f, 2.0f, 3.0f, 3.0f, 5.0f, 2.0f, 6.0f, 0.0f, 6.5f, -2.0f, 9.0f, -2.0f, 9.0f, 3.f, 6.0f, 3.f, -9.5f, -9.5f, -9.5f, -9.5f};
                    //a is updated in updated_coordinates[]

                    //Path Subdivision data
//                    updated_subdivision = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

                    List<Point> array = new ArrayList<Point>();
                    array.add(new Point(-256, -256));
                    array.add(new Point(-256, -256));
                    array.add(new Point(-128, 128));
                    array.add(new Point(-254, -254));
                    array.add(new Point(-121, 121));
                    array.add(new Point(0, 0));
                    array.add(new Point(0, 256));
                    array.add(new Point(128, 0));
                    array.add(new Point(0, -256));
                    array.add(new Point(0, -128));
                    array.add(new Point(128, 128));
                    array.add(new Point(200, 0));
                    array.add(new Point(256, 256));
                    array.add(new Point(256, 256));


                    updated_coordinates = new float[array.size() * 2 ];

                    updated_subdivision = new float[ (array.size()) * 16];

                    for (int i = 0 ; i < array.size(); i++) {
                        updated_coordinates[i * 2] = array.get(i).x / 256.00f;
                        updated_coordinates[i * 2 + 1] = array.get(i).y / 256.00f;
                        updated_subdivision[i * 16] = 1f;

                        for (int jg = 1; jg < 16; jg++) {
                            updated_subdivision[i * 16 + jg] = 0f;
                        }
                    }


 /*                   updated_coordinates = new float[]{
                            -1.0f * 10, -1.0f * 10,
                            -1.0f * 10, -1.0f * 10,
                            -0.5f * 10, 0.5f * 10,
                            0.0f * 10, 0.0f * 10,
                            0.0f * 10, 1.0f * 10,
                            0.5f * 10, 0.0f * 10,
                            0.0f * 10, -1.0f * 10,
                            0.0f * 10, -0.5f * 10,
                            0.5f * 10, 0.5f * 10,
                            0.7f * 10, 0.0f * 10,
                            1.0f * 10, 1.0f * 10,
                            1.0f * 10, 1.0f * 10,
                    };
*/


                    if (true) {


 /*                       updated_coordinates = new float[]{ -0.639146f, 0.366832f, -0.639146f, 0.366832f, -0.644068f, 0.373449f,
                                -0.648877f, 0.379914f, -0.653875f, 0.386631f, -0.658759f, 0.393197f, -0.663587f, 0.399687f, -0.668415f
                                , 0.406177f, -0.673545f, 0.413072f, -0.678467f, 0.419689f, -0.683389f, 0.426306f, -0.683389f, 0.426306f};*/


/*
                        updated_coordinates = new float[]{ -0.63915f, 0.36683f, -0.63915f, 0.36683f, -0.64407f, 0.37345f,
                                -0.64888f, 0.37991f, -0.65388f, 0.38663f, -0.65876f, 0.39320f, -0.66359f, 0.39969f, -0.66842f
                                , 0.40618f, -0.67355f, 0.41307f, -0.67847f, 0.41969f, -0.68339f, 0.42631f, -0.68339f, 0.42631f};

*/


                        updated_coordinates = new float[]{
                                0.3f, -0.9f,
                                0.3f, -0.9f,

                                -0.65876f, 0.3932f,
                                -0.66359f, 0.39969f,
                                -0.66842f, 0.40618f,

                                -1.1f, 0.98f,
                                -1.1f, 0.98f
                        };

                        updated_coordinates = new float[]{
                                0.29155f, -0.6961f,
                                0.29155f, -0.6961f,
                                0.20237267f, -0.5279025f,
                                0.17981756f, -0.4853418f,
                                0.15687752f, -0.44205475f,
                                0.00112f, -0.1481f,
                                0.00112f, -0.1481f
                        };


                        updated_coordinates = new float[]{
                                1.955f, -1.629f,
                                1.955f, -1.629f,

                                0.695f, -0.028f,
                                0.616f, 0.07f,
                                0.537f, 0.168f,

                                -0.797f, 1.87f,
                                -0.797f, 1.87f};



                        updated_coordinates = new float[]{

                                // start point
                                0.57f, -1.16f,
                                0.57f, -1.16f,

                                // first Hole
                                0.41f, -0.91f,
                                0.38f, -0.86f,
                                0.35f, -0.81f,

                                // second Hole
                                -0.14f, 0.11f,
                                -0.17f, 0.17f,
                                -0.2f, 0.23f,

                                // third Hole
                                -0.31f, 0.44f,
                                -0.34f, 0.5f,
                                -0.37f, 0.56f,

                                // end point
                                -0.4f, 0.61f,
                                -0.4f, 0.61f
                        };


                        for (int i = 0; i < updated_coordinates.length; i++) {
                            updated_coordinates[i] = updated_coordinates[i] ;
                        }

                        updated_subdivision = new float[(updated_coordinates.length) * 16];
                        for (int i = 0; i < updated_coordinates.length - 1; i++) {
                            updated_subdivision[i * 16] = 1f;

                            for (int jg = 1; jg < 16; jg++) {
                                updated_subdivision[i * 16 + jg] = 1f;
                            }
                        }
                    }

                    openGLView.getRenderer().pushNewDataset(updated_coordinates, updated_subdivision, 0.111f,
                            new OpenGLRenderer.TileProviderInterface() {
                        @Override
                        public void bitmapDone(final Bitmap bitmap) {
                            image.post(new Runnable() {
                                @Override
                                public void run() {
                                    image.setImageBitmap(bitmap);
                                }
                            });

                        }
                    });


                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };




}
