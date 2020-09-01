package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.*
import jackTheFishman.engine.Audio
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

    Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)

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

    GameObject("Grid").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.scale = it.scale * 1.4f
            it.position = Vector3f(0f, -0.5f, -3.5f)
        }
        gameObject.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath("models/grid.fbx")
            it.material = it.material.copy(
                albedoColor = ColorPalette.ORANGE,
                emissionColor = Vector3f(102f, 51f, 0f) * (1f / 255f)
            )
        }

        Scene.active.spawn(gameObject)
    }

    GameObject("PlayerBox").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.scale = Vector3fConst.one * .23f
            it.parent = player.transform
            it.position = Vector3f(0f, .73f, -.63f)
        }
        gameObject.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath("models/box.fbx")
            val texture: Texture2D = Loader.createViaPath("textures/boxAlbedo.jpg")
            texture.makeLinear()
            it.material = it.material.copy(
                albedoTexture = texture
            )
        }
        Scene.active.spawn(gameObject)
    }

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

    GameObject("HealthIndicator").also { gameObject ->
        gameObject.addComponent<HealthIndicator>()

        Scene.active.spawn(gameObject)
    }
}

fun makePauseOpener() {
    GameObject("Pause").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Input.Mouse.setMode(Input.Mouse.CursorMode.NORMAL)
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
                albedoTexture = Loader.createViaPath<Texture2D>("textures/player/AlbedoMap.png"),
                specularTexture = Loader.createViaPath<Texture2D>("textures/player/SpecularMap.png"),
                normalTexture = Loader.createViaPath<Texture2D>("textures/player/NormalMap.png"),
                normalIntensity = .1f,
                specularRoughness = 10f
            )
        }
        gameObject.addComponent<CircleCollider>().also {
            it.radius = .5f
        }
        gameObject.addComponent<Health>().also {
            it.hp = 10f
            it.maxHp = 10f
        }
        gameObject.addComponent<dodgyDeliveries3.components.Audio>().also {
            it.sample = Loader.createViaPath("sounds/noise.ogg")
            it.play()
            it.source.gain = 0f
            it.source.looping = true
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
                Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)
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
            Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)
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
            }) {
            Time.timeScale = 1f
            loadMenu()
        })
        Scene.active.spawn(gameObject)
    }
}

fun makePauseOptions() {
    GameObject("OptionsMenu").also { gameObject ->
        gameObject.addComponent<EscapeHandler>().also {
            it.action = {
                Scene.active.destroy(gameObject)
                makePauseMenu()
            }
        }

        gameObject.addComponent<Text>().also { text ->
            text.fontName = "Sugarpunch"
            text.leguiComponent.textState.horizontalAlign = HorizontalAlign.CENTER
            text.leguiComponent.textState.textColor = Vector4f(ColorPalette.WHITE, 1f)
            text.logicalFontSize = 25f
            text.text = "VOLUME: " + "%.2f".format(Audio.Listener.gain)
            text.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.25f, Window.logicalSize.y() * 0.25f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.5f, 0.1f)
            }
        }

        // Volumeslider
        gameObject.addComponent<Slider>().also { slider ->
            slider.onLayout = {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.25f)
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

        gameObject.addComponent(makeButton("FULLSCREEN TOGGLE",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.45f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) { Window.fullscreen = !Window.fullscreen })

        // BackButton
        gameObject.addComponent(makeButton("BACK",
            {
                it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.35f, Window.logicalSize.y() * 0.45f + 130f)
                it.logicalSize = Vector2f(Window.logicalSize.x() * 0.3f, 100f)
            }) {
            Scene.active.destroy(gameObject)
            makePauseMenu()
        })

        Scene.active.spawn(gameObject)
    }
}
