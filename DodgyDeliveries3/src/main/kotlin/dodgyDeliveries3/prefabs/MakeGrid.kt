package dodgyDeliveries3.prefabs

import dodgyDeliveries3.DodgyDeliveries3
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.ModelRenderer
import dodgyDeliveries3.components.Transform
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun makeGridMesh() = Loader.createViaPath<Mesh>("models/grid.fbx")

fun makeGrid(): GameObject {
    return GameObject("Grid").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.position = Vector3f(0f, -0.47f, -3.5f)
            it.scale = Vector3f(it.scale.x() * 1.12f, it.scale.y() * 1.4f, it.scale.z() * 1.4f)
        }
        gameObject.addComponent<ModelRenderer>().also {
            it.mesh = if (DodgyDeliveries3.config.showGrid) {
                makeGridMesh()
            } else {
                null
            }

            it.material = it.material.copy(
                albedoColor = ColorPalette.ORANGE,
                emissionColor = Vector3f(66f, 95f, 106f) * (1f / 255f) * 0.6f
            )
        }
    }
}
