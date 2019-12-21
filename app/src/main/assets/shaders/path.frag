#version 310 es
precision mediump float;
out vec4 fragColor;
in vec3 col;
uniform vec2 res;
uniform float time;
void main(){

        fragColor = vec4(col+0.3,0.8);

 }