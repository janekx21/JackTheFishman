package dodgyDeliveries3.components

import org.joml.Vector2f
import org.liquidengine.legui.component.TextInput

open class TextInput(var onChanged: (value: Float) -> Unit = {}) : LeguiComponentWrapper<TextInput>(
    TextInput().also {
        it.position = Vector2f(8F, 8F)
        it.isEditable = true
    }
)

