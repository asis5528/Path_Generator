#version 310 es
precision mediump float;
in vec2 pos;
in float gr;
in vec4 a;
in vec4 b;
in vec4 c;
in vec4 d;


out float g;
out mat4 cut;
void main(){

 vec4 nth = a;
 cut =mat4(a, b,c,d);

    g=gr;

    vec2 p = pos;

    gl_Position = vec4(p,0.0,1.0);
}
