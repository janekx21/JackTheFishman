package components

import GameObject
import graphics.Material
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.Shader
import jackTheFishman.engine.graphics.Texture2D
import org.joml.Vector3f

class ModelRenderer(gameObject: GameObject) : Renderer(gameObject) {
    companion object {
        val defaultShader = Loader.createViaPath<Shader>("shaders/default.shader")
        val defaultNormal = Loader.createViaPath<Texture2D>("textures/normal.png")
        val defaultAlbedo = Loader.createViaPath<Texture2D>("textures/default.png")
    }

    var mesh: Mesh? = null
    var material = Material(defaultShader, .5f, 40f, .6f, Vector3f(.1f, .1f, .1f), null, null, .1f)

    override fun update() {}

    override fun draw() {
        if (mesh != null) {
            val shader = material.shader
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
            shader.setUniform("SpecularIntensity", material.specularIntensity)
            shader.setUniform("SpecularRoughness", material.specularRoughness)
            shader.setUniform("FresnelIntensity", material.fresnelIntensity)
            shader.setUniform("AmbientColor", material.ambientColor)

            if (material.albedoTexture != null) {
                shader.setUniform("AlbedoTexture", material.albedoTexture!!)
            } else {
                shader.setUniform("AlbedoTexture", defaultAlbedo) // default aka white
            }
            if (material.normalTexture != null) {
                shader.setUniform("NormalTexture", material.normalTexture!!)
            } else {
                shader.setUniform("NormalTexture", defaultNormal)
            }
            shader.setUniform("NormalIntensity", material.normalIntensity)

            shader.setUniform("FogDistance", 10f)
            shader.setUniform("FogColor", Vector3f(.1f, .1f, .15f))
            shader.use {
                mesh?.draw()
            }
        }
    }

    override fun toJson(): Any? {
        return mapOf(
            "mesh" to mesh?.toJson(),
            "material" to material
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>
        val mesh: Mesh?

        mesh = if (map["mesh"] != null) {
            Mesh.fromJson(map["mesh"] as String)
        } else {
            null
        }

        this.mesh = mesh
        this.material = map["material"] as Material
    }
}