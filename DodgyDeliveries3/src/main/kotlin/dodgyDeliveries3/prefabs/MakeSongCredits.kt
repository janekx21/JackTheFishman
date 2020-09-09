package dodgyDeliveries3.prefabs

import dodgyDeliveries3.*
import dodgyDeliveries3.components.EscapeHandler
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Window
import org.joml.Vector2f
import java.awt.Desktop
import java.net.URI

fun makeSongCredits() {
    GameObject("SongCredits").also { gameObject ->
        gameObject.addComponent(makeLogo())

        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Scene.active.destroy(gameObject)
                makeSelectLevelMenu()
            }
        }

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/songCredits.png"),
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.025f, Window.logicalSize.y() * 0.35f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.65f, (Window.logicalSize.y() * 0.65f) / 1.6f)
            }
        ))

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/ccLogo.png"),
            {
                it.logicalPosition = Vector2f(
                    Window.logicalSize.x() * 0.45f,
                    Window.logicalSize.y() * 0.35f + (Window.logicalSize.y() * 0.65f) / 1.6f - Window.logicalSize.y() * 0.075f
                )
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.05f, Window.logicalSize.y() * 0.05f)
            },
            {
                Desktop.getDesktop().browse(URI("https://creativecommons.org/licenses/by/3.0/legalcode"))
            }
        ))

        gameObject.addComponent(
            makeButton("TO MAIN MENU",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.8f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                })
            {
                Scene.active.destroy(gameObject)
                makeSelectLevelMenu()
            })
        Scene.active.spawn(gameObject)
    }
}
