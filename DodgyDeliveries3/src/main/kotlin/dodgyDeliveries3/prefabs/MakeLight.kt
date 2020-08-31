package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.PointLight
import dodgyDeliveries3.components.Transform
import org.joml.Vector3fc

fun makeLight(position: Vector3fc, color: Vector3fc): GameObject {
    return GameObject("Light").also { gameObject ->
        gameObject.addComponent<Transform>().also {
            it.position = position
        }
        gameObject.addComponent<PointLight>().also {
            it.color = color
        }
    }
}
