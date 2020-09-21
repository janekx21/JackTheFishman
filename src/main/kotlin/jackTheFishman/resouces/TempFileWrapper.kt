package jackTheFishman.resouces

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.absoluteValue

object TempFileWrapper {
    var prefix: File = File("./")

    fun pathFromResourcePath(resourcePath: File): File {
        checkFileResourceExists(resourcePath)
        return File(wrapResourceInTempFile(resourcePath))
    }

    private fun checkFileResourceExists(resourcePath: File) {
        val pathWithPrefix = resourceWithPrefix(resourcePath)
        val resource = ClassLoader.getSystemResource(pathWithPrefix.invariantSeparatorsPath)
        checkNotNull(resource) { "Resource at $pathWithPrefix not found" }
    }

    private fun resourceWithPrefix(resourcePath: File): File {
        return prefix.resolve(resourcePath)
    }

    private fun wrapResourceInTempFile(resourcePath: File): String {
        val pathWithPrefix = resourceWithPrefix(resourcePath)
        val hash = resourcePath.path.hashCode()

        val inputStream = ClassLoader.getSystemResourceAsStream(pathWithPrefix.invariantSeparatorsPath)
        checkNotNull(inputStream) { "File stream could not be opened to resource at $resourcePath" }

        val tempFile = File.createTempFile("resource_${hash.absoluteValue}", ".${pathWithPrefix.extension}")
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
