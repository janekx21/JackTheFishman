package components

import GameObject
import engine.graphics.Mesh
import engine.graphics.Shader

class ModelRenderer(gameObject: GameObject) : Renderer(gameObject) {
    var mesh: Mesh? = null
    var shader: Shader? = null

    override fun update() {}

    override fun draw() {
        check(shader != null) { "no shader assigned" }
        if (mesh != null) {
            shader!!.setMatrix(gameObject.transform.generateMatrix(), Camera.main!!.generateViewMatrix(), Camera.main!!.getProjectionMatrix())
            shader!!.use {
                mesh?.draw()
            }
        }
    }
}