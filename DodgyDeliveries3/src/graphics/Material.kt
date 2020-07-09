package graphics

import engine.graphics.Shader
import engine.graphics.Texture
import engine.graphics.Texture2D
import engine.math.FloatExt
import engine.math.Vector3fExt
import engine.math.toJson
import engine.util.IJsonSerializable
import engine.util.IJsonUnserializable
import org.joml.Vector3f
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
) : IJsonSerializable {
    override fun toJson(): Any? {
        return mapOf(
            "shader" to shader.toJson(),
            "specularIntensity" to specularIntensity,
            "specularRoughness" to specularRoughness,
            "fresnelIntensity" to fresnelIntensity,
            "ambientColor" to ambientColor.toJson(),
            "albedoTexture" to albedoTexture,
            "normalTexture" to normalTexture,
            "normalIntensity" to normalIntensity
        )
    }

    companion object : IJsonUnserializable<Material> {
        override fun fromJson(json: Any?): Material {
            // TODO(implement Material serialization)
            throw NotImplementedError()

            /*
            return Material(
                Shader.fromJson(map["shader"]),
                FloatExt.fromJson(map["specularIntensity"]),
                FloatExt.fromJson(map["specularRoughness"]),
                FloatExt.fromJson(map["fresnelIntensity"]),
                Vector3fExt.fromJson(map["ambientColor"]),
                Texture2D.createViaPath(""),
                Texture2D.createViaPath(""),
                FloatExt.fromJson(map["normalIntensity"])
            )
            */
        }
    }
}
