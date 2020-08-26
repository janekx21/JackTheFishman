package dodgyDeliveries3

import dodgyDeliveries3.components.Camera
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.Game
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Window
import jackTheFishman.engine.graphics.Texture2D
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glCullFace
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL11C.GL_BACK
import org.lwjgl.opengl.GL11C.GL_CULL_FACE

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
        loadDefaultScene()
        configWindow()
    }

    private fun configCulling() {
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
        GL11.glClearColor(.1f, .1f, .15f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        Scene.active.draw()
        Debug.draw()
    }
}
