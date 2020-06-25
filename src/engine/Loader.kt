package engine

import engine.util.ICreateViaPath
import java.io.File
import kotlin.reflect.full.companionObjectInstance

object Loader {
    var root = ""
    val rootFolder: File
        get() {
            val rootFile = File(root)
            check(rootFile.exists()) { "root not found ${rootFile.path}" }
            check(rootFile.isDirectory) { "root is not a directory" }
            return rootFile
        }

    inline fun <reified T> createViaPath(obj: ICreateViaPath<T>, path: String): T {
        val moddedPath = rootFolder.resolve(path)
        check(moddedPath.exists()) { "file not found ${moddedPath.path}" }
        return obj.createViaPath(moddedPath.path)
    }

    inline fun <reified T> createViaPath(path: String): T {
        val moddedPath = rootFolder.resolve(path)
        check(moddedPath.exists()) { "file not found ${moddedPath.path}" }
        val obj = T::class.companionObjectInstance
        check(obj != null) { "Class dose not define a companionObject" }
        check(obj is ICreateViaPath<*>) { "companionObject dose not implement ICreateViaPath" }
        @Suppress("UNCHECKED_CAST") val obj2 = obj as ICreateViaPath<T>
        return obj2.createViaPath(moddedPath.path)
    }
}