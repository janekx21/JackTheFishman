#version 150 core

in vec2 position;
uniform float time;

void main() {

    float t = time *0.5;
    mat2x2 rot = mat2x2(cos(t), -sin(t), sin(t), cos(t));

    vec2 p = rot * (position - .5);
    gl_Position = vec4(vec2(cos(time * .1)* .5 +.6, sin(time * .2)*.5 + .6)*p,0,1);
}