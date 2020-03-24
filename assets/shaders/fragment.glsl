#version 150 core

uniform sampler2D texture;
uniform sampler2D normalMap;
uniform mat4 mvp;
uniform mat4 projection;

in vec3 pixelNormal;
in vec3 pixelLight;
in vec2 pixelUV;

out vec4 color;

void main() {
    vec3 lightColor = vec3(1, 1, 1);
    vec3 albeto = vec3(.4, .7, .5);
    vec3 speculatColor = vec3(1,1,1);
    float varIntensity = .1;
    float specIntensity = .6;
    vec3 lightDirection = pixelLight;// (mvp * vec4(pixelLight, 1)).xyz;


    vec3 sampledNormal = normalize(texture(normalMap, pixelUV).xyz * 2 - 1);
    vec3 normal = pixelNormal + sampledNormal * .4;
    normal = normalize(normal);

    vec4 transformed = mvp * vec4(normal, 0);
    vec3 screenNormal = transformed.xyz;

    albeto = texture(texture, pixelUV).rgb;
    specIntensity = texture(texture, pixelUV).a;

    vec3 ambient = lightColor * .2f;

    float v = length(screenNormal.xy);
    v = pow(v, 4);
    v = clamp(v, 0, 1);
    vec3 varColor = v * varIntensity * lightColor;

    float r = dot(normalize(lightDirection), normalize(screenNormal));
    r = clamp(r, 0, 1);

    vec3 diff = lightColor * r;

    vec3 refectDir = reflect(normalize(lightDirection), normalize(screenNormal));
    float spec = dot(refectDir, vec3(0, 0, -1));
    spec = pow(clamp(spec, 0, 1), 8);
    vec3 specular = spec * speculatColor * specIntensity;

    vec3 c = (ambient + diff + specular + varColor) * albeto;
    // c = screenNormal;
    color = vec4(c, 1);
}