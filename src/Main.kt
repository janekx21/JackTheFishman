import math.Point2

fun main() {
    Window.init()
    Window(Point2(640, 480), "FooBar").use {
        it.loop()
    }
    Window.close()
}
