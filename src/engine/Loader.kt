package engine

import engine.util.ICreateViaPath
import java.io.File

object Loader {
    var root = ""
    inline fun <reified T> createViaPath(obj: ICreateViaPath<T>, path: String): T {
        val rootFile = File(root)
        check(rootFile.exists()) { "root not found ${rootFile.path}" }
        val moddedPath = rootFile.resolve(path)
        check(moddedPath.exists()) { "file not found ${moddedPath.path}" }
        return obj.createViaPath(moddedPath.path)
    }
}