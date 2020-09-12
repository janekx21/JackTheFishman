package jackTheFishman.framework.graphics

import jackTheFishman.framework.util.Usable

class Texture2DViaPath(val path: String) : Texture2D(), Usable {
    init {
        ImageFile(path).use {
            setData(it.data, it.size)
        }
    }
}
