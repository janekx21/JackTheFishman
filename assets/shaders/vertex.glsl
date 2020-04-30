#version 150 core

uniform mat4 mvp;
uniform mat4 projection;
uniform vec3 light;

out vec3 pixelNormal;
out vec3 pixelLight;
out vec2 pixelUV;

void main() {
    gl_Position = projection * mvp * vec4(position,z,1);
    pixelNormal = normal;
    pixelLight = (mvp * vec4(light, 0)).xyz;
    pixelUV = uv;
}