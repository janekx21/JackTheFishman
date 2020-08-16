package dodgyDeliveries3.graphics

import jackTheFishman.engine.graphics.Shader
import jackTheFishman.engine.graphics.Texture
import org.joml.Vector3fc

data class Material(
    val shader: Shader,
    val specularIntensity: Float,
    val specularRoughness: Float,
    val fresnelIntensity: Float,
    val ambientColor: Vector3fc,
    val albedoTexture: Texture?,
    val normalTexture: Texture?,
    val specularTexture: Texture?,
    val normalIntensity: Float
)
