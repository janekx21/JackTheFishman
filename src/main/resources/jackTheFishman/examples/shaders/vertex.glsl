#import basic.glsl

out vec3 outPosition;
out vec2 uv;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    outPosition = gl_Position.xyz;
    uv = UV;
}