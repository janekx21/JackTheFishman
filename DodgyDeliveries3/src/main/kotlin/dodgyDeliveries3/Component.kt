package dodgyDeliveries3

import com.beust.klaxon.Json
import com.beust.klaxon.TypeFor
import dodgyDeliveries3.components.Transform
import dodgyDeliveries3.util.IDrawable
import dodgyDeliveries3.util.IHasOrigin
import dodgyDeliveries3.util.IRunnable
import dodgyDeliveries3.util.IUpdatable
import dodgyDeliveries3.util.typeAdapter.ComponentTypeAdapter

@TypeFor(field = "className", adapter = ComponentTypeAdapter::class)
abstract class Component : IHasOrigin<GameObject>, IUpdatable, IDrawable, IRunnable {
    val className: String = javaClass.name

    private var internalGameObject: GameObject? = null

    @Json(ignored = true)
    var gameObject: GameObject
        get() {
            check(internalGameObject != null) { "Component not initialized yet. Try overriding the start() function" }
            return internalGameObject!!
        }
        set(value) {
            check(internalGameObject == null) { "gameObject was already set" }
            internalGameObject = value
        }


    override fun start() {}
    override fun update() {}
    override fun draw() {}
    override fun stop() {}

    @Json(ignored = true)
    val transform: Transform
        get() = gameObject.getComponent()

    override fun setOrigin(origin: GameObject) {
        internalGameObject = origin
    }
}
