package components

import GameObject
import engine.Loader
import engine.graphics.Mesh

class TunnelModelRenderer(gameObject: GameObject) : ModelRenderer(gameObject) {
    override var mesh: Mesh? = Loader.createViaPath(Mesh, "models/tunnel.fbx") // TODO: add player mesh

    override fun toJson(): Any? {
        return mapOf<String, Any?>()
    }

    override fun fromJson(json: Any?) {
        check(json is Map<*, *>)
    }
}