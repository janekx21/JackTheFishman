package jackTheFishman.math.extentions

import kotlin.random.Random

fun Random.range(start: Float, end: Float): Float {
    return nextFloat() * (end - start) + start
}
