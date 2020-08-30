package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Audio
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Window
import jackTheFishman.engine.Window.close
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.liquidengine.legui.style.font.FontRegistry

fun loadMenu() {

    for(gameObject in Scene.active.allGameObjects) {
        Scene.active.destroy(gameObject)
    }

    FontRegistry.registerFont("Sugarpunch", "dodgyDeliveries3/fonts/Sugarpunch.otf")

    GameObject("Camera").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, 2f)
        }
        gameObject.addComponent<AudioListener>()
        gameObject.addComponent<DebugCameraMovement>()

        Camera.main = gameObject.addComponent()
        Scene.active.spawn(gameObject)
    }

    GameObject("Light").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f,0f, -5f)
        }
        gameObject.addComponent<PointLight>().apply {
            color = Vector3f(ColorPalette.BLUE) * 2f
        }
        Scene.active.spawn(gameObject)
    }

    GameObject("Light").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f,0f, -1f)
        }
        gameObject.addComponent<PointLight>().apply {
            color = Vector3f(ColorPalette.ORANGE) * 2f
        }
        Scene.active.spawn(gameObject)
    }

    GameObject("Player").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.scale = Vector3fConst.one * .8f
            it.position = Vector3f(2f,-0.5f,-0.1f)
        }
        gameObject.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath("models/playerColoured.fbx")
            it.material = it.material.copy(
                albedoTexture = Loader.createViaPath<Texture2D>("textures/playerUV.png")
            )
        }
        gameObject.addComponent<MenuPlayerAnimation>()
        Scene.active.spawn(gameObject)
    }

    GameObject("Tunnel1").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, -50f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/tunnel.fbx")
            material = material.copy(
                albedoTexture = Loader.createViaPath<Texture2D>("textures/pipes/AlbedoMap.jpg"),
                normalTexture = Loader.createViaPath<Texture2D>("textures/pipes/NormalMap.png"),
                specularTexture = Loader.createViaPath<Texture2D>("textures/pipes/SpecularMap.png")
            )
        }
        gameObject.addComponent<Tunnel>().also {
            it.speed = 2f
            it.forward = false
        }
        Scene.active.spawn(gameObject)
    }
    GameObject("Tunnel2").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, 50f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/tunnel.fbx")
            material = material.copy(
                albedoTexture = Loader.createViaPath<Texture2D>("textures/pipes/AlbedoMap.jpg"),
                normalTexture = Loader.createViaPath<Texture2D>("textures/pipes/NormalMap.png"),
                specularTexture = Loader.createViaPath<Texture2D>("textures/pipes/SpecularMap.png")
            )
        }
        gameObject.addComponent<Tunnel>().also {
            it.speed = 2f
            it.forward = false
        }
        Scene.active.spawn(gameObject)
    }

    makeMainMenu()
}

fun makeMainMenu() {
    GameObject("MainMenu").also { gameObject ->
        gameObject.addComponent<ImageComponent>().also { image ->
            image.texture = Loader.createViaPath("textures/titleWithBG.png")
            image.leguiComponent.style.background.color = Vector4f(0f, 0f, 0f, 0f)
            image.leguiComponent.style.border.isEnabled = false
            image.onLayout = {
                image.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, (Window.logicalSize.x() * 0.5f) / 3.931f)
                image.logicalPosition = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.y() * 0.1f)
            }
        }

        // Startbutton
        gameObject.addComponent(makeButton("START",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) { loadDefaultScene() })

        // Optionsbutton
        gameObject.addComponent(makeButton("OPTIONS",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f + 130f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) { Scene.active.destroy(gameObject); makeOptionsMenu() })

        // Quitbutton
        gameObject.addComponent(makeButton("QUIT",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f + 260f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) { close() })

        // Creditbutton
        gameObject.addComponent<ImageComponent>().also { image ->
            image.texture = Loader.createViaPath<Texture2D>("textures/krakula-xl.png")
            image.onLayout = {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.x() * 0.1f)
                it.logicalPosition = Vector2f(
                    Window.logicalSize.x() * 0.88f,
                    Window.logicalSize.y() - (Window.logicalSize.x() - (Window.logicalSize.x() * 0.88f))
                )
            }
            image.onPressed = {
                Scene.active.destroy(gameObject); makeCredits()
            }
        }

        Scene.active.spawn(gameObject)
    }
}

fun makeOptionsMenu() {
    GameObject("OptionsMenu").also { gameObject ->
        // Volumeslider
        gameObject.addComponent<Slider>().also { slider ->
            slider.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }
            slider.leguiComponent.value = Audio.Listener.gain
            slider.leguiComponent.minValue = 0f
            slider.leguiComponent.maxValue = 1f
            slider.leguiComponent.stepSize = 0.1f
            slider.onChanged = { value ->
                Audio.Listener.gain = value
            }
        }
        // BackButton
        gameObject.addComponent(makeButton("BACK",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.2f, Window.logicalSize.y() * 0.4f + 130f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) { Scene.active.destroy(gameObject); makeMainMenu() })
        Scene.active.spawn(gameObject)
    }


}/*



    val multiSampling = GameObject("MultiSampling").also { gameObject ->
        gameObject.addComponent<Slider>().also {
            it.position = Vector2f(100f,300f)
            it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, 100f)
            it.leguiComponent.minValue = 1f
            it.leguiComponent.maxValue = 4f
            it.leguiComponent.stepSize = 1f
            it.onChanged = { value ->
                Window.multiSampleCount = value.toInt()
            }
        }
        Scene.active.spawn(gameObject)
    }

    val backButton = makeButton(
        "BackButton",
        "BACK",
        { 500f }) {
        Scene.active.destroy(it.gameObject); Scene.active.destroy(volume); Scene.active.destroy(
        multiSampling
    ); makeMainMenu()
    }
    Scene.active.spawn(backButton)
}
*/

fun makeCredits() {
    GameObject("CreditMenu").also { gameObject ->
        gameObject.addComponent<ImageComponent>().also { image ->
            image.texture = Loader.createViaPath("textures/titleWithBG.png")
            image.leguiComponent.style.background.color = Vector4f(0f, 0f, 0f, 0f)
            image.leguiComponent.style.border.isEnabled = false
            image.onLayout = {
                image.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, (Window.logicalSize.x() * 0.5f) / 3.931f)
                image.logicalPosition = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.y() * 0.1f)
            }
        }

        gameObject.addComponent(Text().also { text ->
            text.text = "CREDITS"
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            text.logicalFontSize = 42f
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f)
            }
        })
        gameObject.addComponent(Text().also { text ->
            text.text = "ARNE SCHAUMBURG"
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.ORANGE, 1f)
            text.logicalFontSize = 36f
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 50f)
            }
        })
        gameObject.addComponent(Text().also { text ->
            text.text = "BENNET MEIER"
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.ORANGE, 1f)
            text.logicalFontSize = 36f
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 100f)
            }
        })
        gameObject.addComponent(Text().also { text ->
            text.text = "JANEK WINKLER"
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.ORANGE, 1f)
            text.logicalFontSize = 36f
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 150f)
            }
        })
        gameObject.addComponent(Text().also { text ->
            text.text = "HANNES WINKLER"
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.ORANGE, 1f)
            text.logicalFontSize = 36f
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 200f)
            }
        })

        gameObject.addComponent<ImageComponent>().also { image ->
            image.texture = Loader.createViaPath("textures/legui_logo.png")
            image.leguiComponent.style.background.color = Vector4f(0f, 0f, 0f, 0f)
            image.leguiComponent.style.border.isEnabled = false
            image.leguiComponent.style.shadow.color = Vector4f(0f, 0f, 0f, 0f)
            image.onLayout = {
                image.logicalSize = Vector2f(200f, 50f)
                image.logicalPosition = Vector2f(Window.logicalSize.x() * 0.6f, Window.logicalSize.y() * 0.6f)
            }
        }

        gameObject.addComponent<ImageComponent>().also { image ->
            image.texture = Loader.createViaPath("textures/klaxon_logo.png")
            image.leguiComponent.style.background.color = Vector4f(0f, 0f, 0f, 0f)
            image.leguiComponent.style.border.isEnabled = false
            image.leguiComponent.style.shadow.color = Vector4f(0f, 0f, 0f, 0f)
            image.onLayout = {
                image.logicalSize = Vector2f(200f, 50f)
                image.logicalPosition = Vector2f(Window.logicalSize.x() * 0.7f, Window.logicalSize.y() * 0.6f)
            }
        }

        gameObject.addComponent<ImageComponent>().also { image ->
            image.texture = Loader.createViaPath("textures/lwjgl_logo.png")
            image.leguiComponent.style.background.color = Vector4f(0f, 0f, 0f, 0f)
            image.leguiComponent.style.border.isEnabled = false
            image.leguiComponent.style.shadow.color = Vector4f(0f, 0f, 0f, 0f)
            image.onLayout = {
                image.logicalSize = Vector2f(200f, 50f)
                image.logicalPosition = Vector2f(Window.logicalSize.x() * 0.6f, Window.logicalSize.y() * 0.7f)
            }
        }

        Scene.active.spawn(gameObject)
    }

}/*
    val titleText = GameObject("TitleText").also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent(Text().also {
            it.text = "CREDITS"
            it.fontName = "Sugarpunch"
            it.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            it.logicalFontSize = 42f
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(Window.logicalSize.x() * 0.3f, Window.logicalSize.y() * 0.4f)
            }
        })
    }
    Scene.active.spawn(titleText)

    val arneText = GameObject("ArneText").also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent(Text().also {
            it.text = "ARNE SCHAUMBURG"
            it.fontName = "Sugarpunch"
            it.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            it.logicalFontSize = 36f
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 50f)
            }
        })
    }
    Scene.active.spawn(arneText)

    val bennetText = GameObject("BennetText").also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent(Text().also {
            it.text = "BENNET MEIER"
            it.fontName = "Sugarpunch"
            it.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            it.logicalFontSize = 36f
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 100f)
            }
        })
    }
    Scene.active.spawn(bennetText)

    val janekText = GameObject("JanekText").also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent(Text().also {

            it.text = "JANEK WINKLER"
            it.fontName = "Sugarpunch"
            it.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            it.logicalFontSize = 36f
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 150f)
            }
        })
    }
    Scene.active.spawn(janekText)

    val hannesText = GameObject("HannesText").also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent(Text().also {
            it.text = "HANNES WINKLER"
            it.fontName = "Sugarpunch"
            it.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            it.logicalFontSize = 36f
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.4f + 200f)
            }
        })
    }
    Scene.active.spawn(hannesText)

    val backButton = makeButton(
        "BackButton",
        "BACK",
        { 500f }) {
        Scene.active.destroy(it.gameObject); Scene.active.destroy(titleText); Scene.active.destroy(arneText);Scene.active.destroy(
        bennetText
    );Scene.active.destroy(janekText);Scene.active.destroy(hannesText); makeMainMenu()
    }
    Scene.active.spawn(backButton)
}
*/

fun makeButton(
    text: String,
    onLayout: (self: Button) -> Unit,
    onPressedFunc: (self: Button) -> Unit
): Component {
    return Button().also { button ->
        button.logicalFontSize = 42F
        button.text = text
        button.onLayout = {
            onLayout(button)
        }
        button.leguiComponent.style.background.color = Vector4f(ColorPalette.ORANGE, 0.7f)
        button.leguiComponent.hoveredStyle.background.color = Vector4f(ColorPalette.BLUE, 0.7f)
        button.leguiComponent.style.setBorderRadius(10f)
        button.fontName = "Sugarpunch"
        button.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
        button.onPressed = {
            onPressedFunc(button)
        }
    }

}



