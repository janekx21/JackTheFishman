package dodgyDeliveries3.components

import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector2fc
import org.liquidengine.legui.component.Label
import org.liquidengine.legui.style.font.Font
import org.liquidengine.legui.style.font.FontRegistry

open class Text : LeguiComponent(
    Label("").also {
        it.textState.fontSize = 16f * Window.contentScale
        it.position = Vector2f(8F, 8F)
    }
) {
    var text: String = ""
        get() = field
        set(value) {
            leguiLabel.textState.text = value
            field = value
        }

    var fontSize: Float
        get() = leguiLabel.textState.fontSize
        set(value) {
            leguiLabel.textState.fontSize = value
        }

    var scaledFontSize: Float
        get() = fontSize / Window.contentScale
        set(value) {
            fontSize = value * Window.contentScale
        }

    var fontName: String = FontRegistry.DEFAULT
        get() = field
        set(value) {
            leguiLabel.textState.font = value
            field = value
        }

    private val leguiLabel: Label
        get() = leguiComponent as Label
}
