package jackTheFishman

import java.lang.ref.WeakReference

class EventList<T> where T : Any {
    private val callbacks = mutableListOf<WeakReference<(T) -> Unit>>()

    fun add(function: (T) -> Unit) {
        val ref = WeakReference(function)
        callbacks.add(ref)
    }

    operator fun invoke(arg: T) {
        callbacks.removeIf { it.get() == null }
        for (callback in callbacks) {
            callback.get()!!(arg)
        }
    }
}
