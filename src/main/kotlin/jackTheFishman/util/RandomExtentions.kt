package jackTheFishman.util

import kotlin.random.Random

fun Random.range(start: Float, end: Float): Float {
    return nextFloat() * (end - start) + start
}
