package jackTheFishman.util

/**
 * This interface is a abstract representation of a bindable or usable object
 */
interface Usable {
    fun use(callback: () -> Unit)
}
