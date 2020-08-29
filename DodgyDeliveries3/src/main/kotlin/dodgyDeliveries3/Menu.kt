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

    /*val panelObject = GameObject("Panel").also { gameObject ->
        val panel = Panel().also {
            it.style.display = Style.DisplayType.FLEX
        }

        panel.add()

        gameObject.addComponent(LeguiComponentWrapper(panel)).also {
        }
    }*/

    val titelImage = GameObject("TitleImage").also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent<ImageComponent>().also {
            it.texture = Loader.createViaPath<Texture2D>("textures/titleWithBG.png")
            windowUpdate.logicalSize = {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, (Window.logicalSize.x() * 0.5f) / 3.931f)
            }
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.y() * 0.1f)
            }
            it.leguiComponent.style.border.isEnabled = false
        }
    }
    Scene.active.spawn(titelImage)

    val startButton = makeButton("StartButton", "START", { Window.logicalSize.y() * 0.4f }) { loadDefaultScene() }
    Scene.active.spawn(startButton)

    val quitButton = makeButton("QuitButton", "QUIT", { Window.logicalSize.y() * 0.4f + 260f }) { close() } // 520f
    Scene.active.spawn(quitButton)

    val creditButton: GameObject // Just declaring

    val optionsButton = makeButton(
        "OptionsButton",
        "OPTIONS",
        { Window.logicalSize.y() * 0.4f + 130f }) {
        Scene.active.destroy(it.gameObject); Scene.active.destroy(
        startButton
    ); Scene.active.destroy(quitButton); makeOptionsMenu()
    }
    Scene.active.spawn(optionsButton)   // 310f

    creditButton = GameObject("CreditButton").also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent<ImageComponent>().also {
            it.texture = Loader.createViaPath<Texture2D>("textures/krakula-xl.png")
            windowUpdate.logicalSize = {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.x() * 0.1f)
            }
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(
                    Window.logicalSize.x() * 0.88f,
                    Window.logicalSize.y() - (Window.logicalSize.x() - (Window.logicalSize.x() * 0.88f))
                )
            }
            it.onPressed = {
                Scene.active.destroy(it.gameObject); Scene.active.
            }
        }
    }
    Scene.active.spawn(creditButton)
}

fun makeOptionsMenu() {

    val volume = GameObject("Volume").also { gameObject ->
        gameObject.addComponent<Slider>().also {
            it.position = Vector2f(100f,100f)
            it.logicalSize = Vector2f(800f, 100f)
            it.leguiComponent.minValue = 0f
            it.leguiComponent.maxValue = 1f
            it.leguiComponent.stepSize = 0.1f
            it.onChanged = { value ->
                Audio.Listener.gain = value
            }

        }
        Scene.active.spawn(gameObject)
    }

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

fun makeCredits() {

}


fun makeButton(name: String, text: String, yPosition: () -> Float, onPressedFunc: (self: Button) -> Unit): GameObject {
    return GameObject(name).also { gameObject ->
        val windowUpdate = gameObject.addComponent<LeguiWindowUpdate>()
        gameObject.addComponent<Button>().also {
            it.logicalFontSize = 42F
            it.text = text
            windowUpdate.logicalPosition = {
                it.scaledPosition = Vector2f(Window.logicalSize.x() * 0.2f, yPosition())
            }
            it.leguiComponent.style.background.color = Vector4f(ColorPalette.ORANGE, 0.7f)
            it.leguiComponent.hoveredStyle.background.color = Vector4f(ColorPalette.BLUE, 0.7f)
            it.leguiComponent.style.setBorderRadius(10f)
            it.fontName = "Sugarpunch"
            it.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 130f)
            it.onPressed = {
                onPressedFunc(it)
            }
            windowUpdate.logicalSize = {
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }

        }
    }
}


