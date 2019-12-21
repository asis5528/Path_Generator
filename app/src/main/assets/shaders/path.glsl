#version 310 es

#extension GL_EXT_geometry_shader : enable
#define PI 3.141592653589793238
precision mediump float;
layout (lines_adjacency) in;
layout (triangle_strip,max_vertices = 400) out;
out vec3 col;

in mat4 cut[];
in mat4 cut1[];
in mat4 cut2[];
in mat4 cut3[];

uniform vec2 res;
uniform float line_width;
uniform int division_count;





        void main(){
                  //previous point
                  vec2 prev = gl_in[0].gl_Position.xy;
                  //start point
                  vec2 start = gl_in[1].gl_Position.xy;
                  //end point
                  vec2 end = gl_in[2].gl_Position.xy;
                  //next point
                  vec2 next = gl_in[3].gl_Position.xy;

                  //previous line vector
                  vec2 v0 = normalize(start-prev);
                  //current line vector
                  vec2 v1 = normalize(end-start);
                  //next line vector
                  vec2 v2 = normalize(next-end);

                  //normal of previous line
                  vec2 nor0 = vec2(-v0.y,v0.x);
                  //normal of current line
                  vec2 nor1 = vec2(-v1.y,v1.x);
                  //normal of next line
                  vec2 nor2 = vec2(-v2.y,v2.x);
                  //miter vector between previous line and current line
                  vec2 miter_a = normalize(nor0+nor1);
                  //miter vector between current line and next line
                  vec2 miter_b = normalize(nor1+nor2);
                  //angle between previous line and current line
                  float an1 = dot(miter_a,nor1);
                  //angle between current line and  next line
                  float bn1 = dot(miter_b,nor1);


                  float length_a = line_width/an1;
                  float length_b = line_width/bn1;
                  //offset for start
                  vec2 ofa = length_a*miter_a;
                  //offset for end
                  vec2 ofb = length_b*miter_b;
                  //if distance between previous and start coordinate is smaller than episilon,it means they have same value and are first index coordinates
                  if(length(start-prev)<0.0001){
                        ofa = line_width*nor1;
                  }
                  //if distance between end and next coordinate is smaller than episilon,it means they have same value and are last index coordinates
                  if(length(end-next)<0.0001){
                        ofb = line_width*nor1;
                  }



                  vec2 x,x1,y,y1;
                  //overall calutation of path
                  x =start+ofa;
                  x1  = start-ofa;
                  y = end+ofb;
                  y1 = end-ofb;

                  //if angle between previous line and current line is smaller than 10 degree,we simply offset it to normal direction
                  if((an1*180./PI)<10.){


                        x = start.xy+nor1*line_width;
                        x1=start.xy-nor1*line_width;




                  }
                  //if angle between next line and current line is smaller than 10 degree,we simply offset it to normal direction
                   if((bn1*180./PI)<10.){


                        y = end.xy+nor1*line_width;
                        y1=end.xy-nor1*line_width;

                  }
                   //getting subdivision offset
                   vec2 m = (x1-x)/float(division_count);
                   vec2 m1 = (y1-y)/float(division_count);

                   //subdivision data from user
                   mat4 cu = cut[1];
                   mat4 cu1 = cut1[1];
                   mat4 cu2 = cut2[1];
                   mat4 cu3 = cut3[1];
                   int i1,i2;
                   float val;
                   //looping for subdivision path draw
                   for(int i = 0;i<division_count;i++){
                       //indexing between multiple matrices

                       if(i>47){
                           int ind = i-48;
                           i1 = int(floor(float(ind/4)));
                           i2 = ind-4*i1;
                           val = cu3[i1][i2];

                      }
                      else if(i>31){
                          int ind = i-32;
                          i1 = int(floor(float(ind/4)));
                          i2 = ind-4*i1;
                          val = cu2[i1][i2];

                     }

                      else if(i>15){
                           int ind = i-16;
                           i1 = int(floor(float(ind/4)));
                           i2 = ind-4*i1;
                           val = cu1[i1][i2];


                     }
                     else{
                           i1 = int(floor(float(i/4)));
                           i2 = i-4*i1;
                           val = cu[i1][i2];
                     }


                       //drawing triangle/polygon
                       gl_Position = vec4(x+m*float(i),0., 1.0)*val;
                       col =gl_Position.xyz;
                       EmitVertex();


                       gl_Position = vec4(x+m*(float(i)+1.), 0.,1.0)*val;
                       col = gl_Position.xyz;
                       EmitVertex();


                       gl_Position =vec4(y+m1*float(i),0., 1.0)*val;
                       col = gl_Position.xyz;
                       EmitVertex();

                       gl_Position = vec4(y+m1*(float(i)+1.), 0.,1.0)*val;
                       col = gl_Position.xyz;
                       EmitVertex();

                       EndPrimitive();

                   }




  }