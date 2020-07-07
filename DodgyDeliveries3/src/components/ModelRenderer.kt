package components

import GameObject
import engine.Loader
import engine.graphics.Mesh
import engine.graphics.Shader
import org.joml.Vector3f

class ModelRenderer(gameObject: GameObject) : Renderer(gameObject) {
    companion object {
        val defaultShader = Loader.createViaPath<Shader>("shaders/default.shader")
    }

    var mesh: Mesh? = null
    var shader: Shader = defaultShader

    override fun update() {}

    override fun draw() {
        if (mesh != null) {
            shader.setMatrix(
                gameObject.transform.generateMatrix(),
                Camera.main!!.generateViewMatrix(),
                Camera.main!!.getProjectionMatrix()
            )
            for ((index, light) in PointLight.all.withIndex()) {
                shader.setUniform("LightPositions[$index]", light.transform.position)
                shader.setUniform("LightColors[$index]", Vector3f(light.color))
            }
            shader.use {
                mesh?.draw()
            }
        }
    }

    override fun toJson(): Any? {
        return mapOf(
            "mesh" to mesh?.toJson(),
            "shader" to shader?.toJson()
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>
        val mesh: Mesh?
        val shader: Shader?

        mesh = if (map["mesh"] != null) {
            Mesh.fromJson(map["mesh"] as String)
        } else {
            null
        }

        shader = if (map["shader"] != null) {
            Shader.fromJson(map["shader"] as String)
        } else {
            null
        }

        this.mesh = mesh
        this.shader = shader!!
    }
}