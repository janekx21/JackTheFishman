#version 150 core

uniform mat4 mvp;
in vec2 position;
in float z;

void main() {
    gl_Position = mvp * vec4(position,z,1);
}