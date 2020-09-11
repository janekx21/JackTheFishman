package jackTheFishman.framework

import jackTheFishman.framework.util.ICreateViaPath
import java.io.File
import kotlin.math.absoluteValue
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
        checkFileResourceExists(path)
        return File(wrapResourceInTempFile(path))
    }

    private fun checkFileResourceExists(path: String) {
        val pathWithRoot = File(rootPath).resolve(path)
        val resource = ClassLoader.getSystemResource(pathWithRoot.invariantSeparatorsPath)
        check(resource != null) { "resource at $path not found. root is $rootPath" }
    }

    private fun wrapResourceInTempFile(path: String): String {
        val pathWithRoot = File(rootPath).resolve(path)
        val hash = path.hashCode()
        val tempFile = File.createTempFile("resource_${hash.absoluteValue}", ".${pathWithRoot.extension}")
        val stream = ClassLoader.getSystemResourceAsStream(pathWithRoot.invariantSeparatorsPath)
        checkNotNull(stream) { "File stream could not be opened to resource at $path" }
        tempFile.writeBytes(stream.readAllBytes())
        return tempFile.path
    }

    @Deprecated("because its symbol is ugly. Replace with `createViaPath<T>(path)`.")
    inline fun <reified T> createViaPath(obj: ICreateViaPath<T>, path: String): T {
        val file = resourceFileViaPath(path)
        check(file.exists()) { "file not found ${file.path}" }
        return obj.createViaPath(file.path)
    }

    inline fun <reified T> createViaPath(path: String, cache: Boolean = true): T where T : Any {
        val doCreate = {
            val file = resourceFileViaPath(path)
            check(file.exists()) { "file not found ${file.path}" }
            val possibleFactory = T::class.companionObjectInstance
            check(possibleFactory != null) { "Class dose not define a companionObject" }
            check(possibleFactory is ICreateViaPath<*>) { "companionObject dose not implement ICreateViaPath" }
            @Suppress("UNCHECKED_CAST") val factory = possibleFactory as ICreateViaPath<T>
            factory.createViaPath(file.path)
        }

        return if (cache) {
            loadedObjects.getOrPut(path, doCreate) as T
        } else {
            doCreate()
        }
    }
}
