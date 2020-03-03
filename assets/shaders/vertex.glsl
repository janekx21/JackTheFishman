#version 150 core

uniform vec2 pos;
uniform vec2 scale;
in vec2 position;
in vec2 uv;
out vec2 coord;

void main() {
    coord = uv;
    gl_Position = vec4(position * scale + pos,0,1);
}