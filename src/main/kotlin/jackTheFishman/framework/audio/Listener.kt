package jackTheFishman.framework.audio

import org.joml.Quaternionfc
import org.joml.Vector3fc

interface Listener {
    var position: Vector3fc
    var velocity: Vector3fc
    var rotation: Quaternionfc
    var gain: Float
}
