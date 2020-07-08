#vertex
uniform mat4 MVP;
uniform mat4 World;
uniform float curveWorldX;
uniform float curveWorldY;

in vec3 Position;
in vec3 Normal;
in vec2 UV;
out vec3 normal;
out vec3 position;
out vec2 uv;

vec4 bend(vec4 position) {
    position.x = position.x + (position.z * position.z * curveWorldX);
    position.y = position.y + (position.z * position.z * curveWorldY);
    return position;
}

void main() {
    gl_Position = (MVP * vec4(Position, 1));
    position = ((World * vec4(Position, 1))).xyz;
    //gl_Position = bend(MVP * vec4(Position, 1));
    //position = bend((World * vec4(Position, 1))).xyz;
    normal = Normal;
    uv = UV;
}

    #fragment
    #define MAX_LIGHT_COUNT 32
uniform vec3 LightPositions[MAX_LIGHT_COUNT];
uniform vec3 LightColors[MAX_LIGHT_COUNT];
uniform sampler2D AlbedoTexture;
uniform vec3 CameraPosition;
uniform float SpecularIntensity;
uniform float SpecularRoughness;
uniform float FresnelIntensity;
uniform vec3 FogColor;
uniform float FogDistance;
uniform vec3 AmbientColor;

in vec3 normal;
in vec3 position;
in vec2 uv;
out vec4 outColor;

void main() {
    vec3 light = vec3(0.0);
    vec3 viewDirection = normalize(position - CameraPosition);
    for (int i = 0; i < MAX_LIGHT_COUNT; i++) {
        vec3 lightDirection = position - LightPositions[i];
        float diffuse = dot(normal, -lightDirection);
        float diffuseLight = diffuse / pow(length(lightDirection), 2);

        vec3 reflected = normalize(reflect(-lightDirection, normal));
        float specularLight = pow(max(dot(reflected, viewDirection), 0), SpecularRoughness);


        vec3 albedo = texture(AlbedoTexture, uv).rgb;
        light += (diffuseLight * albedo + specularLight * SpecularIntensity) * LightColors[i];
    }
    float fresnel = pow(1 - max(dot(-viewDirection, normal), 0), 4);

    light += fresnel * FresnelIntensity + AmbientColor;

    float distance = distance(position, CameraPosition) / FogDistance;
    distance = log(distance);
    light = mix(light, FogColor, max(min(distance, 1), 0));

    outColor = vec4(light, 1);
}
