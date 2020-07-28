package jackTheFishman.engine

import jackTheFishman.engine.util.ICreateViaPath
import java.io.File
import kotlin.reflect.full.companionObjectInstance

object Loader {
    var rootPath = ""
    private val rootFolder: File
        get() {
            val rootFile = resourceFileViaPath(rootPath)
            check(rootFile.exists()) { "root not found ${rootFile.path}" }
            check(rootFile.isDirectory) { "root is not a directory" }
            return rootFile
        }

    fun resourceFileViaPath(path: String): File {
        val resource = ClassLoader.getSystemResource(path)
        check(resource != null) { "resource at $path not found. root is $rootPath" }
        val resourcePath = resource.path
        println(resourcePath)
        return File(resourcePath)
    }

    inline fun <reified T> createViaPath(obj: ICreateViaPath<T>, path: String): T {
        val pathWithRoot = File(rootPath).resolve(path)
        val moddedPath = resourceFileViaPath(pathWithRoot.invariantSeparatorsPath)
        check(moddedPath.exists()) { "file not found ${moddedPath.path}" }
        return obj.createViaPath(moddedPath.path)
    }

    inline fun <reified T> createViaPath(path: String): T {
        val pathWithRoot = File(rootPath).resolve(path)
        val moddedPath = resourceFileViaPath(pathWithRoot.invariantSeparatorsPath)
        check(moddedPath.exists()) { "file not found ${moddedPath.path}" }
        val obj = T::class.companionObjectInstance
        check(obj != null) { "Class dose not define a companionObject" }
        check(obj is ICreateViaPath<*>) { "companionObject dose not implement ICreateViaPath" }
        @Suppress("UNCHECKED_CAST") val obj2 = obj as ICreateViaPath<T>
        return obj2.createViaPath(moddedPath.path)
    }
}