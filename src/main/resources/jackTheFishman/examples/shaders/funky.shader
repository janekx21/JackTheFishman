#import matrix.glsl

#define PI 3.1415926535897932384626433832795

#vertex
// default inputs
#import basic.glsl

in vec3 Normal;
out vec3 outPosition;
out vec2 uv;
out vec3 normal;
out vec3 position;

void main() {
    gl_Position = MVP * vec4(Position, 1);
    outPosition = gl_Position.xyz;
    uv = UV;
    position = vec3(World * vec4(Position, 1));
    normal = vec3(World * vec4(Normal, 0));
}

#fragment

in vec3 outPosition;
in vec2 uv;
in vec3 normal;
in vec3 position;
out vec4 outColor;

uniform vec4 funkyColor;
uniform sampler2D funkyTex;
uniform samplerCube Cube;
uniform vec3 CameraPosition;
uniform vec3 LightDirection;

void main() {
    vec3 norm = normalize(normal);
    vec3 viewDirection = normalize(position - CameraPosition);
    vec3 texCoord = reflect(norm, viewDirection);

    vec3 halfWay = normalize(-viewDirection -LightDirection);
    float spec = max(dot(halfWay, norm), 0);

    float a = .5;

    float s = max(dot(halfWay, norm), 0);
    float tmp = s*s * (a-1) + 1;
    spec = (a*a) / (PI * tmp * tmp);

    float k = (a * a) / 2;
    float gmp = max(dot(norm, -LightDirection), 0);
    float diffuse = gmp / (gmp * (1-k) + k);

    vec3 c = texture(funkyTex, uv).xyz;
    c*= diffuse + spec + .15;
    c = mix(c, texture(Cube, texCoord).rgb, .1);
    // c = vec3(spec);
    outColor = vec4(c, 1);
}
