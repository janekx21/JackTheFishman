import engine.util.IJsonSerializable

abstract class Component(val gameObject: GameObject) : IJsonSerializable {
    abstract fun update()
    abstract fun draw()

    val transform: Component
        get() = gameObject.getComponent()

    abstract fun fromJson(json: Any?)
}