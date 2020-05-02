out vec4 outColor;
in vec3 outPosition;

void main() {
    outColor = vec4(outPosition.xyz, 1);
}