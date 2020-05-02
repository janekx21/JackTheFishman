in vec3 Position;

out vec3 outPosition;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    outPosition = gl_Position.xyz;
}