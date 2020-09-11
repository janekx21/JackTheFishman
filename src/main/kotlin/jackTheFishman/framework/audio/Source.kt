package jackTheFishman.framework.audio

import org.joml.Vector3fc

/**
 * Represents a audio source like a noise or a speaker
 */
interface Source : Playable {
    var sample: OpenAlSample?
    val state: PlayState
    var looping: Boolean
    var gain: Float
    var pitch: Float
    var position: Vector3fc
    var velocity: Vector3fc
}
