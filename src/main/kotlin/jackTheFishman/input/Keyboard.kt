package jackTheFishman.input

interface Keyboard {
    fun down(key: KeyboardKey): Boolean
    fun up(key: KeyboardKey): Boolean
    fun changed(key: KeyboardKey): Boolean
    fun justDown(key: KeyboardKey): Boolean
    fun justUp(key: KeyboardKey): Boolean
}
