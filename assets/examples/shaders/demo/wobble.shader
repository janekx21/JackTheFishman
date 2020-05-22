#vertex
uniform mat4 World;
uniform mat4 View;
uniform mat4 Projection;
uniform mat4 MVP;

uniform float Time;

in vec3 Position;
out vec3 worldPosition;

void main() {
    worldPosition = (World * vec4(Position, 1)).xyz;
    // gl_Position = MVP * vec4(Position + vec3(1, 0, 0) * sin(Time + worldPosition.y), 1);
    gl_Position = MVP * vec4(Position, 1);
}

#fragment
in vec3 worldPosition;
out vec4 outColor;

void main() {
    // float red = sin(worldPosition.x) * .5 + .5;
    // float green = sin(worldPosition.z) * .5 + .5;
    // float blue = sin(worldPosition.y) * .5 + .5;
    // vec3 color = vec3(red, green, blue);

    vec3 color = sin(worldPosition) * .5 + .5;
    outColor = vec4(color, 1);
}
