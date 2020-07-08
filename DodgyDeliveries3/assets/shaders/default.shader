#vertex
uniform mat4 MVP;
uniform mat4 World;

in vec3 Position;
in vec3 Normal;
in vec3 Tangent;
in vec2 UV;
out vec3 position;
out vec2 uv;
out mat3 TBN;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    position = vec3(World * vec4(Position, 1));

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
uniform sampler2D AlbedoTexture;
uniform vec3 CameraPosition;
uniform float SpecularIntensity;
uniform float SpecularRoughness;
uniform float FresnelIntensity;
uniform vec3 FogColor;
uniform float FogDistance;
uniform vec3 AmbientColor;
uniform sampler2D NormalTexture;
uniform float NormalIntensity;

in mat3 TBN;
in vec3 position;
in vec2 uv;
out vec4 outColor;

void main() {
    vec3 normal = texture(NormalTexture, uv).xyz * 2 - 1;
    normal.xy *= NormalIntensity;
    normal = normalize(TBN * normal);

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

    float distance = distance(position, CameraPosition);
    distance = clamp(distance / 100, 0, 1);
    float density = 5;
    float gradient = 1.5;
    float fogIntensity = 1 - exp(-pow((distance * density), gradient));

    light = mix(light, FogColor, fogIntensity);

    outColor = vec4(light, 1);
}
