package engine.graphics

import engine.util.IntPointer
import org.lwjgl.opengl.GL46.*
import java.io.File

class Shader(pathFrag: String, pathVert: String) {
    val program: Int
    private val colorAttribute = "outColor"

    init {
        val vs = compileShader(pathVert, GL_VERTEX_SHADER)
        val fs = compileShader(pathFrag, GL_FRAGMENT_SHADER)
        program = glCreateProgram()
        glAttachShader(program, vs)
        glAttachShader(program, fs)
        glBindFragDataLocation(program, 0, colorAttribute)
        // glBindAttribLocation()
        glLinkProgram(program)
    }
    private val mvpLocation = glGetUniformLocation(program, "mvp")
    private val pLocation = glGetUniformLocation(program, "projection")
    private val lightLocation = glGetUniformLocation(program, "light")
    private val textureLocation = glGetUniformLocation(program, "texture")
    private val normalMapLocation = glGetUniformLocation(program, "normalMap")

    fun bind() {
        glUseProgram(program)

        with(FloatArray(16)) {
            glGetFloatv(GL_MODELVIEW_MATRIX, this)
            glProgramUniformMatrix4fv(program, mvpLocation, false, this)
        }

        with(FloatArray(16)) {
            glGetFloatv(GL_PROJECTION_MATRIX, this)
            glProgramUniformMatrix4fv(program, pLocation, false, this)
        }

        glProgramUniform3f(program, lightLocation, .3f, .3f, .3f)
        glProgramUniform1i(program, textureLocation, 0)
        glProgramUniform1i(program, normalMapLocation, 1)
    }

    fun unbind() {
        glUseProgram(0)
    }

    private fun compileShader(path: String, type: Int): Int {
        val code = File(path).readText()
        val shader = glCreateShader(type)
        glShaderSource(shader, code)
        glCompileShader(shader)
        val status = IntPointer()
        glGetShaderiv(shader, GL_COMPILE_STATUS, status.buffer)
        check(status.value != GL_FALSE) { glGetShaderInfoLog(shader) }
        return shader
    }

    public fun compileProgram() {

    }
}