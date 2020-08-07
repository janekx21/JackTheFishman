package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun loadDefaultScene() {
    Scene.active.allGameObjects.clear()

    val player = GameObject("Player").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, -5f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/monkey.fbx") // TODO: add player mesh
        }
        gameObject.addComponent<CircleCollider>()
        gameObject.addComponent<Player>()
        Scene.active.spawn(gameObject)
    }

    GameObject("Tunnel").also { gameObject ->
        gameObject.addComponent<Transform>()
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath(Mesh, "models/tunnel.fbx")
            material = material.copy(
                albedoTexture = Loader.createViaPath<Texture2D>("textures/pipes/AlbedoMap.jpg"),
                normalTexture = Loader.createViaPath<Texture2D>("textures/pipes/NormalMap.png"),
                specularTexture = Loader.createViaPath<Texture2D>("textures/pipes/SpecularMap.png")
            )
        }
        Scene.active.spawn(gameObject)
    }

    GameObject("Camera").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 2f, 4f)
        }
        gameObject.addComponent<AudioListener>()
        gameObject.addComponent<DebugCameraMovement>()
        gameObject.addComponent<LookAt>().apply {
            target = player.transform
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
            position = Vector3f(2f, 0f, 10f)
        }

        gameObject.addComponent<PointLight>().apply {
            color = Vector3f(ColorPalette.ORANGE) * 1f
        }
        Scene.active.spawn(gameObject)
    }
}
