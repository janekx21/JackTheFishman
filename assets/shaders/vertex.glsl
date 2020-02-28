#version 150 core

uniform vec2 pos;
in vec2 position;
out vec2 coord;

void main() {
    coord = position;
    gl_Position = vec4(position + pos,0,1);
}