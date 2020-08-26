package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.graphics.Material
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Texture
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.times
import org.joml.Vector2f
import org.joml.Vector3f
import org.liquidengine.legui.style.font.FontRegistry
import java.io.File

fun loadDefaultScene() {
    Scene.active.allGameObjects.clear()

    val player = makePlayer()
    Scene.active.spawn(player)

    GameObject("StandardEnemy").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, -20f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/standardenemy.fbx") // TODO: add player mesh
        }
        gameObject.addComponent<ProjectileSpawner>()
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
        Scene.active.spawn(gameObject)
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 2f, 4f)
        }
        gameObject.addComponent<AudioListener>()
        gameObject.addComponent<DebugCameraMovement>()
        gameObject.addComponent<LookAt>().apply {
            target = player.transform
        }
        Camera.main = gameObject.addComponent()
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
            color = Vector3f(ColorPalette.ORANGE) * 1f
        }
        Scene.active.spawn(gameObject)
    }
}

fun makePlayer(): GameObject {
    return GameObject("Player").also { gameObject ->
        gameObject.addComponent<Transform>()
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/monkey.fbx") // TODO: add player mesh
        }
        gameObject.addComponent<CircleCollider>().apply {
        }
        gameObject.addComponent<Health>().also {
            it.hp = 100f
            it.maxHp = 100f
        }
        gameObject.addComponent<HpText>().also {
            it.scaledFontSize = 32F
            it.fontName = FontRegistry.ROBOTO_BOLD
            it.scaledPosition = Vector2f(8f, 8f + 5f /* zusätzlich 5f vertikal, da das Text-Rendering irgendwie nicht präzise ist */)
        }
        gameObject.addComponent<Button>().also {
            it.scaledFontSize = 32F
            it.fontName = FontRegistry.ROBOTO_BOLD
            it.text = "Press Me"
            it.scaledPosition = Vector2f(100f, 100f)
            it.scaledSize = Vector2f(100f, 100f)
        }
        gameObject.addComponent<ImageComponent>().also {
            it.path = Loader.resourceFileViaPath("logos/logo.png").path
            it.scaledSize = Vector2f(200f, 200f)
        }
    }
}
