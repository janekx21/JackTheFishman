#version 150 core

uniform sampler2D texture;
uniform sampler2D normalMap;
uniform sampler2D tex2;
uniform mat4 mvp;
uniform mat4 projection;

in vec3 pixelNormal;
in vec3 pixelLight;
in vec2 pixelUV;

out vec4 color;

float pi = 3.1415926535;
float aoDist = .08;
float iter = 32;

float random (vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898, 78.233)))*
    43758.5453123);
}

void main() {
    float depth = sqrt(1-texture(tex2, pixelUV).r);
    float pick = 0;
    for(int i=0;i<iter;i++) {
        vec2 offset = vec2(random(pixelUV + vec2(0,i)), random(pixelUV + vec2(1,i)))*2-1;
        pick += sqrt(1-texture(tex2, pixelUV + offset * aoDist).r) / iter;
    }
    float diff = pick - depth;

    vec3 ao = vec3(diff);

    vec4 c = texture(texture, pixelUV);
    c.rgb = mix(c.rgb, ao, .9);
    color = c;
}