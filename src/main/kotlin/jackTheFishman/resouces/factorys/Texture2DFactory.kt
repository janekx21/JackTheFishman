package jackTheFishman.resouces.factorys

import jackTheFishman.graphics.ImageFile
import jackTheFishman.graphics.Texture2D
import jackTheFishman.resouces.ResourceFactory
import jackTheFishman.resouces.TempFileWrapper
import jackTheFishman.resouces.viaPath.Texture2DViaPath
import java.io.File

class Texture2DFactory : ResourceFactory<Texture2D> {
    override fun createViaPath(resourcePath: File): Texture2D {
        val path = TempFileWrapper.pathFromResourcePath(resourcePath)
        // TODO put file generation back into Texture2DViaPath for serialization
        val texture = Texture2DViaPath(resourcePath.path)
        ImageFile(path.path).use {
            texture.setData(it.data, it.size)
        }
        return texture
    }
}
