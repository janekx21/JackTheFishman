package jackTheFishman.resouces

import java.io.File

interface ResourceFactory<T> where T : Resource {
    /**
     * Loads and creates a [Resource] with a resource path.
     * A resource path points into your resources instead of your filesystem.
     */
    fun createViaPath(resourcePath: File): T

    fun <T : Resource> ResourceFactory<T>.create(resourcePath: String): T {
        return createViaPath(File(resourcePath))
    }
}
