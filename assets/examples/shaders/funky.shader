#import matrix.glsl
#vertex
// default inputs
#import basic.glsl
out vec3 outPosition;
out vec2 uv;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    outPosition = gl_Position.xyz;
    uv = UV;
}

#fragment
in vec3 outPosition;
in vec2 uv;
out vec4 outColor;

uniform vec4 funkyColor;
uniform sampler2D funkyTex;

void main() {
    vec3 c = texture(funkyTex, uv).xyz;
    c = mix(c, vec3(uv, 1), .5);
    outColor = vec4(funkyColor.xyz * c, 1);
}