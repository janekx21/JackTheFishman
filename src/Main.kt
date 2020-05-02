import engine.Window
import org.joml.Vector2i

fun main() {
    Window.init()
    Window(Vector2i(640, 480), "FooBar").use {
        it.loop()
    }
    Window.close()
}
