import components.Transform

abstract class Component(val gameObject: GameObject) {
    abstract fun update()
    abstract fun draw()
    open fun onEnable() {}
    open fun onDisable() {}

    val transform: Transform
        get() = gameObject.getComponent()

    abstract fun toJson(): Any?

    abstract fun fromJson(json: Any?)
}