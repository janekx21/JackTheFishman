#vertex
uniform mat4 World;
uniform mat4 View;
uniform mat4 Projection;
uniform mat4 MVP;

uniform float Time;

in vec3 Position;
in vec2 UV;
out vec2 uv;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    uv = UV;
}

#fragment
uniform sampler2D Texture;

in vec2 uv;
out vec4 outColor;

void main() {
    vec4 color = texture(Texture, uv);
    if (color.a < .5) discard;
    outColor = vec4(color.rgb, 1);
}
