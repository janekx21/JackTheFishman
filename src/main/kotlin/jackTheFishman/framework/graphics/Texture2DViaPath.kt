package jackTheFishman.framework.graphics

import jackTheFishman.framework.util.IUsable

class Texture2DViaPath(val path: String) : Texture2D(), IUsable {
    init {
        ImageFile(path).use {
            setData(it.data, it.size)
        }
    }
}
