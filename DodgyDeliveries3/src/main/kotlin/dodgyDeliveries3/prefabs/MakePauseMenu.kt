package dodgyDeliveries3.prefabs

import dodgyDeliveries3.*
import dodgyDeliveries3.components.EscapeHandler
import dodgyDeliveries3.components.ModelRenderer
import dodgyDeliveries3.components.Slider
import dodgyDeliveries3.components.Text
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Audio
import jackTheFishman.engine.Input
import jackTheFishman.engine.Time
import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector4f
import org.liquidengine.legui.component.optional.align.HorizontalAlign

fun makePauseOptions(): GameObject {
    return GameObject("OptionsMenu").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Scene.active.destroy(gameObject)
                Scene.active.spawn(makePauseMenu())
            }
        }

        gameObject.addComponent<Text>().also { text ->
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            text.logicalFontSize = 25f
            text.text = "VOLUME: " + "%.2f".format(Audio.Listener.gain)
            text.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.2f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, 0.1f)
            }
        }

        // Volumeslider
        gameObject.addComponent<Slider>().also { slider ->
            slider.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }
            slider.leguiComponent.value = Audio.Listener.gain
            slider.leguiComponent.minValue = 0f
            slider.leguiComponent.maxValue = 1f
            slider.leguiComponent.stepSize = 0.1f
            slider.leguiComponent.sliderColor = Vector4f(ColorPalette.ORANGE, 1f)
            slider.leguiComponent.sliderSize = 35f
            slider.leguiComponent.sliderActiveColor = Vector4f(ColorPalette.WHITE, 0.7f)
            slider.onChanged = { value ->
                Audio.Listener.gain = value
                gameObject.getComponent<Text>().text = "VOLUME: " + "%.2f".format(Audio.Listener.gain)
            }
        }

        gameObject.addComponent(
            makeButton("FULLSCREEN TOGGLE",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.35f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    val fullscreen = !Window.fullscreen
                    DodgyDeliveries3.config = DodgyDeliveries3.config.copy(fullscreen = fullscreen).also {
                        it.saveToDefaultPath()
                    }
                    Window.fullscreen = fullscreen
                })
        )

        gameObject.addComponent(
            makeButton(
                "GRID TOGGLE",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.35f + 130f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    val showGrid = !DodgyDeliveries3.config.showGrid

                    DodgyDeliveries3.config = DodgyDeliveries3.config.copy(showGrid = showGrid).also {
                        it.saveToDefaultPath()
                        Scene.active.findViaName("Grid")!!.getComponent<ModelRenderer>().mesh = if (showGrid) {
                            makeGridMesh()
                        } else {
                            null
                        }
                    }
                })
        )

        // BackButton
        gameObject.addComponent(
            makeButton("BACK",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.35f + 260f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    Scene.active.destroy(gameObject)
                    Scene.active.spawn(makePauseMenu())
                })
        )
    }
}

fun makePauseMenuOpener(): GameObject {
    return GameObject("Pause").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Input.Mouse.setMode(Input.Mouse.CursorMode.NORMAL)
                Time.timeScale = 0f
                Scene.active.spawn(makePauseMenu())
                Scene.active.destroy(gameObject)
            }
        }
    }
}

fun makePauseMenu(): GameObject {
    return GameObject("PauseMenu").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)
                Time.timeScale = 1f
                Scene.active.spawn(makePauseMenuOpener())
                Scene.active.destroy(gameObject)
            }
        }

        // Resumebutton
        gameObject.addComponent(
            makeButton("RESUME",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)
                    Time.timeScale = 1f
                    Scene.active.spawn(makePauseMenuOpener())
                    Scene.active.destroy(gameObject)
                })
        )

        // Optionsbutton
        gameObject.addComponent(
            makeButton("OPTIONS",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f + 130f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    Scene.active.destroy(gameObject)
                    Scene.active.spawn(makePauseOptions())
                })
        )

        // Quitbutton
        gameObject.addComponent(
            makeButton("TO MAIN MENU",
                {
                    it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f + 260f)
                    it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
                },
                {
                    Time.timeScale = 1f
                    Scene.active.destroy(gameObject)
                    loadMenu()
                })
        )
    }
}
