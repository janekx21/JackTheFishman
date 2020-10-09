import jackTheFishman.Game
import jackTheFishman.GlfwWindow
import org.joml.Vector2i

object FlyExample : Game(GlfwWindow(startSize = Vector2i(100, 200))) {
    override fun update() {
        super.update()
        println("I am alive!")
    }
}

fun main() {
    FlyExample.run()
}
