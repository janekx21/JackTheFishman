package jackTheFishman.engine.graphics

import jackTheFishman.engine.util.IUsable

class Texture2DViaPath(val path: String) : Texture2D(), IUsable {
    init {
        ImageFile(path).use {
            setData(it.data, it.size)
        }
    }
}