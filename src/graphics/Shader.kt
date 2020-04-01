package graphics

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL46.*
import java.io.File

class Shader(pathFrag: String, pathVert: String) {
    val program: Int

    init {
        val vs = compileShader(pathVert, GL_VERTEX_SHADER)
        val fs = compileShader(pathFrag, GL_FRAGMENT_SHADER)
        program = glCreateProgram()
        glAttachShader(program, vs)
        glAttachShader(program, fs)
        glBindFragDataLocation(program, 0, "color")
        glLinkProgram(program)
    }

    val parameters = getActiveParameters()

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
        val status = intArrayOf(0)
        glGetShaderiv(shader, GL_COMPILE_STATUS, status)
        check(status[0] != GL_FALSE) { glGetShaderInfoLog(shader) }
        return shader
    }

    private fun getActiveParameters(): Array<Parameter> {
        val countPointer = intArrayOf(0)
        glGetProgramiv(program, GL_ACTIVE_ATTRIBUTES, countPointer)
        val count = countPointer[0]
        return Array(count) {
            val sizePointer = BufferUtils.createIntBuffer(1)
            val typePointer = BufferUtils.createIntBuffer(1)
            val name = glGetActiveAttrib(program, it, sizePointer, typePointer)
            Parameter(name, typePointer[0], sizePointer[0])
        }
    }

    private fun getActiveUniforms(): Array<Uniform> {
        val countPointer = intArrayOf(0)
        glGetProgramiv(program, GL_ACTIVE_UNIFORMS, countPointer)
        val count = countPointer[0]
        return Array(count) {
            val sizePointer = BufferUtils.createIntBuffer(1)
            val typePointer = BufferUtils.createIntBuffer(1)
            val name = glGetActiveUniform(program, it, sizePointer, typePointer)
            Uniform(name, typePointer[0], sizePointer[0])
        }
    }

    override fun toString(): String {
        val attributes = getActiveParameters().joinToString(", ")
        val uniforms = getActiveUniforms().joinToString(", ")
        return "<Shader with [$attributes] attributes and [$uniforms] uniforms>"
    }

    data class Parameter(val name: String, val type: Type, val size: Int) {
        constructor(name: String, type: Int, size: Int) : this(name, Type(type), size)
    }

    data class Uniform(val name: String, val type: Type, val size: Int) {
        constructor(name: String, type: Int, size: Int) : this(name, Type(type), size)
    }

    data class Type(val index: Int) {
        init {
            check(possible.contains(index)) { "type with index $index not found" }
        }

        companion object {
            val possible = arrayOf(
                GL_FLOAT,
                GL_FLOAT_VEC2,
                GL_FLOAT_VEC3,
                GL_FLOAT_VEC4,
                GL_FLOAT_MAT2,
                GL_FLOAT_MAT3,
                GL_FLOAT_MAT4,
                GL_FLOAT_MAT2x3,
                GL_FLOAT_MAT2x4,
                GL_FLOAT_MAT3x2,
                GL_FLOAT_MAT3x4,
                GL_FLOAT_MAT4x2,
                GL_FLOAT_MAT4x3,
                GL_INT,
                GL_INT_VEC2,
                GL_INT_VEC3,
                GL_INT_VEC4,
                GL_UNSIGNED_INT,
                GL_UNSIGNED_INT_VEC2,
                GL_UNSIGNED_INT_VEC3,
                GL_UNSIGNED_INT_VEC4,
                GL_DOUBLE,
                GL_DOUBLE_VEC2,
                GL_DOUBLE_VEC3,
                GL_DOUBLE_VEC4,
                GL_DOUBLE_MAT2,
                GL_DOUBLE_MAT3,
                GL_DOUBLE_MAT4,
                GL_DOUBLE_MAT2x3,
                GL_DOUBLE_MAT2x4,
                GL_DOUBLE_MAT3x2,
                GL_DOUBLE_MAT3x4,
                GL_DOUBLE_MAT4x2,
                GL_DOUBLE_MAT4x3
            )
            val types = mapOf(Pair(GL_FLOAT, Float), Pair(GL_INT, Int))
        }
    }
}