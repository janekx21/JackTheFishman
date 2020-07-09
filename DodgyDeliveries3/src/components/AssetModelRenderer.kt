package components

import GameObject
import engine.Loader
import engine.graphics.Mesh
import engine.graphics.Shader
import graphics.Material
import org.joml.Vector3f

/**
 * ModelRenderer, dessen Mesh ein Asset ist und mit [Loader.createViaPath] geladen werden kann.
 */
class AssetModelRenderer(gameObject: GameObject) : ModelRenderer(gameObject) {
    var meshPath: String? = null
        set(value) {
            field = value
            mesh = if (value != null) {
                Loader.createViaPath(Mesh, value)
            } else {
                null
            }
        }

    var shaderPath: String? = null
        set(value) {
            field = value
            material = material.copy(
                shader = if (value != null) {
                    Shader.createViaPath(value)
                } else {
                    defaultShader
                }
            )
        }

    var albedoPath: String? = null
        set(value) {
            field = value
            material = material.copy(
                albedoTexture = if (value != null) {
                    Loader.createViaPath(value)
                } else {
                    defaultAlbedo
                }
            )
        }

    var normalPath: String? = null
        set(value) {
            field = value
            material = material.copy(
                normalTexture = if (value != null) {
                    Loader.createViaPath(value)
                } else {
                    defaultNormal
                }
            )
        }

    override var mesh: Mesh? = null
        private set

    override var material: Material = Material(defaultShader, .5f, 40f, .6f, Vector3f(.1f, .1f, .1f), defaultAlbedo, defaultNormal, .1f)
        private set

    override fun toJson(): Any? {
        return mapOf(
            "meshPath" to meshPath,
            "shaderPath" to shaderPath,
            "albedoPath" to albedoPath,
            "normalPath" to normalPath
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>

        meshPath = map["meshPath"] as String
        shaderPath = map["shaderPath"] as String
        albedoPath = map["albedoPath"] as String
        normalPath = map["normalPath"] as String
    }
}