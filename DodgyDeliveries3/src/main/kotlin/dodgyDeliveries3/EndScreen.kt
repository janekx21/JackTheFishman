package dodgyDeliveries3

import dodgyDeliveries3.components.EscapeHandler
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.Window
import org.joml.Vector2f

fun loadWinScreen(): GameObject {
    return GameObject("WinScreen").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Time.timeScale = 1f
                Scene.active.destroy(gameObject)
                loadMenu()
            }
        }

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/complete.png"),
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.3f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, (Window.logicalSize.x() * 0.5f) / 6.177f)
            }
        ))

        gameObject.addComponent(
            makeButton("TO MAIN MENU",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.5f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                })
            {
                Time.timeScale = 1f
                Scene.active.destroy(gameObject)
                loadMenu()
            })
    }
}

fun loadLooseScreen(): GameObject {
    return GameObject("LooseScreen").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Time.timeScale = 1f
                Scene.active.destroy(gameObject)
                loadMenu()
            }
        }

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/rip.png"),
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.3f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, (Window.logicalSize.x() * 0.5f) / 4.79f)
            }
        ))

        gameObject.addComponent(
            makeButton("TO MAIN MENU",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.4f + 150f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                })
            {
                Time.timeScale = 1f
                Scene.active.destroy(gameObject)
                loadMenu()
            })
    }
}
