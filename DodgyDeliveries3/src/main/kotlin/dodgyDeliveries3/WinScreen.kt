package dodgyDeliveries3

import dodgyDeliveries3.components.EscapeHandler
import dodgyDeliveries3.components.Text
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Time
import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector4f
import org.liquidengine.legui.component.optional.align.HorizontalAlign

fun loadWinScreen() {
    GameObject("WinScreen").also { gameObject ->

        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Scene.active.destroy(gameObject)
                makeMainMenu()
            }
        }

        gameObject.addComponent<Text>().also { text ->
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            text.logicalFontSize = 25f
            text.text = "SUCCESSFULLY DELIVERED"
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.y() * 0.45f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, 0.1f)
            }
        }

        gameObject.addComponent(makeButton("TO MAIN MENU",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f + 260f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) {
            Time.timeScale = 1f
            loadMenu()
        })

        Scene.active.spawn(gameObject)
    }
}

fun loadLooseScreen() {
    GameObject("LooseScreen").also { gameObject ->

        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Scene.active.destroy(gameObject)
                makeMainMenu()
            }
        }

        gameObject.addComponent<Text>().also { text ->
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            text.logicalFontSize = 25f
            text.text = "GAME OVER"
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.y() * 0.45f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, 0.1f)
            }
        }

        gameObject.addComponent(makeButton("TO MAIN MENU",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f + 260f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) {
            Time.timeScale = 1f
            loadMenu()
        })

        Scene.active.spawn(gameObject)
    }
}
