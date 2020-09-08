package dodgyDeliveries3

import dodgyDeliveries3.components.Text
import dodgyDeliveries3.components.TextInput
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Window
import jackTheFishman.engine.audio.Sample
import org.joml.Vector2f
import org.joml.Vector4f
import org.liquidengine.legui.component.optional.align.HorizontalAlign
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

fun getPath() {
    GameObject("SongLoader").also { gameObject ->
        gameObject.addComponent(makeLogo())
        gameObject.addComponent(makeButton(
            "CHOOSE MUSIC",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            },
            {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
                } catch (ex: Exception) {

                }
                val fileChoose = JFileChooser()
                fileChoose.dialogTitle = "Select .OGG File"
                val filter = FileNameExtensionFilter("Ogg Files", "ogg")
                fileChoose.fileFilter = filter
                val fullscreen = Window.fullscreen
                if (Window.fullscreen) {
                    Window.fullscreen = false
                }
                fileChoose.showOpenDialog(null)
                if (fileChoose.selectedFile != null) {
                    Scene.active.destroy(gameObject)
                    getBpm(Sample(Sample.getSampleFileViaPath(fileChoose.selectedFile.path)))
                }
                if (fullscreen) {
                    Window.fullscreen = true
                }
            }
        ))

        gameObject.addComponent(makeButton("BACK",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f + 130f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }, {
                Scene.active.destroy(it.gameObject)
                makeSelectLevelMenu()
            })
        )
        Scene.active.spawn(gameObject)
    }
}

fun getBpm(sample: Sample) {

    GameObject("SongLoader").also { gameObject ->
        gameObject.addComponent(makeLogo())
        gameObject.addComponent<Text>().also { text ->
            text.logicalFontSize = 42F
            text.fontName = "Sugarpunch"
            text.text = "PLEASE ENTER THE BPM"
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.BLUE, 1f)
            text.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            text.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.3f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }
        }
        gameObject.addComponent<TextInput>().also { textinput ->
            textinput.onChanged = {
            }
            textinput.leguiComponent.textState.font = "Sugarpunch"
            textinput.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            textinput.leguiComponent.textState.textColor = Vector4f(ColorPalette.ORANGE, 1f)
            textinput.leguiComponent.style.background.color = Vector4f(ColorPalette.BLUE, 1f)
            textinput.leguiComponent.textState.fontSize = 50f
            textinput.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.3f + 100f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }
        }
        gameObject.addComponent(makeButton(
            "CONFIRM",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.3f + 230f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            },
            {
                val bpmString = gameObject.getComponent<TextInput>().leguiComponent.textState.text
                bpmString.replace("\\s".toRegex(), "")
                val bpmFloat = bpmString.toFloatOrNull()
                if (bpmFloat != null && bpmString != "") {
                    Scene.active.destroy(gameObject)
                    getOffset(sample, bpmFloat)
                } else {
                    gameObject.getComponent<TextInput>().leguiComponent.textState.text = "INVALID INPUT"
                }
            }
        ))

        gameObject.addComponent(makeButton("BACK",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.3f + 360f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }, {
                Scene.active.destroy(it.gameObject)
                getPath()
            })
        )

        Scene.active.spawn(gameObject)
    }
}

fun getOffset(sample: Sample, bpm: Float) {
    GameObject("SongLoader").also { gameObject ->
        gameObject.addComponent(makeLogo())
        gameObject.addComponent<Text>().also { text ->
            text.logicalFontSize = 42F
            text.fontName = "Sugarpunch"
            text.text = "PLEASE ENTER THE OFFSET"
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.BLUE, 1f)
            text.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            text.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.3f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }
        }
        gameObject.addComponent<TextInput>().also { textinput ->
            textinput.onChanged = {
            }
            textinput.leguiComponent.textState.font = "Sugarpunch"
            textinput.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            textinput.leguiComponent.textState.textColor = Vector4f(ColorPalette.ORANGE, 1f)
            textinput.leguiComponent.style.background.color = Vector4f(ColorPalette.BLUE, 1f)
            textinput.leguiComponent.textState.fontSize = 50f
            textinput.onSizeChange = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.3f + 100f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }
        }

        gameObject.addComponent(makeButton(
            "EASY",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.075f, Window.logicalSize.y() * 0.3f + 230f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.25f, 100f)
            },
            {

                val offsetString = gameObject.getComponent<TextInput>().leguiComponent.textState.text
                offsetString.replace("\\s".toRegex(), "")
                val offsetFloat = offsetString.toFloatOrNull()
                if (offsetFloat != null && offsetString != "") {
                    Scene.active.destroy(gameObject)
                    loadDefaultSceneFromOwnSample(sample, offsetFloat, bpm, Difficulty.EASY)
                } else {
                    gameObject.getComponent<TextInput>().leguiComponent.textState.text = "INVALID INPUT"
                }
            }
        ))

        gameObject.addComponent(makeButton(
            "HARD",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.375f, Window.logicalSize.y() * 0.3f + 230f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.25f, 100f)
            },
            {
                val offset = gameObject.getComponent<TextInput>().leguiComponent.textState.text
                offset.replace("\\s".toRegex(), "")
                Scene.active.destroy(gameObject)
                loadDefaultSceneFromOwnSample(sample, offset.toFloat(), bpm, Difficulty.HARD)
            }
        ))
        gameObject.addComponent(makeButton("BACK",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.3f + 360f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }, {
                Scene.active.destroy(it.gameObject)
                getBpm(sample)
            })
        )
        Scene.active.spawn(gameObject)
    }
}
