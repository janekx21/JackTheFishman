package jackTheFishman.framework

import jackTheFishman.framework.util.IFinalized
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10.*
import org.lwjgl.openal.ALC11
import java.nio.IntBuffer


object OpenAlAudio : IFinalized {
    private val defaultDeviceName = ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER)
    private val device = alcOpenDevice(defaultDeviceName)

    init {
        check(device != 0L) { "Open AI Device not found" }
        IFinalized.push(this)
    }

    private val attributes: IntBuffer? = null
    private val context = alcCreateContext(device, attributes)

    init {
        setupContext()
        setupCapabilities()
        setupListener()
    }

    private fun setupContext() {
        alcMakeContextCurrent(context).also {
            check(it) { "failed to make context current" }
        }
        alcMakeContextCurrent(context)
    }

    private fun setupCapabilities() {
        val cap = ALC.createCapabilities(device)
        AL.createCapabilities(cap)
    }

    private fun setupListener() {
        alListener3f(AL_POSITION, 0f, 0f, 1f)
        alListener3f(AL_VELOCITY, 0f, 0f, 0f)
        alListenerfv(AL_ORIENTATION, floatArrayOf(0f, 0f, 1f, 0f, 1f, 0f))
    }

    override fun finalize() {
        alcDestroyContext(context)
        alcCloseDevice(device)
        ALC.destroy()
    }

    fun <T> checkedInvocation(f: () -> T): T {
        alGetError()
        val result = f()
        val alErr = alGetError()
        if (alErr != AL_NO_ERROR) {
            val alErrStr = when (alErr) {
                AL_INVALID_NAME -> "AL_INVALID_NAME"
                AL_INVALID_ENUM -> "AL_INVALID_ENUM"
                AL_INVALID_VALUE -> "AL_INVALID_VALUE"
                AL_INVALID_OPERATION -> "AL_INVALID_OPERATION"
                AL_OUT_OF_MEMORY -> "AL_OUT_OF_MEMORY"
                else -> "($alErr)"
            }
            throw IllegalStateException("OpenAL error ocurred: $alErrStr")
        }
        return result
    }
}
