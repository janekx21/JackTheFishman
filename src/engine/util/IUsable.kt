package engine.util

interface IUsable {
    fun use(callback: () -> Unit)
}