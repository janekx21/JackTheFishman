import engine.Game
import engine.util.IJsonSerializable
import engine.util.IJsonUnserializable
import components.Transform
import kotlin.reflect.full.primaryConstructor

open class GameObject(val name: String) : IJsonSerializable {
    val components = arrayListOf<Component>()
    val transform: Transform
        get() = getComponent()

    var cachedTransform: Transform? = null

    fun update() {
        for (component in ArrayList(components)) {
            component.update()
        }
    }

    fun draw() {
        for (component in ArrayList(components)) {
            component.draw()
        }
    }

    fun addComponent(component: Component) {
        components.add(component)
        component.onEnable()
        if (component is Transform) {
            cachedTransform = component
        }
    }

    inline fun <reified T : Component> addComponent(): T {
        check(T::class.primaryConstructor != null) { "you need a primary constructor" }
        val component = T::class.primaryConstructor!!.call(this)
        addComponent(component)
        return component
    }

    fun removeComponent(component: Component) {
        components.remove(component)
        component.onDisable()
        if (component is Transform) {
            cachedTransform = null
        }
    }

    fun onEnable() {
        for(component in components) {
            component.onEnable()
        }
    }
    fun onDisable() {
        for(component in components) {
            component.onDisable()
        }
    }

    fun destroyAllComponents() {
        for(component in components) {
            component.onDisable()
        }
        components.clear()
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

    override fun toJson(): Any? {
        return mapOf(
            "name" to name,
            "components" to components.map {
                mapOf(
                    "className" to it::class.java.name,
                    "serialization" to it.toJson()
                )
            }
        )
    }

    companion object : IJsonUnserializable<GameObject> {
        override fun fromJson(json: Any?): GameObject {
            val map = json as Map<*, *>

            val list = map["components"] as List<*>
            val name = map["name"] as String

            return GameObject(name).also { gameObject ->
                list.forEach {
                    @Suppress("NAME_SHADOWING")
                    val map = it as Map<*, *>

                    val component = Class
                        .forName(map["className"] as String)
                        .kotlin
                        .primaryConstructor!!
                        .call(gameObject) as Component

                    component.fromJson(map["serialization"])

                    gameObject.addComponent(component)
                }
            }
        }
    }
}