package jackTheFishman.engine.graphics

class ShaderViaPath(val path: String) : Shader(getCodeViaPath(path))