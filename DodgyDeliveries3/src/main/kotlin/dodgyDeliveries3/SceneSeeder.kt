package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.graphics.Material
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Texture
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector2fCopy
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun loadDefaultScene() {
    Scene.active.allGameObjects.clear()

    Scene.active.spawn(makePlayer())

    GameObject("StandardEnemy").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, -20f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/standardenemy.fbx") // TODO: add player mesh
        }
        gameObject.addComponent<ProjectileSpawner>()
        // TODO: add player controller when ready
        Scene.active.spawn(gameObject)
    }

    GameObject("Tunnel1").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, -50f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/tunnel.fbx")
            material = material.copy(albedoTexture = Loader.createViaPath<Texture2D>("textures/pipes/AlbedoMap.jpg"), normalTexture = Loader.createViaPath<Texture2D>("textures/pipes/NormalMap.png"))
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
            material = material.copy(albedoTexture = Loader.createViaPath<Texture2D>("textures/pipes/AlbedoMap.jpg"), normalTexture = Loader.createViaPath<Texture2D>("textures/pipes/NormalMap.png"))
        }
        gameObject.addComponent<Tunnel>()
        Scene.active.spawn(gameObject)
    }

    GameObject("Camera").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, .5f)
        }
        gameObject.addComponent<AudioListener>()
        gameObject.addComponent<DebugCameraMovement>()
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
        // TODO: add player controller when ready
    }
}
