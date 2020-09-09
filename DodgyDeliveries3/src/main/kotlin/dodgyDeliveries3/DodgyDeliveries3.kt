package dodgyDeliveries3

import dodgyDeliveries3.components.Camera
import dodgyDeliveries3.util.Configuration
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.Audio
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
    private val cursor = Loader.createViaPath<Texture2D>("logos/cursor.png")

    private val defaultConfig = Configuration(volume = 0.5f, fullscreen = true, showGrid = true)

    var config = Configuration.loadFromDefaultPathOrNull() ?: defaultConfig

    init {
        // set default texture color to white
        Texture2D.setDefaultTexture2DWhite()
        configCulling()
        loadMenu()
        configWindow()
        Audio.Listener.gain = config.volume
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
        Window.setCursor(cursor)
    }

    override fun update() {
        if (Window.fullscreen != config.fullscreen) {
            Window.fullscreen = config.fullscreen
        }
        Scene.active.update()
    }

    override fun draw() {
        glClearColor(.1f, .1f, .15f, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        Scene.active.draw()
        Debug.draw()
    }
}
