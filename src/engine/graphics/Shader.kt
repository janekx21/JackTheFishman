package engine.graphics

import engine.util.IUsable
import engine.util.IntPointer
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL46.*
import java.io.File
import java.nio.file.Path

class Shader(pathVert: String, pathFrag: String) : IUsable {
    private val program = compileProgram(pathVert, pathFrag)
    private val uniformLocations = HashMap<String, Int>()
    private val textureUniforms = arrayListOf<Texture>()

    override fun use(callback: () -> Unit) {
        glUseProgram(program)
        Texture.bind(textureUniforms.toTypedArray()) {
            callback()
        }
        glUseProgram(0)
    }

    fun setUniform(name: String, value: Vector3f) {
        glProgramUniform3f(program, getUniformLocation(name), value.x, value.y, value.z)
    }

    fun setUniform(name: String, value: Matrix4f) {
        val buffer = FloatArray(16)
        value.get(buffer)
        glProgramUniformMatrix4fv(program, getUniformLocation(name), false, buffer)
    }

    fun setUniform(name: String, value: Vector4f) {
        glProgramUniform4f(program, getUniformLocation(name), value.x, value.y, value.z, value.w)
    }

    fun setUniform(name: String, value: Texture) {
        textureUniforms.add(value)
        glProgramUniform1i(program, getUniformLocation(name), textureUniforms.lastIndex)
    }

    private fun getUniformLocation(name: String): Int {
        return uniformLocations.getOrDefault(name, GL20.glGetUniformLocation(program, name))
    }

    fun setMatrix(world: Matrix4f, view: Matrix4f, projection: Matrix4f) {
        val mvp = Matrix4f(projection).mul(view).mul(world)
        setUniform(worldAttributeName, world)
        setUniform(viewAttributeName, view)
        setUniform(projectionAttributeName, projection)
        setUniform(mvpAttributeName, mvp)
    }

    companion object {
        private const val colorAttribute = "outColor"
        private const val versionString = "#version 150 core\n"

        private const val worldAttributeName = "World"
        private const val viewAttributeName = "View"
        private const val projectionAttributeName = "Projection"
        private const val mvpAttributeName = "MVP"

        private const val worldAttribute = "uniform mat4 $worldAttributeName;"
        private const val viewAttribute = "uniform mat4 $viewAttributeName;"
        private const val projectionAttribute = "uniform mat4 $projectionAttributeName;"
        private const val mvpAttribute = "uniform mat4 $mvpAttributeName;"

        private const val importKeyword = "#import"

        private fun preprocess(path: String): String {
            var code = ""
            val folder = Path.of(path).parent
            for(line in File(path).readText().lines()) {
                if(line.startsWith(importKeyword)) {
                    val file = line.substringAfterLast("$importKeyword ")
                    val addition = preprocess("$folder/$file")
                    code += "$addition\n"
                } else {
                    code += "$line\n"
                }
            }
            return code
        }

        private fun prefixShader(code: String): String {
            return "$versionString$worldAttribute$viewAttribute$projectionAttribute$mvpAttribute\n$code"
        }

        private fun compileShader(path: String, type: Int): Int {
            val code = prefixShader(preprocess(path))
            val shader = glCreateShader(type)
            glShaderSource(shader, code)
            glCompileShader(shader)
            val status = IntPointer()
            glGetShaderiv(shader, GL_COMPILE_STATUS, status.buffer)
            check(status.value != GL_FALSE) { glGetShaderInfoLog(shader) }
            return shader
        }

        fun compileProgram(vertPath: String, fragPath: String): Int {
            val vs = compileShader(vertPath, GL_VERTEX_SHADER)
            val fs = compileShader(fragPath, GL_FRAGMENT_SHADER)
            val program = glCreateProgram()
            glAttachShader(program, vs)
            glAttachShader(program, fs)
            glBindFragDataLocation(program, 0, colorAttribute)

            for (attribute in Vertex.namedIndex().withIndex()) {
                GL20.glBindAttribLocation(program, attribute.index, attribute.value)
            }

            glLinkProgram(program)
            return program
        }
    }
}