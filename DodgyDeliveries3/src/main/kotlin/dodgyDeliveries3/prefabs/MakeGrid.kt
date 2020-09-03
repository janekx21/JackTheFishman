package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.ModelRenderer
import dodgyDeliveries3.components.Transform
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun makeGrid(): GameObject {
    return GameObject("Grid").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.position = Vector3f(0f, -0.47f, -3.5f)
            it.scale = it.scale * 1.4f
        }
        gameObject.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath("models/grid.fbx")
            it.material = it.material.copy(
                albedoColor = ColorPalette.ORANGE,
                emissionColor = Vector3f(66f, 95f, 106f) * (1f / 255f) * 0.6f
            )
        }
    }
}
