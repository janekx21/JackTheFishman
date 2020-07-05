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
uniform vec3 Color;

in vec3 normal;
out vec4 outColor;

void main() {
    float light = max(dot(normal, -LightDirection), 0) + .2;
    vec3 color = vec3(light) * Color;
    outColor = vec4(color, 1);
}
