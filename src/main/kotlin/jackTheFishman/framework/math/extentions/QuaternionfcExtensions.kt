package jackTheFishman.framework.math.extentions

import org.joml.Quaternionf
import org.joml.Quaternionfc

operator fun Quaternionfc.times(other: Quaternionfc): Quaternionf {
    return Quaternionf(this).mul(other)
}
