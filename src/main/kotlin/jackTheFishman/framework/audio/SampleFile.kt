package jackTheFishman.framework.audio

import java.nio.ShortBuffer

data class SampleFile(val rawData: ShortBuffer, val channelCount: Int, val sampleRate: Int)
