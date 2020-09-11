package jackTheFishman.framework

import jackTheFishman.framework.math.Vector3fConst
import org.joml.Quaternionf
import org.joml.Quaternionfc
import org.joml.Vector3f
import org.joml.Vector3fc
import org.lwjgl.openal.AL10

class OpenAlListener(val audio: Audio) : Listener {
    override var position: Vector3fc = Vector3fConst.zero
        set(value) {
            AL10.alListener3f(AL10.AL_POSITION, value.x(), value.y(), value.z())
            field = value
        }

    override var velocity: Vector3fc = Vector3fConst.zero
        set(value) {
            AL10.alListener3f(AL10.AL_VELOCITY, value.x(), value.y(), value.z())
            field = value
        }

    override var gain: Float = 1f
        set(value) {
            AL10.alListenerf(AL10.AL_GAIN, value)
            field = value
        }

    override var rotation: Quaternionfc = Quaternionf()
        set(value) {
            val direction = Vector3f(Vector3fConst.forward) // default direction
            Quaternionf(rotation).transform(direction)
            AL10.alListenerfv(AL10.AL_ORIENTATION, floatArrayOf(direction.x(), direction.y(), direction.z(), 0f, 1f, 0f))
            field = value
        }
}
