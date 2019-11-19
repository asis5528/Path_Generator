package com.example.pathgenerator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;


public class MainActivity extends Activity {
    private OpenGLView openGLView;
    static boolean closeLoop = false;
    private static int a;


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
                                float updated_coordinates[] = {
                                        0.0f, 0.0f, -0.0f, -0.0f,
                                        1.0f, 2.0f, 3.0f, 3.0f, 5.0f, 2.0f + a / 5000.f, 6.0f, 0.0f, 6.5f, -2.0f, 9.0f, -2.0f, 9.0f, 3.f, 6.0f, 3.f, -9.5f, -9.5f, -9.5f, -9.5f
                                };
                                //a is updated in updated_coordinates[]
                                a += 1;
                                //Path Subdivision data
                                float updated_subdivision[] = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                                        1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                                        1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                                        1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                                        1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                                        1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                                        1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                                        1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                                        1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                                        1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                                        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                                        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f


                                };

                                //update the subdivision data here
                                renderer.subdivision_data = updated_subdivision;
                                //update the path coordinates data
                                renderer.coordinates = updated_coordinates;
                                //update the line width
                                renderer.line_width = 0.04f;
                                // //update subdivision count here
                                renderer.division_count = 16;




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

