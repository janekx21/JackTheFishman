package dodgyDeliveries3

import dodgyDeliveries3.components.Button
import dodgyDeliveries3.components.HealthIndicator
import dodgyDeliveries3.components.ImageComponent
import dodgyDeliveries3.components.LeguiComponentWrapper
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Window
import jackTheFishman.engine.graphics.Texture2D
import org.joml.Vector2f
import org.joml.Vector4f
import org.liquidengine.legui.component.ImageView

fun makeButton(
    text: String,
    onSizeChange: (self: Button) -> Unit,
    onClick: (self: Button) -> Unit
): Component {
    return Button().also { button ->
        button.logicalFontSize = 42F
        button.text = text
        button.onSizeChange = {
            onSizeChange(button)
        }
        button.leguiComponent.style.background.color = Vector4f(ColorPalette.ORANGE, 0.7f)
        button.leguiComponent.hoveredStyle.background.color = Vector4f(ColorPalette.BLUE, 0.7f)
        button.leguiComponent.style.setBorderRadius(10f)
        button.fontName = "Sugarpunch"
        button.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
        button.onPressed = {
            onClick(button)
        }
    }
}

fun makeBackButton(yPosition: () -> Float): Component {
    return makeButton("BACK",
        {
            it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, yPosition())
            it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
        }, {
            Scene.active.destroy(it.gameObject)
            makeMainMenu()
        })
}

fun makeTransparentImage(
    texture: Texture2D,
    onSizeChange: (LeguiComponentWrapper<ImageView>) -> Unit = {},
    onPressed: () -> Unit = {}
): Component {
    return ImageComponent().also { image ->
        texture.makeLinear()
        image.texture = texture
        image.leguiComponent.style.background.color = Vector4f(0f, 0f, 0f, 0f)
        image.leguiComponent.style.border.isEnabled = false
        image.leguiComponent.style.shadow.color = Vector4f(0f, 0f, 0f, 0f)
        image.onSizeChange = onSizeChange
        image.onPressed = onPressed
    }
}

fun makeHealthIndicator(): GameObject {
    return GameObject("HealthIndicator").also {
        it.addComponent<HealthIndicator>()
    }
}
