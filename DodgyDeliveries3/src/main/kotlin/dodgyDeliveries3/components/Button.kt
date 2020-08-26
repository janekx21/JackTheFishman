package dodgyDeliveries3.components

import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.liquidengine.legui.component.Button

open class Button : LeguiTextComponent<Button>(
    Button().also {
        it.textState.fontSize = 16f * Window.contentScale
        it.position = Vector2f(8F, 8F)
    }
)
