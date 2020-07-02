package engine.util

interface IJsonUnserializable<T> {
    fun fromJson(json: Any?): T
}