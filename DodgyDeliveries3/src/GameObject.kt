import components.Transform
import kotlin.reflect.full.primaryConstructor

open class GameObject(val name: String) {
    val components = arrayListOf<Component>()
    val transform: Transform
        get() = getComponent()

    var cachedTransform: Transform? = null

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
        components.add(component)
        if (component is Transform) {
            cachedTransform = component
        }
    }

    inline fun <reified T : Component> addComponent(): T {
        val component = T::class.primaryConstructor!!.call(this)
        addComponent(component)
        if (component is Transform) {
            cachedTransform = component
        }
        return component
    }

    fun removeComponent(component: Component) {
        components.remove(component)
        if (component is Transform) {
            cachedTransform = null
        }
    }

    inline fun <reified T : Component> getComponent(): T {
        if (T::class == Transform::class) {
            check(cachedTransform != null) { "Transform was not found" }
            return cachedTransform as T
        }
        val component = components.find { component -> component is T } as T?
        check(component != null) { "Component was not found" }
        return component
    }

    inline fun <reified T : Component> getComponents(): List<T> {
        return components.filterIsInstance<T>()
    }
}