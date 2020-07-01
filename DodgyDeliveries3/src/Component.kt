abstract class Component(val gameObject: GameObject) {
    abstract fun update()
    abstract fun draw()

    val transform: Transform
        get() = gameObject.getComponent()
}