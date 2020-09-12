package jackTheFishman.audio.openAl

import jackTheFishman.audio.PlayState
import jackTheFishman.audio.SampleFile
import jackTheFishman.math.constants.vector3f.forward
import jackTheFishman.math.constants.vector3f.up
import jackTheFishman.util.Finalized
import jackTheFishman.util.pointer.IntPointer
import org.joml.Quaternionf
import org.joml.Quaternionfc
import org.joml.Vector3f
import org.joml.Vector3fc
import org.lwjgl.openal.AL.createCapabilities
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.AL11.AL_SEC_OFFSET
import org.lwjgl.openal.ALC.createCapabilities
import org.lwjgl.openal.ALC.destroy
import org.lwjgl.openal.ALC10.*
import org.lwjgl.openal.ALC11

object OpenAl : Finalized {
    private val defaultDeviceName = ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER)
    private val device = alcOpenDevice(defaultDeviceName)

    private const val BITS_PER_BYTE = 8

    init {
        check(device != 0L) { "Open AI Device not found" }
        checkNotNull(defaultDeviceName) { "Default device not found" }
        Finalized.push(this)
    }

    private val context = alcCreateContext(device, intArrayOf())

    init {
        setupContext()
        setupCapabilities()
        setupListener()
    }

    private fun setupContext() {
        alcMakeContextCurrent(context).also { worked ->
            check(worked) { "Failed to make context current" }
        }
    }

    private fun setupCapabilities() {
        val capabilities = createCapabilities(device)
        createCapabilities(capabilities)
    }

    private fun setupListener() {
        alListener3f(AL_POSITION, 0f, 0f, 0f)
        alListener3f(AL_VELOCITY, 0f, 0f, 0f)
        alListenerfv(AL_ORIENTATION, floatArrayOf(0f, 0f, 1f, 0f, 1f, 0f))
    }

    fun genSource(): Int = alGenSources()

    fun setSampleOfSource(sourcePointer: OpenAlSourcePointer, samplePointer: OpenAlSamplePointer) {
        alSourcei(sourcePointer, AL_BUFFER, samplePointer)
    }

    fun setSourcePitch(pointer: OpenAlSourcePointer, value: Float) {
        alSourcef(pointer, AL_PITCH, value)
    }

    fun setSourceGain(pointer: OpenAlSourcePointer, value: Float) {
        alSourcef(pointer, AL_GAIN, value)
    }

    fun setSourcePosition(pointer: OpenAlSourcePointer, value: Vector3fc) {
        alSource3f(pointer, AL_POSITION, value.x(), value.y(), value.z())
    }

    fun setSourceVelocity(pointer: OpenAlSourcePointer, value: Vector3fc) {
        alSource3f(pointer, AL_VELOCITY, value.x(), value.y(), value.z())
    }

    fun setSourceLooping(pointer: OpenAlSourcePointer, value: Boolean) {
        alSourcei(pointer, AL_LOOPING, if (value) AL_TRUE else AL_FALSE)
    }

    fun sourcePlay(pointer: OpenAlSourcePointer) {
        alSourcePlay(pointer)
    }

    fun sourcePause(pointer: OpenAlSourcePointer) {
        alSourcePause(pointer)
    }

    fun sourceStop(pointer: OpenAlSourcePointer) {
        alSourceStop(pointer)
    }

    fun getSourceOffset(pointer: OpenAlSourcePointer): Float {
        return alGetSourcef(pointer, AL_SEC_OFFSET)
    }

    fun setSourceOffset(pointer: OpenAlSourcePointer, value: Float) {
        alSourcef(pointer, AL_SEC_OFFSET, value)
    }

    fun getSourceState(pointer: OpenAlSourcePointer): PlayState {
        return when (alGetSourcei(pointer, AL_SOURCE_STATE)) {
            AL_INITIAL -> PlayState.INITIAL
            AL_PLAYING -> PlayState.PLAYING
            AL_STOPPED -> PlayState.STOPPED
            AL_PAUSED -> PlayState.PAUSED
            else -> throw Exception("Unknown state")
        }
    }

    fun deleteSource(pointer: OpenAlSourcePointer) {
        alDeleteSources(pointer)
    }

    fun genSample(): OpenAlSamplePointer {
        return alGenBuffers()
    }

    fun setSampleData(pointer: OpenAlSamplePointer, file: SampleFile) {
        val format = when (file.channelCount) {
            1 -> OpenAlSampleFormat.MONO_16
            2 -> OpenAlSampleFormat.STEREO_16
            else -> throw Exception("Channel count of ${file.channelCount} is not supported")
        }

        val alFormat = when(format) {
            OpenAlSampleFormat.MONO_16 -> AL_FORMAT_MONO16
            OpenAlSampleFormat.STEREO_16 -> AL_FORMAT_STEREO16
        }
        alBufferData(pointer, alFormat, file.rawData, file.sampleRate)
    }

    fun getSampleDurationInSeconds(pointer: OpenAlSamplePointer): Float {
        val temp = IntPointer()

        alGetBufferi(pointer, AL_SIZE, temp.array)
        val bufferSize = temp.value

        alGetBufferi(pointer, AL_FREQUENCY, temp.array)
        val frequency = temp.value

        alGetBufferi(pointer, AL_CHANNELS, temp.array)
        val channels = temp.value

        alGetBufferi(pointer, AL_BITS, temp.array)
        val bitsPerSample = temp.value

        return bufferSize * BITS_PER_BYTE.toFloat() / (frequency * channels * bitsPerSample)
    }

    fun deleteSample(pointer: OpenAlSamplePointer) {
        alDeleteBuffers(pointer)
    }

    fun setListenerPosition(value: Vector3fc) {
        alListener3f(AL_POSITION, value.x(), value.y(), value.z())
    }

    fun setListenerVelocity(value: Vector3fc) {
        alListener3f(AL_VELOCITY, value.x(), value.y(), value.z())
    }

    fun setListenerRotation(value: Quaternionfc) {
        val rotatedForward = Vector3f(forward)
        val rotatedUp = Vector3f(up)
        Quaternionf(value).transform(rotatedForward)
        Quaternionf(value).transform(rotatedUp)
        alListenerfv(
            AL_ORIENTATION, floatArrayOf(
                rotatedForward.x(), rotatedForward.y(), rotatedForward.z(),
                rotatedUp.x(), rotatedUp.y(), rotatedUp.z()
            )
        )
    }

    fun setMasterGain(value: Float) {
        alListenerf(AL_GAIN, value)
    }

    override fun finalize() {
        alcDestroyContext(context)
        alcCloseDevice(device)
        destroy()
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
