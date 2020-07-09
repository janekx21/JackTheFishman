package engine.math

import engine.util.IJsonUnserializable

object FloatExt : IJsonUnserializable<Float> {
    override fun fromJson(json: Any?): Float {
        return (json as Double).toFloat()
    }
}