out vec4 outColor;
in vec3 outPosition;
in vec2 uv;

uniform vec4 funkyColor;
uniform sampler2D funkyTex;

void main() {
    vec3 c = texture(funkyTex, uv).xyz;
    outColor = vec4(funkyColor.xyz * c, 1);
}