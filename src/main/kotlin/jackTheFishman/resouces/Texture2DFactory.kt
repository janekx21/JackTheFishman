package jackTheFishman.resouces

import jackTheFishman.graphics.ImageFile
import jackTheFishman.graphics.Texture2D
import jackTheFishman.resouces.viaPath.Texture2DViaPath
import java.io.File

class Texture2DFactory : ResourceFactory<Texture2D> {
    override fun createViaPath(resourcePath: File): Texture2D {
        val path = TempFileWrapper.pathFromResourcePath(resourcePath)
        val texture = Texture2DViaPath(resourcePath.path)
        ImageFile(path.path).use {
            texture.setData(it.data, it.size)
        }
        return texture
    }
}
