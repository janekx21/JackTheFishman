package jackTheFishman.engine

import jackTheFishman.engine.math.Vector3fCopy
import jackTheFishman.engine.util.IFinalized
import org.joml.Quaternionf
import org.joml.Vector3f
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10.*
import org.lwjgl.openal.ALC11
import java.nio.IntBuffer


object Audio : IFinalized {
    private val defaultDeviceName = ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER)
    private val device = alcOpenDevice(defaultDeviceName)

    init {
        check(device != 0L) { "Open AI Device not found" }
        IFinalized.push(this)
    }

    private val attributes: IntBuffer? = null
    private val context = alcCreateContext(device, attributes)

    init {
        alcMakeContextCurrent(context).also {
            check(it) { "failed to make context current" }
        }
        alcMakeContextCurrent(context)

        val cap = ALC.createCapabilities(device)
        AL.createCapabilities(cap)

        alListener3f(AL_POSITION, 0f, 0f, 1f)
        alListener3f(AL_VELOCITY, 0f, 0f, 0f)
        alListenerfv(AL_ORIENTATION, floatArrayOf(0f, 0f, 1f, 0f, 1f, 0f))
    }

    override fun finalize() {
        alcDestroyContext(context)
        alcCloseDevice(device)
        ALC.destroy()
    }

    object Listener {
        var position: Vector3f = Vector3fCopy.zero
            set(value) {
                alListener3f(AL_POSITION, value.x, value.y, value.z)
                field = value
            }

        var velocity: Vector3f = Vector3fCopy.zero
            set(value) {
                alListener3f(AL_VELOCITY, value.x, value.y, value.z)
                field = value
            }

        // TODO this is buggy
        var rotation: Quaternionf = Quaternionf()
            set(value) {
                val direction = Vector3fCopy.forward // default direction
                rotation.transformInverse(direction)
                alListenerfv(AL_ORIENTATION, floatArrayOf(direction.x, direction.y, direction.z, 0f, 1f, 0f))
                field = value
            }

    }
}