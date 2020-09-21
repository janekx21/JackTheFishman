package jackTheFishman.resouces

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.absoluteValue

object TempFileWrapper {
    var prefix: File = File("./")

    fun pathFromResourcePath(resourcePath: File): File {
        val resourcePathWithPrefix = resourceWithPrefix(resourcePath)
        checkFileResourceExists(resourcePathWithPrefix)
        return wrapResourceInTempFile(resourcePathWithPrefix)
    }

    private fun resourceWithPrefix(resourcePath: File): File {
        return prefix.resolve(resourcePath)
    }

    private fun checkFileResourceExists(resourcePathWithPrefix: File) {
        val resource = ClassLoader.getSystemResource(resourcePathWithPrefix.invariantSeparatorsPath)
        checkNotNull(resource) { "Resource at $resourcePathWithPrefix not found" }
    }

    private fun wrapResourceInTempFile(pathWithPrefix: File): File {
        val tempFile = createTempFile(pathWithPrefix)

        val inputStream = createInputStream(pathWithPrefix)
        val outputStream = createOutputStream(tempFile)

        outputStream.pipe(inputStream)

        return tempFile
    }

    private fun createTempFile(pathWithPrefix: File): File {
        val hash = pathWithPrefix.path.hashCode()
        return File.createTempFile("resource_${hash.absoluteValue}", ".${pathWithPrefix.extension}")
    }

    private fun createInputStream(pathWithPrefix: File): InputStream {
        val inputStream = ClassLoader.getSystemResourceAsStream(pathWithPrefix.invariantSeparatorsPath)
        checkNotNull(inputStream) { "File stream could not be opened to resource at $pathWithPrefix" }
        return inputStream
    }

    private fun createOutputStream(tempFile: File): FileOutputStream {
        return FileOutputStream(tempFile)
    }

    private fun OutputStream.pipe(source: InputStream) {
        val data = ByteArray(source.available())
        source.read(data)
        write(data)
    }
}
