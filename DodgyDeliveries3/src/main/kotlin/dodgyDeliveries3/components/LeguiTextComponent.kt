package dodgyDeliveries3.components

import jackTheFishman.engine.Window
import org.liquidengine.legui.component.Component
import org.liquidengine.legui.component.TextComponent

open class LeguiTextComponent<out T>(component: T) : LeguiComponent<T>(component) where T : Component, T : TextComponent {
    var text: String
        get() = leguiComponent.textState.text
        set(value) {
            leguiComponent.textState.text = value
        }

    var fontSize: Float
        get() = leguiComponent.textState.fontSize
        set(value) {
            leguiComponent.textState.fontSize = value
        }

    var scaledFontSize: Float
        get() = fontSize / Window.contentScale
        set(value) {
            fontSize = value * Window.contentScale
        }

    var fontName: String
        get() = leguiComponent.textState.font
        set(value) {
            leguiComponent.textState.font = value
        }
}
