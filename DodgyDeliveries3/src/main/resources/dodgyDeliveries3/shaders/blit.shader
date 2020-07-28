#vertex
in vec3 Position;
in vec2 UV;
out vec2 uv;

void main() {
    gl_Position = vec4(Position, 1);
    uv = UV;
}

#fragment
uniform sampler2D Source;

in vec2 uv;
out vec4 outColor;

void main() {
    vec3 color = texture(Source, uv).rgb;
    outColor = vec4(color, 1);
}
