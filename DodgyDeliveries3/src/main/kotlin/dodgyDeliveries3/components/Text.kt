package dodgyDeliveries3.components

import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector2fc
import org.liquidengine.legui.component.Label
import org.liquidengine.legui.style.font.Font
import org.liquidengine.legui.style.font.FontRegistry

open class Text : LeguiTextComponent<Label>(
    Label("").also {
        it.textState.fontSize = 16f * Window.contentScale
        it.position = Vector2f(8F, 8F)
    }
)
