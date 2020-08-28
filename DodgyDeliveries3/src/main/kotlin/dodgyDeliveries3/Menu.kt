package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Audio
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.Window
import jackTheFishman.engine.Window.close
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import org.jbox2d.common.MathUtils
import org.joml.*
import org.liquidengine.legui.component.optional.align.HorizontalAlign
import org.liquidengine.legui.component.optional.align.VerticalAlign
import org.liquidengine.legui.style.font.FontRegistry
import org.liquidengine.legui.style.length.Length
import org.liquidengine.legui.style.length.LengthType
import java.awt.Menu
import java.lang.Math
import kotlin.math.sin

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
            it.mesh = Loader.createViaPath("models/player.fbx")
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
    val startButton = makeButton("StartButton", "START", Vector2f(200f,100f)) { loadDefaultScene() }
    Scene.active.spawn(startButton)

    val quitButton = makeButton("QuitButton", "QUIT", Vector2f(200f, 520f)) { close() }
    Scene.active.spawn(quitButton)

    val optionsButton = makeButton("OptionsButton", "OPTIONS", Vector2f(200f,310f)) { Scene.active.destroy(it.gameObject); Scene.active.destroy(startButton); Scene.active.destroy(quitButton); makeOptionsMenu() }
    Scene.active.spawn(optionsButton)
}

fun makeOptionsMenu() {

    val volume = GameObject("Volume").also { gameObject ->
        gameObject.addComponent<Slider>().also {
            it.position = Vector2f(100f,100f)
            it.scaledSize = Vector2f(800f, 100f)
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
            it.scaledSize = Vector2f(800f, 100f)
            it.leguiComponent.minValue = 1f
            it.leguiComponent.maxValue = 4f
            it.leguiComponent.stepSize = 1f
            it.onChanged = { value ->
                Window.multiSampleCount = value.toInt()
            }
        }
        Scene.active.spawn(gameObject)
    }

    val backButton = makeButton("BackButton", "BACK", Vector2f(100f,500f)) { Scene.active.destroy(it.gameObject); Scene.active.destroy(volume); Scene.active.destroy(multiSampling); makeMainMenu() }
    Scene.active.spawn(backButton)
}


fun makeButton(name: String, text: String, position: Vector2fc, onPressedFunc: (self: Button) -> Unit) : GameObject {
    return GameObject(name).also {gameObject ->
        gameObject.addComponent<Button>().also {
            it.scaledFontSize = 42F
            it.text = text
            it.position = position
            it.leguiComponent.style.background.color = Vector4f(ColorPalette.ORANGE,0.7f)
            it.leguiComponent.hoveredStyle.background.color = Vector4f(ColorPalette.BLUE,0.7f)
            it.leguiComponent.style.setBorderRadius(10f)
            it.fontName = "Sugarpunch"
            it.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            it.scaledSize = Vector2f(400f, 130f)
            it.onPressed = {
                onPressedFunc(it)
            }
        }
    }
}


