fun main() {
    Window.init()
    Window(Point(640, 480), "FooBar").use {
        it.loop()
    }
    Window.close()
}
