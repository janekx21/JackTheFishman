import components.BoxCollider
import components.ModelRenderer
import components.Transform
import engine.Loader
import engine.graphics.Mesh

object Player : GameObject("player") {

    init {
        addComponent<Transform>()
        addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
        }
        addComponent<BoxCollider>().also {
            it.linearDamping = 3f
        }
        addComponent<PlayerController>()
        addComponent<HealthComponent>().also {
            it.maxHp = 100f
            it.hp = it.maxHp
        }
        Scene.active.spawn(Player)
    }
}
