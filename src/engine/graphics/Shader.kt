package engine.graphics

import engine.util.IntPointer
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL46.*
import java.io.File

class Shader(pathVert: String, pathFrag: String) {
    val program = compileProgram(pathVert, pathFrag)
    private val uniformLocations = HashMap<String, Int>()

    fun use(callback: () -> Unit) {
        glUseProgram(program)
        callback()
        glUseProgram(0)
    }

    fun setUniform(name: String, value: Vector3f) {
        glProgramUniform3f(program, getUniformLocation(name), value.x, value.y, value.z);
    }

    fun setUniform(name: String, value: Matrix4f) {
        val buffer = FloatArray(16)
        value.get(buffer)
        glProgramUniformMatrix4fv(program, getUniformLocation(name), false, buffer)
    }

    private fun getUniformLocation(name: String): Int {
        return uniformLocations.getOrDefault(name, GL20.glGetUniformLocation(program, name))
    }

    public fun setMatrix(world: Matrix4f, view: Matrix4f, projection: Matrix4f) {
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

        private fun compileShader(path: String, type: Int): Int {
            val code = prefixShader(File(path).readText())
            val shader = glCreateShader(type)
            glShaderSource(shader, code)
            glCompileShader(shader)
            val status = IntPointer()
            glGetShaderiv(shader, GL_COMPILE_STATUS, status.buffer)
            check(status.value != GL_FALSE) { glGetShaderInfoLog(shader) }
            return shader
        }

        private fun prefixShader(code: String): String {
            return "$versionString$worldAttribute$viewAttribute$projectionAttribute$mvpAttribute\n$code"
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