package engine.graphics

import engine.util.ICreateViaPath
import engine.util.IUsable
import engine.util.IntPointer
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL46.*
import java.io.File
import java.nio.file.Path

class Shader(vertexCode: String, fragmentCode: String) : IUsable {
    private val program = compileProgram(vertexCode, fragmentCode)
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

    companion object : ICreateViaPath<Shader> {
        private const val colorAttribute = "outColor"
        private const val versionString = "#version 150 core\n"

        private const val worldAttributeName = "World"
        private const val viewAttributeName = "View"
        private const val projectionAttributeName = "Projection"
        private const val mvpAttributeName = "MVP"

        private const val importKeyword = "#import"
        private const val vertexKeyword = "#vertex"
        private const val fragmentKeyword = "#fragment"
        private const val noneKeyword = "#none"

        private fun preprocess(path: String): String {
            var code = ""
            val folder = Path.of(path).parent
            for (line in File(path).readLines()) {
                code += if (line.startsWith(importKeyword)) {
                    val file = line.substringAfterLast("$importKeyword ")
                    val addition = preprocess("$folder/$file")
                    "$addition\n"
                } else {
                    "$line\n"
                }
            }
            return code
        }

        private fun prefixShader(code: String): String {
            return "$versionString\n$code"
        }

        private fun compileShader(code: String, type: Int): Int {
            val shader = glCreateShader(type)
            glShaderSource(shader, code)
            glCompileShader(shader)
            val status = IntPointer()
            glGetShaderiv(shader, GL_COMPILE_STATUS, status.buffer)
            check(status.value != GL_FALSE) { glGetShaderInfoLog(shader) }
            return shader
        }

        private fun compileProgram(vertexCode: String, fragmentCode: String): Int {
            val vs = compileShader(vertexCode, GL_VERTEX_SHADER)
            val fs = compileShader(fragmentCode, GL_FRAGMENT_SHADER)
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

        enum class Mode {
            None, Vertex, Fragment
        }

        override fun createViaPath(path: String): Shader {
            var vertex = ""
            var fragment = ""
            var mode = Mode.None
            val code = preprocess(path)

            code.lines().forEach {
                var ignore = true
                when (it) {
                    vertexKeyword -> {
                        mode = Mode.Vertex
                    }
                    fragmentKeyword -> {
                        mode = Mode.Fragment
                    }
                    noneKeyword -> {
                        mode = Mode.None
                    }
                    else -> {
                        ignore = false
                    }
                }

                if (!ignore) {
                    when (mode) {
                        Mode.Vertex -> vertex += "$it\n"
                        Mode.Fragment -> fragment += "$it\n"
                        else -> {
                            vertex += "$it\n"
                            fragment += "$it\n"
                        }
                    }
                }
            }

            return Shader(prefixShader(vertex), prefixShader(fragment))
        }
    }
}