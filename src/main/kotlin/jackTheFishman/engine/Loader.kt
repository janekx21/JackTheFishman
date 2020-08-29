package jackTheFishman.engine

import jackTheFishman.engine.util.ICreateViaPath
import java.io.File
import kotlin.reflect.full.companionObjectInstance

/**
 * Object for loading and caching assets via java resources
 */
object Loader {
    var rootPath = ""
    private val rootFolder: File
        get() {
            val rootFile = resourceFileViaPath(rootPath)
            check(rootFile.exists()) { "root not found ${rootFile.path}" }
            check(rootFile.isDirectory) { "root is not a directory" }
            return rootFile
        }

    val loadedObjects = mutableMapOf<String, Any>()

    fun resourceFileViaPath(path: String): File {
        val pathWithRoot = File(rootPath).resolve(path)
        val resource = ClassLoader.getSystemResource(pathWithRoot.invariantSeparatorsPath)
        check(resource != null) { "resource at $path not found. root is $rootPath" }
        val resourcePath = resource.path
        return File(resourcePath)
    }

    @Deprecated("because its symbol is ugly. Replace with `createViaPath<T>(path)`.")
    inline fun <reified T> createViaPath(obj: ICreateViaPath<T>, path: String): T {
        val file = resourceFileViaPath(path)
        check(file.exists()) { "file not found ${file.path}" }
        return obj.createViaPath(file.path)
    }

    inline fun <reified T> createViaPath(path: String): T where T : Any {
        val instance= loadedObjects.getOrPut(path) {
            val file = resourceFileViaPath(path)
            check(file.exists()) { "file not found ${file.path}" }
            val possibleFactory= T::class.companionObjectInstance
            check(possibleFactory != null) { "Class dose not define a companionObject" }
            check(possibleFactory is ICreateViaPath<*>) { "companionObject dose not implement ICreateViaPath" }
            @Suppress("UNCHECKED_CAST") val factory = possibleFactory as ICreateViaPath<T>
            factory.createViaPath(file.path)
        }
        return instance as T
    }
}
