abstract class Component(val gameObject: GameObject) {
    abstract fun update()
    abstract fun draw()

    val transform: Component
        get() = gameObject.getComponent()

    abstract fun fromJson(json: Any?)
    abstract fun toJson(): Any?
}