import engine.Loader
import engine.graphics.Mesh
import engine.graphics.Shader

object Player : GameObject("player") {
    private val diffuseshader = Loader.createViaPath(Shader, "shaders/diffuse.shader")

    init {
        addComponent<Transform>()
        addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
            it.shader = diffuseshader
        }
        addComponent<ColliderComponent>()
        addComponent<PlayerController>()
    }
}
