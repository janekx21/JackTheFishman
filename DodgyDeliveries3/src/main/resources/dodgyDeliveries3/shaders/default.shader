#vertex
uniform mat4 MVP;
uniform mat4 World;
uniform float curveWorld;

in vec3 Position;
in vec3 Normal;
in vec3 Tangent;
in vec2 UV;
out vec3 position;
out vec2 uv;
out mat3 TBN;

vec4 bend(vec4 pos) {
    pos.x = pos.x + (pos.z * pos.z * curveWorld);
    return pos;
}

void main() {
    gl_Position = bend(MVP * vec4(Position, 1));
    position = (World * vec4(Position, 1)).xyz;

    vec3 N = normalize(vec3(World * vec4(Normal, 0.0)));
    vec3 T = normalize(vec3(World * vec4(Tangent, 0.0)));
    vec3 B = cross(N, T);
    TBN = mat3(T, B, N);
    uv = UV;
}

#fragment
#define MAX_LIGHT_COUNT 32
uniform vec3 LightPositions[MAX_LIGHT_COUNT];
uniform vec3 LightColors[MAX_LIGHT_COUNT];
uniform vec3 AlbedoColor;
uniform sampler2D AlbedoTexture;
uniform vec3 CameraPosition;
uniform float SpecularIntensity;
uniform float SpecularRoughness;
uniform sampler2D SpecularTexture;
uniform float FresnelIntensity;
uniform vec3 FogColor;
uniform float FogDistance;
uniform vec3 AmbientColor;
uniform sampler2D NormalTexture;
uniform float NormalIntensity;
uniform vec3 EmissionColor;

in mat3 TBN;
in vec3 position;
in vec2 uv;
out vec4 outColor;

vec3 generateFresnel(vec3 viewDirection, vec3 normal) {
    float intensity = pow(1 - dot(-viewDirection, normal), 4);
    return vec3(intensity);
}

vec3 generateAmbient() {
    return AmbientColor;
}

float generateFogIntensity(vec3 fragmentPosition, vec3 cameraPosition) {
    float distance = distance(position, CameraPosition);
    distance = clamp(distance / 100, 0, 1);
    float density = 5;
    float gradient = 1.5;
    return 1 - exp(-pow((distance * density), gradient));
}

void main() {
    vec3 normal = texture(NormalTexture, uv).xyz * 2 - 1;
    normal.xy *= NormalIntensity;
    normal = normalize(normal);
    normal = normalize(TBN * normal);

    vec3 light = vec3(0.0);
    vec3 viewDirection = normalize(position - CameraPosition);
    vec3 albedo = texture(AlbedoTexture, uv).rgb * AlbedoColor;
    vec3 specular = texture(SpecularTexture, uv).rgb;
    for (int i = 0; i < MAX_LIGHT_COUNT; i++) {
        if (LightColors[i] != vec3(0, 0, 0)) {
            vec3 deltaLight = position - LightPositions[i];
            vec3 lightDirection = normalize(deltaLight);
            float diffuse = dot(normal, -lightDirection);
            float diffuseLight = max(diffuse / pow(length(deltaLight), 2), 0);

            vec3 reflected = reflect(-lightDirection, normal);
            float specularLight = pow(max(dot(reflected, viewDirection), 0), SpecularRoughness);

            light += (diffuseLight * albedo + specularLight * SpecularIntensity * specular) * LightColors[i];
        }
    }
    float fresnel = pow(1 - dot(-viewDirection, normal), 4);

    light += generateFresnel(viewDirection, normal) * FresnelIntensity * specular;
    light += generateAmbient() * albedo;
    light += EmissionColor;

    light = mix(light, FogColor, generateFogIntensity(position, CameraPosition));

    outColor = vec4(light, 1);
}
