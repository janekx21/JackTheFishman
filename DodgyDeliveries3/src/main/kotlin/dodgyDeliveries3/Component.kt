package dodgyDeliveries3

import com.beust.klaxon.Json
import com.beust.klaxon.TypeFor
import dodgyDeliveries3.components.Transform
import dodgyDeliveries3.util.IHasOrigin
import dodgyDeliveries3.util.typeAdapter.ComponentTypeAdapter

@TypeFor(field = "className", adapter = ComponentTypeAdapter::class)
abstract class Component : IHasOrigin<GameObject> {
    val className: String = javaClass.name

    private var cachedGameObject: GameObject? = null

    @Json(ignored = true)
    var gameObject: GameObject
        get() {
            check(cachedGameObject != null) { "dodgyDeliveries3.Component not initialized yet. Try overriding the start() function" }
            return cachedGameObject!!
        }
        set(value) {
            check(cachedGameObject == null) { "gameObject was already set" }
            cachedGameObject = value
        }


    open fun start() {}
    abstract fun update()
    abstract fun draw()
    open fun onEnable() {}
    open fun onDisable() {}

    @Json(ignored = true)
    val transform: Transform
        get() = gameObject.getComponent()

    override fun setOrigin(origin: GameObject) {
        cachedGameObject = origin
    }
}