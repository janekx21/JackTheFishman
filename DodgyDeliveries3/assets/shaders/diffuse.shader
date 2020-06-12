#vertex
uniform mat4 MVP;

in vec3 Position;
in vec3 Normal;
out vec3 normal;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    normal = Normal;
}

    #fragment
uniform vec3 LightDirection;

in vec3 normal;
out vec4 outColor;

void main() {
    float diffuse = dot(normal, -LightDirection);
    outColor = vec4(diffuse, diffuse, diffuse, 1);
}
