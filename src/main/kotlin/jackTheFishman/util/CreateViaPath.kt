package jackTheFishman.util

/**
 * Represents a object that can be constructed with a resource that's represented with a path
 * @param T is the type of the factory result
 */
interface CreateViaPath<T> {
    fun createViaPath(path: String): T
}
