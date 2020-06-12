import kotlin.reflect.full.primaryConstructor

class GameObject(val name: String) {
    val components = arrayListOf<Component>()
    val transform: Transform
        get() = getComponent()

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
    }

    inline fun <reified T : Component> addComponent(): T {
        val component = T::class.primaryConstructor!!.call(this)
        addComponent(component)
        return component
    }

    inline fun <reified T : Component> getComponent(): T {
        val list = components.filterIsInstance<T>()
        check(list.isNotEmpty()) { "Component on GameObject not found" }
        return list.first()
    }

    inline fun <reified T : Component> getComponents(): List<T> {
        return components.filterIsInstance<T>()
    }
}