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
    mat4 projMatrixInv = inverse(projection);
    mat4 viewMatrixInv = inverse(mvp);
    float depth = texture(tex2, pixelUV).r;
    vec4 clipSpacePosition = vec4(pixelUV * 2 - 1, depth * 2 - 1, 1);
    vec4 viewSpacePosition = projMatrixInv * clipSpacePosition;
    viewSpacePosition /= viewSpacePosition.w;
    vec4 worldSpacePosition = viewMatrixInv * viewSpacePosition;

    vec3 wPos = worldSpacePosition.xyz;
    vec3 off = vec3(random(wPos.xy + vec2(1,3)),random(wPos.xz + vec2(2, -2)),random(wPos.yz + vec2(3,-1)));
    wPos += off * .1;


    float sum = 0;

    for (float count = 0; count < iter; count++) {
        vec2 offset = (vec2(random(pixelUV + vec2(0, count)), random(pixelUV + vec2(10, count)))-.5) * 2;
        offset = normalize(offset) * random(pixelUV + vec2(5, count));

        float pick = texture(tex2, pixelUV + offset * aoDist).r;// in unit circle
        float distance = length(offset);// 0 - 1
        float zOffset = (random(pixelUV + vec2(3, count)) - .5) * 2 * aoDist;

        if (pick + zOffset < depth) {
            float difference = abs(depth - pick);
            sum = 1;
        }
    }
    float outside = sum / iter;

    float v = outside;//abs(depth - outside);
    // v = flag2 * v;

    vec4 c = texture(texture, pixelUV);
    c.rgb = mix(c.rgb, wPos, .9);
    color = c;
}