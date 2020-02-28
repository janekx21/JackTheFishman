#version 150 core

in vec2 position;
out vec2 coord;

void main() {
    coord = position;
    gl_Position = vec4(position,0,1);
}