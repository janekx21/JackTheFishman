#version 150 core

uniform sampler2D tex;
in vec2 coord;
out vec4 color;

void main() {
    vec4 c = texture(tex, coord);
    if(c.a < .5) discard;
    color = c;
}