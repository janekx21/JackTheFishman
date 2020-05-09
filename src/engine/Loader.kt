package engine

import engine.util.ICreateViaPath
import java.io.File

object Loader {
    var root = ""
    fun <T>createViaPath(obj: ICreateViaPath<T>, path: String): T {
        val moddedPath = File(root).resolve(path).path
        return obj.createViaPath(moddedPath)
    }
}