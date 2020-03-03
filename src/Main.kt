fun main() {
    init()
    Window(Point(640, 480), "FooBar").use {
        it.loop()
    }
    close()
}
