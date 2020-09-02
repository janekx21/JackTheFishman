package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.*
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun makeCamera(following: Transform): GameObject {
    return GameObject("Camera").also { gameObject ->
        gameObject.addComponent<Transform>().apply {
            position = Vector3f(0f, 2f, 2f)
        }
        gameObject.addComponent<AudioListener>()
        gameObject.addComponent<DebugCameraMovement>()
        gameObject.addComponent<LookAt>().apply {
            target = following
            offset = Vector3fConst.forward * 5f
        }
        Camera.main = gameObject.addComponent()
    }
}
