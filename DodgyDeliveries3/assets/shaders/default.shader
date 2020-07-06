#vertex
uniform mat4 MVP;
uniform mat4 World;

in vec3 Position;
in vec3 Normal;
out vec3 normal;
out vec3 position;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    position = vec3(World * vec4(Position, 1));
    normal = Normal;
}

#fragment
#define MAX_LIGHT_COUNT 32
uniform vec3 LightPositions[MAX_LIGHT_COUNT];
uniform vec3 LightColors[MAX_LIGHT_COUNT];

in vec3 normal;
in vec3 position;
out vec4 outColor;

void main() {
    vec3 light = vec3(0.0);
    for (int i = 0; i < MAX_LIGHT_COUNT; i++) {
        vec3 lightDirection = position - LightPositions[i];
        float diffuse = dot(normal, -lightDirection);
        light += (diffuse / pow(length(lightDirection), 2)) * LightColors[i];
    }
    outColor = vec4(vec3(light), 1);
}
