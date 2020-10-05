
import jackTheFishman.Game
import jackTheFishman.GlfwWindow

object FlyExample : Game(GlfwWindow()) {
    override fun update() {
        super.update()
        println("I am alive!")
    }
}

fun main() {
    FlyExample.run()
}
