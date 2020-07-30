package jackTheFishman.engine.util

/**
 * This interface is a abstract representation of a bindable or usable object
 */
interface IUsable {
    fun use(callback: () -> Unit)
}
