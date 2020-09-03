package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.CurveWorld

fun makeWorldCurver(): GameObject {
    return GameObject("WorldCurver").also { gameObject ->
        gameObject.addComponent<CurveWorld>()
    }
}
