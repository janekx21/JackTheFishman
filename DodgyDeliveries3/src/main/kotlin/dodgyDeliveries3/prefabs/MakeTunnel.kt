package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.ModelRenderer
import dodgyDeliveries3.components.Transform
import dodgyDeliveries3.components.Tunnel
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Texture2D
import org.joml.Vector3f

fun makeForwardTunnel(z: Float): GameObject {
    val tunnel = makeTunnel(z)
    tunnel.addComponent<Tunnel>()
    return tunnel
}

fun makeBackwardTunnel(z: Float): GameObject {
    val tunnel = makeTunnel(z)
    tunnel.addComponent<Tunnel>().also {
        it.speed = 2f
        it.forward = false
    }
    return tunnel
}

fun makeTunnel(z: Float): GameObject {
    return GameObject("Tunnel").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 0f, z)
        }
        gameObject.addComponent<ModelRenderer>().apply {
            mesh = Loader.createViaPath("models/tunnel.fbx")
            material = material.copy(
                albedoTexture = Loader.createViaPath<Texture2D>("textures/pipes/AlbedoMap.jpg"),
                normalTexture = Loader.createViaPath<Texture2D>("textures/pipes/NormalMap.png"),
                specularTexture = Loader.createViaPath<Texture2D>("textures/pipes/SpecularMap.png")
            )
        }
    }
}
