package com.example.pathgenerator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private OpenGLView openGLView;
    static boolean closeLoop = false;
    private static double a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        //for making window fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        //getting openglView
        openGLView = (OpenGLView) findViewById(R.id.openGLView);
        final OpenGLRenderer renderer = openGLView.renderer;

        //set openglview alpha to 0 so that it won't be seen in screen
        //openGLView.setAlpha(0.0f);

        //getting imageview
        final ImageView image = (ImageView) findViewById(R.id.imageView5);

        //new thread for updating imageview
        new Thread(new Runnable() {
            @Override
            public void run() {


                float vertices[] = {
                       0
                };
                //Path Subdivision data
                float subdivision[] = {0


                };
                //initialization of opengl data ,so they all are zero..once state is true you can update values
                renderer.subdivision_data = subdivision;
                renderer.coordinates = vertices;
                renderer.line_width = 0;
                renderer.division_count = 0;


                while (true) {



                    //state of opengl whether it finished rendering or not
                     boolean state = com.example.pathgenerator.Bitam.getState();

                    if (state) {

                        image.post(new Runnable() {
                            @Override

                            public void run() {
                                //path coordinates
                                float[] updated_coordinates;
                                float[] updated_subdivision;

                                List<Point> array = new ArrayList<Point>();
                                int k = (int) (-154-Math.round(a));
                                array.add(new Point(-256, -256));
                                array.add(new Point(-256, -256));
                                array.add(new Point(-128, 128));
                                //array.add(new Point(-128, 128));// problematic area
                                array.add(new Point(124+k, -254));
                                array.add(new Point(124, 128));
                                array.add(new Point(124, 128));// problematic area
                              //  array.add(new Point(-121, 121)); // problematic area
                               // array.add(new Point(0, 0));
                              //  array.add(new Point(0, 256));
                              //  array.add(new Point(128, 0));
                               // array.add(new Point(0, -256));
                               // array.add(new Point(0, -128));
                              //  array.add(new Point(128, 128));
                               // array.add(new Point(200, 0));
                                array.add(new Point(256, 256));
                               // //array.add(new Point(256, 256));

                                a+=0.0005f;

                                updated_coordinates = new float[array.size() * 2 ];
                                updated_subdivision = new float[ (array.size()) * 16];

                                for (int i = 0 ; i < array.size(); i++) {
                                    updated_coordinates[i * 2] = array.get(i).x / (256.00f);
                                    updated_coordinates[i * 2 + 1] = array.get(i).y / (256.00f);
                                    updated_subdivision[i * 16] = 1f;

                                    for (int jg = 1; jg < 16; jg++) {
                                        updated_subdivision[i * 16 + jg] = 0f;
                                    }
                                }

                                //update the subdivision data here
                                renderer.subdivision_data = updated_subdivision;
                                //update the path coordinates data
                                renderer.coordinates = updated_coordinates;
                                //update the line width
                                renderer.line_width = 0.05f;
                                // //update subdivision count here
                                renderer.division_count = 1;




                                //getting the last generated bitmap from opengl
                                Bitmap bm = com.example.pathgenerator.Bitam.getVal();
                                //replacing current image with newly generated bitmap
                                image.setImageBitmap(bm);

                                //setting state false so that opengl render with new updated data
                                com.example.pathgenerator.Bitam.setState(false);

                                closeLoop = true;


                            }
                        });
                    }
                    if (closeLoop) {
                        // break;
                    }


                }


            }
        }).start();

    }



}

