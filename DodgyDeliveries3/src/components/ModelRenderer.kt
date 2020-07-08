package components

import GameObject
import engine.Input
import engine.Loader
import engine.graphics.Mesh
import engine.graphics.Shader
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

class ModelRenderer(gameObject: GameObject) : Renderer(gameObject) {
    companion object {
        val defaultShader = Loader.createViaPath<Shader>("shaders/default.shader")
    }

    var mesh: Mesh? = null
    var shader: Shader = defaultShader

    var curveX = 0f
    var curveY = 0f

    override fun update() {
        if (Input.Keyboard.justDown(GLFW_KEY_5)) {
            curveX += 0.01f
        }
        if (Input.Keyboard.justDown(GLFW_KEY_6)) {
            curveX -= 0.01f
        }
        if (Input.Keyboard.justDown(GLFW_KEY_7)) {
            curveY += 0.01f
        }
        if (Input.Keyboard.justDown(GLFW_KEY_8)) {
            curveY -= 0.01f
        }
    }

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
            shader.setUniform("CameraPosition", Camera.main!!.transform.position)
            shader.setUniform("curveWorldX", curveX)
            shader.setUniform("curveWorldY", curveY)
            shader.setUniform("SpecularIntensity", .5f)
            shader.setUniform("SpecularRoughness", 40f)
            shader.setUniform("FresnelIntensity", .6f)
            shader.setUniform("FogDistance", 10f)
            shader.setUniform("FogColor", Vector3f(.1f, .1f, .15f))
            shader.setUniform("AmbientColor", Vector3f(.1f, .1f, .1f))
            shader.use {
                mesh?.draw()
            }
        }
    }

    override fun toJson(): Any? {
        return mapOf(
            "mesh" to mesh?.toJson(),
            "shader" to shader.toJson()
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