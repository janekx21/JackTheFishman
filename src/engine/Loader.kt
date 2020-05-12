package engine

import engine.util.ICreateViaPath
import java.io.File

object Loader {
    var root = ""
    inline fun <reified T> createViaPath(obj: ICreateViaPath<T>, path: String): T {
        val moddedPath = File(root).resolve(path).path
        return obj.createViaPath(moddedPath)
    }
}