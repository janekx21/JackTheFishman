#vertex
uniform mat4 MVP;
in vec3 Position;

void main() {
    gl_Position = MVP * vec4(Position, 1);
}

#fragment
out vec4 outColor;

void main() {
    outColor = vec4(1, 1, 1, 1);
}
