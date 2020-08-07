package dodgyDeliveries3.components

import dodgyDeliveries3.graphics.Material
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.Shader
import jackTheFishman.engine.graphics.Texture2D
import org.joml.Vector3f

/**
 * Component that renders a mesh with a specific material
 */
data class ModelRenderer(var mesh: Mesh? = null, var material: Material = defaultMaterial) : Renderer() {
    override fun update() {}

    override fun draw() {
        if (mesh != null) {
            val shader = material.shader
            shader.setMatrix(
                gameObject.transform.generateMatrix(),
                Camera.main!!.generateViewMatrix(),
                Camera.main!!.getProjectionMatrix()
            )
            uploadUniforms(shader)
            shader.use {
                mesh?.draw()
            }
        }
    }

    private fun uploadUniforms(shader: Shader) {
        uploadLightUniforms(shader)
        uploadMaterialUniforms(shader)
        uploadFogUniforms(shader)
    }

    private fun uploadLightUniforms(shader: Shader) {
        for ((index, light) in PointLight.all.withIndex()) {
            shader.setUniform("LightPositions[$index]", light.transform.position)
            shader.setUniform("LightColors[$index]", Vector3f(light.color))
        }
        for (index in PointLight.all.size until PointLight.max) {
            shader.setUniform("LightColors[$index]", Vector3f(0f, 0f, 0f))
        }
    }

    private fun uploadMaterialUniforms(shader: Shader) {
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
    }

    private fun uploadFogUniforms(shader: Shader) {
        shader.setUniform("FogDistance", 10f)
        shader.setUniform("FogColor", Vector3f(.1f, .1f, .15f))
    }

    companion object {
        private val defaultShader = Loader.createViaPath<Shader>("shaders/default.shader")
        private val defaultNormal = Loader.createViaPath<Texture2D>("textures/normal.png")
        private val defaultAlbedo = Loader.createViaPath<Texture2D>("textures/default.png")
        private val defaultMaterial = Material(defaultShader, .5f, 40f, .6f, Vector3f(.1f, .1f, .1f), null, null, .1f)
    }
}
