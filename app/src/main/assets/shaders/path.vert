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
 float angle  = 1.5708;
 vec4 nth = a;
 cut =mat4(a, b,c,d);
 //cut = mat4(a,b,c,d);
g=gr;
angle=0.;
vec2 p = (pos/10.)*mat2(cos(angle),sin(angle),-sin(angle),cos(angle));
if(gl_InstanceID == 1){
p.y-=0.2;  }
gl_Position = vec4(p,0.0,1.0);
}
