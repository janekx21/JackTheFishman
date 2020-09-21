package jackTheFishman.resouces

import jackTheFishman.audio.Sample
import jackTheFishman.graphics.CubeTexture
import jackTheFishman.graphics.Mesh
import jackTheFishman.graphics.Shader
import jackTheFishman.graphics.Texture2D
import jackTheFishman.util.CreateViaPath
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.absoluteValue
import kotlin.reflect.full.companionObjectInstance

/**
 * Object for loading and caching assets via java resources
 */
object ResourceLocator {
    var prefix: File = File("./")
    private val loadedObjects = mutableMapOf<String, Any>()

    fun createTexture2D(path: String): Texture2D = createViaPath(path)
    fun createCubeTexture(path: String): CubeTexture = createViaPath(path)
    fun createSample(path: String): Sample = createViaPath(path)
    fun createMesh(path: String): Mesh = createViaPath(path)
    fun createShader(path: String): Shader = createViaPath(path)

    // TODO replace with better typed alternative
    private inline fun <reified T> createViaPath(path: String): T where T : Any {
        val doCreate = {
            val file = resourceFileViaPath(path).also {
                check(it.exists()) { "File not found ${it.path}" }
            }

            val possibleFactory = T::class.companionObjectInstance.also {
                checkNotNull(it) { "Class does not define a companion object" }
                check(it is CreateViaPath<*>) { "Companion object does not implement ICreateViaPath" }
            }

            @Suppress("UNCHECKED_CAST") val factory = possibleFactory as CreateViaPath<T>
            factory.createViaPath(file.path)
        }

        return loadedObjects.getOrPut(path, doCreate) as T
    }

    private fun resourceFileViaPath(resourcePath: String): File {
        checkFileResourceExists(resourcePath)
        return File(wrapResourceInTempFile(resourcePath))
    }

    private fun checkFileResourceExists(path: String) {
        val pathWithRoot = prefix.resolve(path)
        val resource = ClassLoader.getSystemResource(pathWithRoot.invariantSeparatorsPath)
        checkNotNull(resource) { "Resource at $pathWithRoot not found" }
    }

    private fun wrapResourceInTempFile(path: String): String {
        val pathWithRoot = prefix.resolve(path)
        val hash = path.hashCode()

        val inputStream = ClassLoader.getSystemResourceAsStream(pathWithRoot.invariantSeparatorsPath)
        checkNotNull(inputStream) { "File stream could not be opened to resource at $path" }

        val tempFile = File.createTempFile("resource_${hash.absoluteValue}", ".${pathWithRoot.extension}")
        val outputStream = FileOutputStream(tempFile)

        outputStream.pipe(inputStream)

        return tempFile.path
    }

    private fun OutputStream.pipe(source: InputStream) {
        val data = ByteArray(source.available())
        source.read(data)
        write(data)
    }
}
