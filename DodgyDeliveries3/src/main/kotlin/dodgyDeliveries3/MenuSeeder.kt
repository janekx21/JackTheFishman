package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.prefabs.makeBackwardTunnel
import dodgyDeliveries3.prefabs.makeLight
import dodgyDeliveries3.prefabs.makeMenuPlayerWithBox
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Audio
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Window
import jackTheFishman.engine.Window.close
import jackTheFishman.engine.math.times
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.liquidengine.legui.component.optional.align.HorizontalAlign
import org.liquidengine.legui.style.font.FontRegistry
import java.awt.Desktop
import java.net.URI

fun loadMenu() {
    removeAllGameObjects()

    configFont()

    GameObject("Main Menu Music").also { gameObject ->
        gameObject.addComponent<Music>().also {
            it.sample = Loader.createViaPath("music/mainMenuMusic.ogg")
            it.play()
        }
        Scene.active.spawn(gameObject)
    }

    GameObject("Camera").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, 2f)
        }
        gameObject.addComponent<AudioListener>()
        gameObject.addComponent<DebugCameraMovement>()

        Camera.main = gameObject.addComponent()
        Scene.active.spawn(gameObject)
    }

    Scene.active.spawn(makeLight(Vector3f(0f, 0f, -5f), Vector3f(ColorPalette.BLUE) * 2f))

    Scene.active.spawn(makeLight(Vector3f(0f, 0f, -1f), Vector3f(ColorPalette.ORANGE) * 2f))

    val (player, box) = makeMenuPlayerWithBox()
    Scene.active.spawn(player)
    Scene.active.spawn(box)

    Scene.active.spawn(makeBackwardTunnel(50f))
    Scene.active.spawn(makeBackwardTunnel(-50f))

    makeMainMenu()
}

fun configFont() {
    FontRegistry.registerFont("Sugarpunch", "dodgyDeliveries3/fonts/Sugarpunch.otf")
}

fun makeMainMenu() {
    GameObject("MainMenu").also { gameObject ->
        gameObject.addComponent(makeLogo())

        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                close()
            }
        }

        // Startbutton
        gameObject.addComponent(
            makeButton("START",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    Scene.active.destroy(gameObject)
                    makeSelectLevelMenu()
                })
        )

        // Optionsbutton
        gameObject.addComponent(
            makeButton(
                "OPTIONS",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f + 130f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    Scene.active.destroy(gameObject)
                    makeOptionsMenu()
                })
        )

        // Quitbutton
        gameObject.addComponent(
            makeButton(
                "QUIT",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f + 260f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    close()
                })
        )

        // Creditbutton (KrakulaLogo)
        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/krakula-xl.png"),
            {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.x() * 0.1f)
                it.logicalPosition = Vector2f(
                    Window.logicalSize.x() * 0.88f,
                    Window.logicalSize.y() - (Window.logicalSize.x() - (Window.logicalSize.x() * 0.88f))
                )
            },
            {
                Scene.active.destroy(gameObject)
                makeCredits()
            })
        )

        Scene.active.spawn(gameObject)
    }
}

fun makeOptionsMenu() {
    GameObject("OptionsMenu").also { gameObject ->
        gameObject.addComponent(makeLogo())

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
            text.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.y() * 0.45f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, 0.1f)
            }
        }

        // Volumeslider
        gameObject.addComponent<Slider>().also { slider ->
            slider.leguiComponent.value = Audio.Listener.gain
            slider.leguiComponent.minValue = 0f
            slider.leguiComponent.maxValue = 1f
            slider.leguiComponent.stepSize = 0.1f
            slider.leguiComponent.sliderColor = Vector4f(ColorPalette.ORANGE, 1f)
            slider.leguiComponent.sliderSize = 35f
            slider.leguiComponent.sliderActiveColor = Vector4f(ColorPalette.WHITE, 0.7f)
            slider.onChanged = { value ->
                Audio.Listener.gain = value
                DodgyDeliveries3.config = DodgyDeliveries3.config
                    .copy(volume = value)
                    .also { it.saveToDefaultPath() }
                gameObject.getComponent<Text>().text = "VOLUME: " + "%.2f".format(Audio.Listener.gain)
            }
            slider.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.45f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }
        }

        gameObject.addComponent(
            makeButton("FULLSCREEN TOGGLE",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.6f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    Window.fullscreen = !Window.fullscreen
                })
        )

        gameObject.addComponent(
            makeTransparentImage(
                Loader.createViaPath("textures/krakula-xl.png"),
                {
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.x() * 0.1f)
                    it.logicalPosition = Vector2f(
                        Window.logicalSize.x() * 0.88f,
                        Window.logicalSize.y() - (Window.logicalSize.x() - (Window.logicalSize.x() * 0.88f))
                    )
                })
        )

        // BackButton
        gameObject.addComponent(makeBackButton { Window.logicalSize.y() * 0.8f })

        Scene.active.spawn(gameObject)
    }
}

fun makeSelectLevelMenu() {
    GameObject("OptionsMenu").also { gameObject ->
        gameObject.addComponent(makeLogo())

        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Scene.active.destroy(gameObject)
                makeMainMenu()
            }
        }

        // LEVEL 1
        gameObject.addComponent(makeButton("LEVEL 1",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            },
            {
                Scene.active.destroy(gameObject)
                loadDefaultScene(Difficulty.EASY)
            })
        )

        // LEVEL 2
        gameObject.addComponent(makeButton("LEVEL 2",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f + 130f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            },
            {
                Scene.active.destroy(gameObject)
                loadDefaultScene(Difficulty.HARD)
            })
        )

        gameObject.addComponent(makeBackButton { Window.logicalSize.y() * 0.4f + 260f })

        gameObject.addComponent(
            makeTransparentImage(
                Loader.createViaPath("textures/krakula-xl.png"),
                {
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.x() * 0.1f)
                    it.logicalPosition = Vector2f(
                        Window.logicalSize.x() * 0.88f,
                        Window.logicalSize.y() - (Window.logicalSize.x() - (Window.logicalSize.x() * 0.88f))
                    )
                })
        )

        Scene.active.spawn(gameObject)
    }

}

fun makeCredits() {
    GameObject("CreditMenu").also { gameObject ->
        gameObject.addComponent(makeLogo())

        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Scene.active.destroy(gameObject)
                makeMainMenu()
            }
        }

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/credits.png"),
            {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.55f, (Window.logicalSize.x() * 0.55f) / 3.72f)
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.075f, Window.logicalSize.y() * 0.35f)
            }
        ))

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/lwjgl_logo.png"),
            {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, (Window.logicalSize.x() * 0.1f) / 2.77f)
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.075f, Window.logicalSize.y() * 0.65f)
            },
            {
                Desktop.getDesktop().browse(URI("https://www.lwjgl.org/"))
            }
        ))

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/klaxon_logo.png"),
            {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, (Window.logicalSize.x() * 0.1f) / 2.77f)
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.225f, Window.logicalSize.y() * 0.65f)
            },
            {
                Desktop.getDesktop().browse(URI("https://github.com/cbeust/klaxon/"))
            }
        ))

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/legui_logo.png"),
            {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, (Window.logicalSize.x() * 0.1f) / 2.77f)
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.375f, Window.logicalSize.y() * 0.65f)
            },
            {
                Desktop.getDesktop().browse(URI("https://github.com/SpinyOwl/legui"))
            }
        ))

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/jbox2d_logo.png"),
            {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, (Window.logicalSize.x() * 0.1f) / 2.77f)
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.525f, Window.logicalSize.y() * 0.65f)
            },
            {
                Desktop.getDesktop().browse(URI("http://www.jbox2d.org/"))
            }
        ))

        gameObject.addComponent(makeTransparentImage(
            Loader.createViaPath("textures/krakula-xl.png"),
            {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.x() * 0.1f)
                it.logicalPosition = Vector2f(
                    Window.logicalSize.x() * 0.88f,
                    Window.logicalSize.y() - (Window.logicalSize.x() - (Window.logicalSize.x() * 0.88f))
                )
            },
            {
                Desktop.getDesktop().browse(URI("https://www.krakula.com"))
            }
        ))

        gameObject.addComponent(makeBackButton { Window.logicalSize.y() * 0.8f })

        Scene.active.spawn(gameObject)
    }
}

fun makeLogo(): Component {
    return ImageComponent().also { image ->
        image.texture = Loader.createViaPath("textures/title.png")
        image.texture!!.makeLinear()
        image.leguiComponent.style.background.color = Vector4f(0f, 0f, 0f, 0f)
        image.leguiComponent.style.border.isEnabled = false
        image.leguiComponent.style.shadow.color = Vector4f(0f, 0f, 0f, 0f)
        image.onSizeChange = {
            image.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, (Window.logicalSize.x() * 0.5f) / 3.931f)
            image.logicalPosition = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.y() * 0.1f)
        }
    }
}
