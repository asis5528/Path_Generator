#version 310 es
#extension GL_EXT_geometry_shader : enable
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
                vec3 prev = gl_in[0].gl_Position.xyz;
                    vec3 start = gl_in[1].gl_Position.xyz;
                    vec3 end = gl_in[2].gl_Position.xyz;
                    vec3 next = gl_in[3].gl_Position.xyz;
                    vec3 lhs = cross(normalize(end-start), vec3(0.0, 0.0, 1.0));
                    bool colStart = length(start-prev) < 0.0001;
                    bool colEnd = length(end-next) < 0.0001;
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
                    endLhs *=  vec3(line_width,line_width*asp,0.0);
                vec2 st = start.xy;
                vec2 stlhs = startLhs.xy;
                vec2 en = end.xy;
                vec2 enlhs = endLhs.xy;
                vec2 x  = st+stlhs/startInvScale;
                vec2 x1 = st-stlhs/startInvScale;
                vec2 y =en+enlhs/endInvScale;
                vec2 y1 = en-enlhs/endInvScale;
                vec2 m = (x1-x)/15.;
                vec2 m1 = (y1-y)/15.;
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
                //if(val<0.0){
              //  col = vec3(1.,0.,0.);
             //   }
             //   else{
             //   col = vec3
             //   }
                gl_Position = vec4(x+m*float(ind),0., 1.0)*val;
                col =gl_Position.xyz;
                //col = vec3(1.,0.,0.);


                EmitVertex();
                gl_Position = vec4(x+m*(float(ind)+1.), 0.,1.0)*val;
                col = gl_Position.xyz;
                 //col = vec3(1.,0.,0.);
                EmitVertex();
                gl_Position =vec4(y+m1*float(ind),0., 1.0)*val;
            col = gl_Position.xyz;
 //col = vec3(0.,1.,0.);
           EmitVertex();
         gl_Position = vec4(y+m1*(float(ind)+1.), 0.,1.0)*val;
         col = gl_Position.xyz;
          //col = vec3(0.,1.,0.);
               EmitVertex();
              EndPrimitive();
              ind+=1;
}




                }