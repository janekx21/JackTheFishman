package jackTheFishman.audio.openAl

import jackTheFishman.audio.Listener
import jackTheFishman.math.constants.vector3f.zero
import org.joml.Quaternionf
import org.joml.Quaternionfc
import org.joml.Vector3fc

class OpenAlListener : Listener {
    override var position: Vector3fc = zero
        set(value) {
            OpenAl.setListenerPosition(value)
            field = value
        }

    override var velocity: Vector3fc = zero
        set(value) {
            OpenAl.setListenerVelocity(value)
            field = value
        }

    override var gain: Float = 1f
        set(value) {
            OpenAl.setMasterGain(value)
            field = value
        }

    override var rotation: Quaternionfc = Quaternionf()
        set(value) {
            OpenAl.setListenerRotation(value)
        }
}
