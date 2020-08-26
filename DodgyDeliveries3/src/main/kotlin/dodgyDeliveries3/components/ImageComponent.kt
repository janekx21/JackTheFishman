package dodgyDeliveries3.components

import com.beust.klaxon.Json
import jackTheFishman.engine.util.ICreateViaPath
import org.liquidengine.legui.component.ImageView
import org.liquidengine.legui.image.Image
import org.liquidengine.legui.image.loader.ImageLoader

class ImageComponent(path: String? = null) : LeguiComponent<ImageView>(ImageView()) {
    var path: String? = path
        set(value) {
            image = when {
                value != null -> ImageLoader.loadImage(value)
                else -> null
            }
            field = value
        }

    @Json(ignored = true)
    var image: Image? = null
        private set(value) {
            leguiComponent.image = value
            field = value
        }
}
