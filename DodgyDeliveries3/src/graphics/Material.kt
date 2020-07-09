package graphics

import engine.graphics.Shader
import engine.graphics.Texture
import org.joml.Vector3fc

data class Material (
    val shader: Shader,
    val specularIntensity: Float,
    val specularRoughness: Float,
    val fresnelIntensity: Float,
    val ambientColor: Vector3fc,
    val albedoTexture: Texture?,
    val normalTexture: Texture?,
    val normalIntensity: Float
)
