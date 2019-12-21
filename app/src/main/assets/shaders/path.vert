#version 310 es
precision mediump float;
in vec2 pos;
in vec4 a;
in vec4 b;
in vec4 c;
in vec4 d;
in vec4 e;
in vec4 f;
in vec4 g;
in vec4 h;
in vec4 i;
in vec4 j;
in vec4 k;
in vec4 l;
in vec4 m;
in vec4 n;
in vec4 o;
in vec4 p;


out mat4 cut;
out mat4 cut1;
out mat4 cut2;
out mat4 cut3;

void main(){


 cut =mat4(a, b,c,d);
 cut1 = mat4(e,f,g,h);
 cut2 = mat4(i,j,k,l);
 cut3 = mat4(m,n,o,p);

 gl_Position = vec4(pos,0.0,1.0);
}
