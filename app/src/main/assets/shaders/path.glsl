#version 310 es

#extension GL_EXT_geometry_shader : enable
#define PI 3.141592653589793238
                precision mediump float;
                layout (lines_adjacency) in;
                layout (triangle_strip,max_vertices = 100) out;
                out vec3 col;
                in float g[];
                in mat4 cut[];
                uniform vec2 res;
                uniform float line_width;
                uniform int division_count;





        void main(){

                  vec2 prev = gl_in[0].gl_Position.xy;
                  vec2 start = gl_in[1].gl_Position.xy;
                  vec2 end = gl_in[2].gl_Position.xy;
                  vec2 next = gl_in[3].gl_Position.xy;


                  vec2 v0 = normalize(start-prev);
                  vec2 v1 = normalize(end-start);
                  vec2 v2 = normalize(next-end);

                  vec2 nor0 = vec2(-v0.y,v0.x);
                  vec2 nor1 = vec2(-v1.y,v1.x);
                  vec2 nor2 = vec2(-v2.y,v2.x);

                  vec2 miter_a = normalize(nor0+nor1);
                  vec2 miter_b = normalize(nor1+nor2);

                  float an1 = dot(miter_a,nor1);
                  float bn1 = dot(miter_b,nor1);
                  // if(an1==0.) an1 = 1.;
                  // if(bn1==0.) bn1 = 1.;

                  float length_a = line_width/an1;
                  float length_b = line_width/bn1;
                  vec2 ofa = length_a*miter_a;
                  vec2 ofb = length_b*miter_b;
                  if(length(start-prev)<0.0001){
                  ofa = line_width*nor1;
                  }
                  if(length(end-next)<0.0001){
                  ofb = line_width*nor1;
                  }



                  vec2 x,x1,y,y1;

                  x =start+ofa;
                  x1  = start-ofa;
                  y = end+ofb;
                  y1 = end-ofb;

                   if((an1*180./PI)<10.){


                    x = start.xy+nor1*line_width;
                    x1=start.xy-nor1*line_width;




                     }
                      if((bn1*180./PI)<10.){
                      //color = vec3(1.,0.,0.);

                  y = end.xy+nor1*line_width;
                  y1=end.xy-nor1*line_width;

                  }

                   vec2 m = (x1-x)/float(division_count);
                   vec2 m1 = (y1-y)/float(division_count);
                   float gra;
                   mat4 cu = cut[1];
                   int ind = 0;
                   for(int i = 0;i<division_count;i++){
                       if(mod(float(i),float(2.))!=0.){

                       gra = 1.;}
                       else{ gra = 0.;}
                       int i1 = int(floor(float(i/4)));
                       int i2 = i-4*i1;
                       float val = cu[i1][i2];
                       val = gra;
                       //if(val<0.0){
                     //  col = vec3(1.,0.,0.);
                    //   }
                    //   else{
                    //   col = vec3
                    //   }
                       gl_Position = vec4(x+m*float(i),0., 1.0)*val;
                       col =gl_Position.xyz;
                       //col = vec3(1.,0.,0.);


                       EmitVertex();
                       gl_Position = vec4(x+m*(float(i)+1.), 0.,1.0)*val;
                       col = gl_Position.xyz;
                        //col = vec3(1.,0.,0.);
                       EmitVertex();
                       gl_Position =vec4(y+m1*float(i),0., 1.0)*val;
                               col = gl_Position.xyz;
                    //col = vec3(0.,1.,0.);
                              EmitVertex();
                            gl_Position = vec4(y+m1*(float(i)+1.), 0.,1.0)*val;
                            col = gl_Position.xyz;
                             //col = vec3(0.,1.,0.);
                                  EmitVertex();
                                 EndPrimitive();
                                 ind+=1;
                   }




                }