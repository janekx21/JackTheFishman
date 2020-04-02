#version 150 core

uniform mat4 mvp;
uniform mat4 projection;
uniform vec3 light;

in vec2 position;
in float z;
in vec3 normal;
in vec2 uv;
out vec2 pixelUV;

void main() {
    gl_Position = vec4(position,z,1);
    pixelUV = uv;
}