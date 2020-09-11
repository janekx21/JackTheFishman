package jackTheFishman.framework.audio

import java.nio.ShortBuffer

data class SampleFile(val data: ShortBuffer, val channelCount: Int, val sampleRate: Int)
