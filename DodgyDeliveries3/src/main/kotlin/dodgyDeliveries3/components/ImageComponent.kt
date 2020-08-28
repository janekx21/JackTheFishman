package dodgyDeliveries3.components

import com.beust.klaxon.Json
import jackTheFishman.engine.graphics.Texture2D
import org.liquidengine.legui.component.ImageView
import org.liquidengine.legui.image.FBOImage
import org.liquidengine.legui.image.Image

class ImageComponent(texture: Texture2D? = null) : LeguiComponentWrapper<ImageView>(ImageView()) {
    @Json(ignored = true)
    var image: Image?
        get() = leguiComponent.image
        private set(value) {
            leguiComponent.image = value
        }

    var texture: Texture2D? = texture
        set(value) {
            image = when {
                value != null -> FBOImage(value.pointer, value.size.x(), value.size.y())
                else -> null
            }
            field = value
        }
}
