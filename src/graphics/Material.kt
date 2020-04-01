package graphics

data class Parameter(val name: String, val value: String) {
}

class Material(val shader: Shader, val parameters: List<Parameter>) {
}