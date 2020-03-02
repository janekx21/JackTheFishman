package util

object Path {
    fun combine(vararg arg: String): String {
        return arg.joinToString(separator = "/")
    }
}