package components

import GameObject
import engine.Loader
import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture2D
import graphics.Material
import org.joml.Vector3f

abstract class ModelRenderer(gameObject: GameObject) : Renderer(gameObject) {
    companion object {
        val defaultShader = Loader.createViaPath<Shader>("shaders/default.shader")
        val defaultNormal = Loader.createViaPath<Texture2D>("textures/normal.png")
        val defaultAlbedo = Loader.createViaPath<Texture2D>("textures/default.png")
    }

    abstract val mesh: Mesh?
    abstract val material: Material

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
}