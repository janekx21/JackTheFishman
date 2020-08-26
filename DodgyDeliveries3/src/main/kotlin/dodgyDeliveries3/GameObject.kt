package dodgyDeliveries3

import com.beust.klaxon.Json
import dodgyDeliveries3.components.Collider
import dodgyDeliveries3.components.ICollisionHandler
import dodgyDeliveries3.components.Transform
import dodgyDeliveries3.util.IHasOrigin
import org.jbox2d.dynamics.contacts.Contact
import kotlin.reflect.full.primaryConstructor

data class GameObject(val name: String, val components: ArrayList<Component> = arrayListOf()) : IHasOrigin<Scene>,
    ICollisionHandler {
    @Json(ignored = true)
    val transform: Transform
        get() = getComponent()

    @Json(ignored = true)
    var cachedTransform: Transform? = components.find { component -> component is Transform } as Transform?

    @Json(ignored = true)
    var scene: Scene? = null

    init {
        for (component in components) {
            component.setOrigin(this)
        }
    }

    fun update() {
        for (component in components) {
            component.update()
        }
    }

    fun draw() {
        for (component in components) {
            component.draw()
        }
    }

    fun addComponent(component: Component) {
        component.gameObject = this
        components.add(component)
        component.start()
        if (component is Transform) {
            cachedTransform = component
        }
    }

    inline fun <reified T : Component> addComponent(): T {
        check(T::class.primaryConstructor != null) { "Primary constructor not found" }
        check(T::class.primaryConstructor!!.parameters.all { it.isOptional }) { "${T::class.simpleName}'s primary constructor needs to have a default value for all parameters" }
        val component = T::class.primaryConstructor!!.callBy(emptyMap())
        addComponent(component)
        return component
    }

    fun removeComponent(component: Component) {
        components.remove(component)
        component.stop()
        if (component is Transform) {
            cachedTransform = null
        }
    }

    fun start() {
        for (component in components) {
            component.start()
        }
    }

    fun stop() {
        for (component in components) {
            component.stop()
        }
    }

    fun destroyAllComponents() {
        for (component in components) {
            component.stop()
        }
        components.clear()
    }

    inline fun <reified T : Component> getComponent(): T {
        if (T::class == Transform::class) {
            if (cachedTransform == null) {
                cachedTransform = components.find { component -> component is Transform } as Transform
            }
            check(cachedTransform != null) { "Transform was not found" }
            return cachedTransform as T
        }
        val component = components.find { component -> component is T } as T?
        check(component != null) { "dodgyDeliveries3.Component was not found" }
        return component
    }

    inline fun <reified T : Component> getComponents(): List<T> {
        return components.filterIsInstance<T>()
    }

    override fun setOrigin(origin: Scene) {
        scene = origin
    }

    override fun beginContact(ours: Collider, other: Collider, contact: Contact) {
        components
            .filter { it is ICollisionHandler }
            .map { it as ICollisionHandler }
            .forEach { it.beginContact(ours, other, contact) }
    }

    override fun endContact(ours: Collider, other: Collider, contact: Contact) {
        components
            .filter { it is ICollisionHandler }
            .map { it as ICollisionHandler }
            .forEach { it.endContact(ours, other, contact) }
    }
}
