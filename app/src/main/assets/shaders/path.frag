#version 310 es
precision mediump float;
out vec4 fragColor;
in vec3 col;
uniform vec2 res;
uniform float time;
void main(){
        fragColor = vec4(col.r,col.g,sin(time*0.1)+col.b,0.8)+vec4(0.3,0.3,0.3,1.);
        fragColor = vec4(col+0.3,0.8);
        //fragColor = vec4(1.,0.,0.,0.6);
 }