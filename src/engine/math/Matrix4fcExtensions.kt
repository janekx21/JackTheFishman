package engine.math

import org.joml.Matrix4f
import org.joml.Matrix4fc

operator fun Matrix4fc.times(other: Matrix4fc): Matrix4fc {
    return Matrix4f(this).mul(other)
}