package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Audio
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.Window
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.liquidengine.legui.component.optional.align.HorizontalAlign
import org.liquidengine.legui.style.font.FontRegistry

fun loadDefaultScene() {

    for (gameObject in Scene.active.allGameObjects) {
        Scene.active.destroy(gameObject)
    }

    makePauseOpener()

    GameObject("Music").also { gameObject ->
        gameObject.addComponent<Music>().also {
            it.sample = Loader.createViaPath<Sample>("music/Sia&SafriDuo-Chandelier(AlchimystRemix).ogg")
            it.offset = .082f
            it.bpm = 138f
            it.play()
        }
        Scene.active.spawn(gameObject)
    }

    val player = makePlayer()
    Scene.active.spawn(player)

    GameObject("Enemy Spawner").also { gameObject ->
        gameObject.addComponent<EnemySpawner>()
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
        gameObject.addComponent<Tunnel>()
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
        gameObject.addComponent<Tunnel>()
        Scene.active.spawn(gameObject)
    }

    GameObject("Camera").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 2f, 2f)
        }
        gameObject.addComponent<AudioListener>()
        gameObject.addComponent<DebugCameraMovement>()
        gameObject.addComponent<LookAt>().apply {
            target = player.transform
            offset = Vector3fConst.forward * 5f
        }
        Camera.main = gameObject.addComponent()
        Scene.active.spawn(gameObject)
    }
    GameObject("Light").also { gameObject ->
        gameObject.addComponent<Transform>()
        gameObject.addComponent<PointLight>().apply {
            color = Vector3f(ColorPalette.BLUE) * 2f
        }
        Scene.active.spawn(gameObject)
    }

    GameObject("Light").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, 10f)
        }

        gameObject.addComponent<PointLight>().apply {
            color = Vector3f(ColorPalette.ORANGE)
        }
        Scene.active.spawn(gameObject)
    }
}

fun makePauseOpener() {
    GameObject("Pause").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Time.timeScale = 0f
                makePauseMenu()
                Scene.active.destroy(gameObject)
            }
        }
        Scene.active.spawn(gameObject)
    }
}

fun makePlayer(): GameObject {
    return GameObject("Player").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.scale = Vector3fConst.one * .8f
        }
        gameObject.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath("models/playerColoured.fbx")
            it.material = it.material.copy(
                albedoTexture = Loader.createViaPath<Texture2D>("textures/playerUV.png")
            )
        }
        gameObject.addComponent<CircleCollider>().also {
            it.radius = .5f
        }
        gameObject.addComponent<Health>().also {
            it.hp = 10f
            it.maxHp = 10f
        }
        gameObject.addComponent<Player>()
        gameObject.addComponent<HpText>().also {
            it.logicalFontSize = 32F
            it.fontName = FontRegistry.ROBOTO_BOLD
            it.logicalPosition = Vector2f(8f, 13f)
        }
    }
}

fun makePauseMenu() {
    GameObject("PauseMenu").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Time.timeScale = 1f
                makePauseOpener()
                Scene.active.destroy(gameObject)
            }
        }

        // Resumebutton
        gameObject.addComponent(makeButton("RESUME",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) {
            Time.timeScale = 1f
            makePauseOpener()
            Scene.active.destroy(gameObject)
        })

        // Optionsbutton
        gameObject.addComponent(makeButton("OPTIONS",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f + 130f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) {
            Scene.active.destroy(gameObject)
            makePauseOptions()
        })

        // Quitbutton
        gameObject.addComponent(makeButton("TO MAIN MENU",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.2f + 260f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) { Time.timeScale = 1f; loadMenu() })
        Scene.active.spawn(gameObject)
    }
}

fun makePauseOptions() {
    GameObject("OptionsMenu").also { gameObject ->
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
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.35f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, 0.1f)
            }
        }

        // Volumeslider
        gameObject.addComponent<Slider>().also { slider ->
            slider.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.35f)
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

        // BackButton
        gameObject.addComponent(makeButton("BACK",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.6f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) { Scene.active.destroy(gameObject); makePauseMenu() })

        Scene.active.spawn(gameObject)
    }
}
