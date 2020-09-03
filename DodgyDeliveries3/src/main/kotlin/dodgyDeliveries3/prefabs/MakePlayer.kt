package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.*
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun makePlayerWithBox(): Pair<GameObject, GameObject> {
    val player = GameObject("Player").also { gameObject ->
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
            it.hp = 6f
            it.maxHp = 6f
        }
        gameObject.addComponent<Audio>().also {
            it.sample = Loader.createViaPath("sounds/hit.ogg")
        }

        gameObject.addComponent<Player>()
    }
    val box = makeBox(player.transform)
    box.addComponent<Audio>().also {
        it.sample = Loader.createViaPath("sounds/moped.ogg")
        it.source.looping = true
        it.source.gain = 10f
        it.play()
    }
    return Pair(player, box)
}

fun makeMenuPlayerWithBox(): Pair<GameObject, GameObject> {
    val player = GameObject("Player").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.scale = Vector3fConst.one * .8f
            it.position = Vector3f(2f, -0.5f, -0.1f)
        }
        gameObject.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath("models/playerColoured.fbx")
            it.material = it.material.copy(
                albedoTexture = Loader.createViaPath<Texture2D>("textures/player/AlbedoMap.png"),
                specularTexture = Loader.createViaPath<Texture2D>("textures/player/SpecularMap.png"),
                normalTexture = Loader.createViaPath<Texture2D>("textures/player/NormalMap.png"),
                normalIntensity = .1f,
                specularRoughness = 10f,
                ambientColor = Vector3f(.3f, .3f, .3f)
            )
        }
        gameObject.addComponent<MenuPlayerAnimation>()
    }
    return Pair(player, makeBox(player.transform))
}

private fun makeBox(parent: Transform): GameObject {
    return GameObject("Player Box").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.scale = Vector3fConst.one * .23f
            it.parent = parent
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
    }
}
