#version 310 es

#extension GL_EXT_geometry_shader : enable
#define PI 3.141592653589793238
                precision mediump float;
                layout (lines_adjacency) in;
                layout (triangle_strip,max_vertices = 10) out;
                out vec3 col;
                in float g[];
                in mat4 cut[];
                uniform vec2 res;
                uniform float line_width;
                uniform int division_count;

                const float MiterLimit = 12.75;

                /*

                void main(){
                    vec3 prev = gl_in[0].gl_Position.xyz;
                    vec3 start = gl_in[1].gl_Position.xyz;
                    vec3 end = gl_in[2].gl_Position.xyz;
                    vec3 next = gl_in[3].gl_Position.xyz;
                    vec3 aa = normalize(prev-start);
                    vec3 bb = normalize(end-start);
                    float ang = acos(dot(aa,bb)/(length(aa)*length(bb)));
                    float angle = ang*(180.0/PI);
                    vec3 aaa = normalize(start-end);
                    vec3 bbb = normalize(next-end);
                    float ang1 = acos(dot(aaa,bbb)/(length(aaa)*length(bbb)));
                    float angle1 = ang1*(180.0/PI);

                    vec3 color = vec3(0.,1.,0.);
                    vec2 x,x1,y,y1;
                    //Normal computaion
                     vec3 lhs = cross(normalize(end-start), vec3(0.0, 0.0, 1.0));
                     bool dista = length(end-start)<0.1;
                     bool ndista;
                     bool pdista;
                     if(!dista){
                       ndista = length(next-end)<0.06;
                       pdista = length(start-prev)<0.06;
                     }
                     bool colStart = length(start-prev) ==0.0;
                     bool colEnd = length(end-next) ==0.0;
                     //bool sta = length(next-start)<0.7;
                    //bool enn = length(end-prev)<0.7;
                    vec3 a = normalize(start-prev);
                    vec3 b = normalize(start-end);
                    vec3 c = (a+b)*0.5;
                    vec3 startLhs = normalize(c) * sign(dot(c, lhs));
                    a = normalize(end-start);
                    b = normalize(end-next);
                    c = (a+b)*0.5;
                    vec3 endLhs = normalize(c) * sign(dot(c, lhs));
                    if(colStart)
                        startLhs = lhs;
                    if(colEnd)
                        endLhs = lhs;

                    float startInvScale = dot(startLhs, lhs);
                    float endInvScale = dot(endLhs, lhs);
                    float asp = res.x/res.y;

                    startLhs *= vec3(line_width,line_width*asp,0.0);
                    endLhs *= vec3(line_width,line_width*asp,0.0);
                    vec2 st = start.xy;
                    vec2 stlhs = startLhs.xy;
                    vec2 en = end.xy;
                    vec2 enlhs = endLhs.xy;
                     x  = st+stlhs/startInvScale;

                     x1 = st-stlhs/startInvScale;

                     y =en+enlhs/endInvScale;

                     y1 = en-enlhs/endInvScale;



                    if(angle < 10.0 ^^ dista^^pdista){
                    color = vec3(1.,0.,0.);
                    float t = atan(start.y-prev.y,start.x-prev.x);
                    vec2 offset = vec2(0.0,line_width);
                    offset*=mat2(cos(t),-sin(t),sin(t),cos(t));
                    if(pdista){
                    x = start.xy+offset;
                    x1=start.xy-offset;
                    }
                    else{


                    x = start.xy-offset;
                    x1=start.xy+offset;
  }
                    //y=end.xy+offset;

                    //y1=end.xy-offset;


                    }
                    if(angle1<10.0^^dista^^ndista){
                    color = vec3(1.,0.,0.);
                    float t = atan(end.y-start.y,end.x-start.x);
                    vec2 offset = vec2(0.0,line_width);
                    offset*=mat2(cos(t),-sin(t),sin(t),cos(t));
                    y = end.xy-offset;
                    y1=end.xy+offset;

                    }
                    color = start;
                    vec2 m = (x1-x)/float(division_count);
                    vec2 m1 = (y1-y)/float(division_count);
                    float gra;
                    mat4 cu = cut[1];
                    int ind = 0;
                    for(int i = 0;i<division_count;i++){
                    if(mod(float(i),float(2.))!=0.){

                    gra = 1.;}
                    else{ gra = 1.;}
                    int i1 = int(floor(float(i/4)));
                    int i2 = i-4*i1;
                    float val = cu[i1][i2];
                    gl_Position = vec4(x+m*float(ind),0., 1.0)*val;
                    col =color;



                    EmitVertex();
                    gl_Position = vec4(x+m*(float(ind)+1.), 0.,1.0)*val;
                    col = color;

                    EmitVertex();
                    gl_Position =vec4(y+m1*float(ind),0., 1.0)*val;
                    col = color;

                    EmitVertex();
                    gl_Position = vec4(y+m1*(float(ind)+1.), 0.,1.0)*val;
                    col = color;

                    EmitVertex();
                    EndPrimitive();
                    ind+=1;
}
*/
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
                  float bn1 = dot(miter_b,nor2);
                  if(an1==0.) an1 = 1.;
                  if(bn1==0.) bn1 = 1.;

                  float length_a = line_width/an1;
                  float length_b = line_width/bn1;
                  if(dot(v0,nor1)< - MiterLimit){
                    miter_a = nor1;
                    length_a = line_width;

                    if(dot(v0,nor1)>0.){
                        gl_Position = vec4((start+line_width*nor0),0,1.);
                        EmitVertex();
                        gl_Position = vec4((start+line_width*nor1),0,1.);
                        EmitVertex();
                        gl_Position = vec4((start),0,1.);
                        EmitVertex();
                        EndPrimitive();


                    }
                    else{
                        gl_Position = vec4((start-line_width*nor1),0,1.);
                         EmitVertex();
                        gl_Position = vec4((start-line_width*nor0),0,1.);
                         EmitVertex();
                        gl_Position = vec4((start),0,1.);
                         EmitVertex();
                        EndPrimitive();

                    }

                  }

                  if(dot(v1,v2) < -MiterLimit){
                    miter_b = nor1;
                    length_b = line_width;
                  }

                  gl_Position = vec4((start+length_a*miter_a),0.,1.);
                   EmitVertex();
                  gl_Position = vec4((start-length_a*miter_a),0.,1.);
                   EmitVertex();
                  gl_Position = vec4((end+length_b*miter_b),0.,1.);
                   EmitVertex();
                  gl_Position = vec4((end-length_b*miter_b),0.,1.);
                   EmitVertex();
                   EndPrimitive();


                }