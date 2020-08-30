package dodgyDeliveries3.components

import jackTheFishman.engine.Window
import org.liquidengine.legui.component.Component
import org.liquidengine.legui.component.TextComponent

open class LeguiTextComponentWrapper<T>(component: T) : LeguiComponentWrapper<T>(component) where T : Component, T : TextComponent {
    var text: String
        get() = leguiComponent.textState.text
        set(value) {
            leguiComponent.textState.text = value
        }

    /**
     * @brief Size of the font in pixels
     */
    var physicalFontSize: Float
        get() = leguiComponent.textState.fontSize
        set(value) {
            leguiComponent.textState.fontSize = value
        }

    /**
     * @brief Size of the font in logical pixels, i.e.
     * physicalSize = logicalSize * Window.contentScale
     */
    var logicalFontSize: Float
        get() = physicalFontSize / Window.contentScale
        set(value) {
            physicalFontSize = value * Window.contentScale
        }

    var fontName: String
        get() = leguiComponent.textState.font
        set(value) {
            leguiComponent.textState.font = value
        }
}
