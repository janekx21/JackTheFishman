#vertex
uniform mat4 World;
uniform mat4 View;
uniform mat4 Projection;
uniform mat4 MVP;

in vec3 Position;
out vec3 worldPosition;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    worldPosition = (World * vec4(Position, 1)).xyz;
}

#fragment
in vec3 worldPosition;
out vec4 outColor;

void main() {
    // http://www.shaderific.com/glsl-functions

    // float value = fract(worldPosition.y);
    // float value = sin(worldPosition.y);
    float value = sin(worldPosition.y * 10) * .5 + .5;
    // if (value < .5) discard;
    outColor = vec4(value, value, value, 1);
}
