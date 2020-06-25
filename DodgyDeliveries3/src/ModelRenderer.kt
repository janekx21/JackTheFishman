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
        this.shader = shader
    }
}