package dodgyDeliveries3.components

import dodgyDeliveries3.graphics.Material
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.Shader
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.moveTowards
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
        for (index in 0 until PointLight.max) {
            if (index < PointLight.all.size) {
                val light = PointLight.all[index]
                shader.setUniform("LightPositions[$index]", light.transform.position)
                shader.setUniform("LightColors[$index]", Vector3f(light.animatedColor))
            } else {
                shader.setUniform("LightColors[$index]", Vector3fConst.zero)
            }
        }

        // TODO refactor
        for (light in PointLight.all) {
            if (!light.alive) {
                light.animatedColor =
                    light.animatedColor.moveTowards(Vector3fConst.zero, Time.deltaTime * PointLight.colorSwitchTime)
            }
        }

        PointLight.all.removeIf { !it.alive && it.animatedColor.lengthSquared() <= 0 }
    }

    private fun uploadMaterialUniforms(shader: Shader) {
        shader.setUniform("CameraPosition", Camera.main!!.transform.position)
        shader.setUniform("SpecularIntensity", material.specularIntensity)
        shader.setUniform("SpecularRoughness", material.specularRoughness)
        shader.setUniform("FresnelIntensity", material.fresnelIntensity)
        shader.setUniform("AmbientColor", material.ambientColor)
        shader.setUniform("AlbedoColor", material.albedoColor)

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
        if (material.specularTexture != null) {
            shader.setUniform("SpecularTexture", material.specularTexture!!)
        } else {
            shader.setUniform("SpecularTexture", defaultAlbedo)
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
        private val defaultMaterial =
            Material(
                defaultShader,
                .5f,
                40f,
                .3f,
                Vector3f(.1f, .1f, .1f),
                Vector3fConst.one,
                null,
                null,
                null,
                1f
            )
    }
}
