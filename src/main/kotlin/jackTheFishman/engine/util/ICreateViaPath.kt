package jackTheFishman.engine.util

interface ICreateViaPath<T> {
    fun createViaPath(path: String): T
}