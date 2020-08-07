package dodgyDeliveries3

import dodgyDeliveries3.components.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.math.Vector2fCopy
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun loadDefaultScene() {
    Scene.active.allGameObjects.clear()

    GameObject("Player").also { gameObject ->
        gameObject.addComponent<Transform>()
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
        }
        gameObject.addComponent<CircleCollider>().apply {
            velocity = Vector2fCopy.left * 1f
        }
        // TODO: add player controller when ready
        Scene.active.spawn(gameObject)
    }

    GameObject("StandardEnemy").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, -20f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/monkey.fbx")
        }
        gameObject.addComponent<ProjectileSpawner>().apply {
            projectileMesh = Loader.createViaPath("models/cube.fbx")
        }
        Scene.active.spawn(gameObject)
    }

    GameObject("Object").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(-10f, 0f, .5f)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
        }
        gameObject.addComponent<BoxCollider>().apply {
            velocity = Vector2fCopy.right * 1f
        }
        // TODO: add player controller when ready
        Scene.active.spawn(gameObject)
    }


    GameObject("Tunnel").also { gameObject ->
        gameObject.addComponent<Transform>()
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath(Mesh, "models/tunnel.fbx")
        }
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
            position = Vector3f(2f, 0f, 10f)
        }

        gameObject.addComponent<PointLight>().apply {
            color = Vector3f(ColorPalette.ORANGE) * 1f
        }
        Scene.active.spawn(gameObject)
    }
}
