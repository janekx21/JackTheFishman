package dodgyDeliveries3

import dodgyDeliveries3.components.Camera
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.Game
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Window
import jackTheFishman.engine.graphics.Texture2D
import org.lwjgl.opengl.GL11.*

fun main() {
    DodgyDeliveries3.run()
}

object DodgyDeliveries3 : Game() {
    init {
        Loader.rootPath = "dodgyDeliveries3"
    }

    private val logo = Loader.createViaPath<Texture2D>("logos/logo.png")

    init {
        // set default texture color to white
        Texture2D.setDefaultTexture2DWhite()
        configCulling()
        loadMenu()
        configWindow()
    }

    private fun configCulling() {
        glFrontFace(GL_CCW)
        glCullFace(GL_BACK)
        glEnable(GL_CULL_FACE)
    }

    private fun configWindow() {
        Window.onResize = {
            Camera.main!!.updateProjectionMatrix()
        }
        Window.onResize(Window)
        Window.setIcon(logo)
    }

    override fun update() {
        Scene.active.update()
    }

    override fun draw() {
        glClearColor(.1f, .1f, .15f, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        Scene.active.draw()
        Debug.draw()
    }
}
