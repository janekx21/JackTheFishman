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
        constructor(name: String, type: Int, size: Int) : this(name, Type.parse(type), size)

        override fun toString(): String {
            if (size > 1) {
                return "<Parameter $name as $type[$size]>"
            }
            return "<Parameter $name as $type>"
        }
    }

    open class Uniform(private val name: String, private val type: Type, private val size: Int) {
        constructor(name: String, type: Int, size: Int) : this(name, Type.parse(type), size)

        override fun toString(): String {
            if (size > 1) {
                return "<Uniform $name as $type[$size]>"
            }
            return "<Uniform $name as $type>"
        }

        class IntUniform(name: String) : Uniform(name, GL_INT, 1)
        class FloatUniform(name: String) : Uniform(name, GL_FLOAT, 1)

        companion object {
            // fun typeToUniform(name: String)
        }
    }

    open class Type(private val index: Int) {
        init {
            check(possible.contains(index)) { "type with index $index not found" }
        }

        override fun toString(): String {
            return "${glslTypes[index]}"
        }

        class IntType : Type(GL_INT)
        class FloatType : Type(GL_FLOAT)
        class Float2Type : Type(GL_FLOAT_VEC2)

        companion object {
            fun parse(index: Int): Type {
                return when (index) {
                    GL_FLOAT -> FloatType()
                    GL_INT -> IntType()
                    GL_FLOAT_VEC2 -> Float2Type()
                    else -> Type(index)
                }
            }

            val possible
                get() = glslTypes.keys

            val glslTypes = mapOf(
                Pair(GL_FLOAT, "float"),
                Pair(GL_FLOAT_VEC2, "vec2"),
                Pair(GL_FLOAT_VEC3, "vec3"),
                Pair(GL_FLOAT_VEC4, "vec4"),
                Pair(GL_DOUBLE, "double"),
                Pair(GL_DOUBLE_VEC2, "dvec2"),
                Pair(GL_DOUBLE_VEC3, "dvec3"),
                Pair(GL_DOUBLE_VEC4, "dvec4"),
                Pair(GL_INT, "int"),
                Pair(GL_INT_VEC2, "ivec2"),
                Pair(GL_INT_VEC3, "ivec3"),
                Pair(GL_INT_VEC4, "ivec4"),
                Pair(GL_UNSIGNED_INT, "unsigned int"),
                Pair(GL_UNSIGNED_INT_VEC2, "uvec2"),
                Pair(GL_UNSIGNED_INT_VEC3, "uvec3"),
                Pair(GL_UNSIGNED_INT_VEC4, "uvec4"),
                Pair(GL_BOOL, "bool"),
                Pair(GL_BOOL_VEC2, "bvec2"),
                Pair(GL_BOOL_VEC3, "bvec3"),
                Pair(GL_BOOL_VEC4, "bvec4"),
                Pair(GL_FLOAT_MAT2, "mat2"),
                Pair(GL_FLOAT_MAT3, "mat3"),
                Pair(GL_FLOAT_MAT4, "mat4"),
                Pair(GL_FLOAT_MAT2x3, "mat2x3"),
                Pair(GL_FLOAT_MAT2x4, "mat2x4"),
                Pair(GL_FLOAT_MAT3x2, "mat3x2"),
                Pair(GL_FLOAT_MAT3x4, "mat3x4"),
                Pair(GL_FLOAT_MAT4x2, "mat4x2"),
                Pair(GL_FLOAT_MAT4x3, "mat4x3"),
                Pair(GL_DOUBLE_MAT2, "dmat2"),
                Pair(GL_DOUBLE_MAT3, "dmat3"),
                Pair(GL_DOUBLE_MAT4, "dmat4"),
                Pair(GL_DOUBLE_MAT2x3, "dmat2x3"),
                Pair(GL_DOUBLE_MAT2x4, "dmat2x4"),
                Pair(GL_DOUBLE_MAT3x2, "dmat3x2"),
                Pair(GL_DOUBLE_MAT3x4, "dmat3x4"),
                Pair(GL_DOUBLE_MAT4x2, "dmat4x2"),
                Pair(GL_DOUBLE_MAT4x3, "dmat4x3"),
                Pair(GL_SAMPLER_1D, "sampler1D"),
                Pair(GL_SAMPLER_2D, "sampler2D"),
                Pair(GL_SAMPLER_3D, "sampler3D"),
                Pair(GL_SAMPLER_CUBE, "samplerCube"),
                Pair(GL_SAMPLER_1D_SHADOW, "sampler1DShadow"),
                Pair(GL_SAMPLER_2D_SHADOW, "sampler2DShadow"),
                Pair(GL_SAMPLER_1D_ARRAY, "sampler1DArray"),
                Pair(GL_SAMPLER_2D_ARRAY, "sampler2DArray"),
                Pair(GL_SAMPLER_1D_ARRAY_SHADOW, "sampler1DArrayShadow"),
                Pair(GL_SAMPLER_2D_ARRAY_SHADOW, "sampler2DArrayShadow"),
                Pair(GL_SAMPLER_2D_MULTISAMPLE, "sampler2DMS"),
                Pair(GL_SAMPLER_2D_MULTISAMPLE_ARRAY, "sampler2DMSArray"),
                Pair(GL_SAMPLER_CUBE_SHADOW, "samplerCubeShadow"),
                Pair(GL_SAMPLER_BUFFER, "samplerBuffer"),
                Pair(GL_SAMPLER_2D_RECT, "sampler2DRect"),
                Pair(GL_SAMPLER_2D_RECT_SHADOW, "sampler2DRectShadow"),
                Pair(GL_INT_SAMPLER_1D, "isampler1D"),
                Pair(GL_INT_SAMPLER_2D, "isampler2D"),
                Pair(GL_INT_SAMPLER_3D, "isampler3D"),
                Pair(GL_INT_SAMPLER_CUBE, "isamplerCube"),
                Pair(GL_INT_SAMPLER_1D_ARRAY, "isampler1DArray"),
                Pair(GL_INT_SAMPLER_2D_ARRAY, "isampler2DArray"),
                Pair(GL_INT_SAMPLER_2D_MULTISAMPLE, "isampler2DMS"),
                Pair(GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, "isampler2DMSArray"),
                Pair(GL_INT_SAMPLER_BUFFER, "isamplerBuffer"),
                Pair(GL_INT_SAMPLER_2D_RECT, "isampler2DRect"),
                Pair(GL_UNSIGNED_INT_SAMPLER_1D, "usampler1D"),
                Pair(GL_UNSIGNED_INT_SAMPLER_2D, "usampler2D"),
                Pair(GL_UNSIGNED_INT_SAMPLER_3D, "usampler3D"),
                Pair(GL_UNSIGNED_INT_SAMPLER_CUBE, "usamplerCube"),
                Pair(GL_UNSIGNED_INT_SAMPLER_1D_ARRAY, "usampler2DArray"),
                Pair(GL_UNSIGNED_INT_SAMPLER_2D_ARRAY, "usampler2DArray"),
                Pair(GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE, "usampler2DMS"),
                Pair(GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, "usampler2DMSArray"),
                Pair(GL_UNSIGNED_INT_SAMPLER_BUFFER, "usamplerBuffer"),
                Pair(GL_UNSIGNED_INT_SAMPLER_2D_RECT, "usampler2DRect"),
                Pair(GL_IMAGE_1D, "image1D"),
                Pair(GL_IMAGE_2D, "image2D"),
                Pair(GL_IMAGE_3D, "image3D"),
                Pair(GL_IMAGE_2D_RECT, "image2DRect"),
                Pair(GL_IMAGE_CUBE, "imageCube"),
                Pair(GL_IMAGE_BUFFER, "imageBuffer"),
                Pair(GL_IMAGE_1D_ARRAY, "image1DArray"),
                Pair(GL_IMAGE_2D_ARRAY, "image2DArray"),
                Pair(GL_IMAGE_2D_MULTISAMPLE, "image2DMS"),
                Pair(GL_IMAGE_2D_MULTISAMPLE_ARRAY, "image2DMSArray"),
                Pair(GL_INT_IMAGE_1D, "iimage1D"),
                Pair(GL_INT_IMAGE_2D, "iimage2D"),
                Pair(GL_INT_IMAGE_3D, "iimage3D"),
                Pair(GL_INT_IMAGE_2D_RECT, "iimage2DRect"),
                Pair(GL_INT_IMAGE_CUBE, "iimageCube"),
                Pair(GL_INT_IMAGE_BUFFER, "iimageBuffer"),
                Pair(GL_INT_IMAGE_1D_ARRAY, "iimage1DArray"),
                Pair(GL_INT_IMAGE_2D_ARRAY, "iimage2DArray"),
                Pair(GL_INT_IMAGE_2D_MULTISAMPLE, "iimage2DMS"),
                Pair(GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY, "iimage2DMSArray"),
                Pair(GL_UNSIGNED_INT_IMAGE_1D, "uimage1D"),
                Pair(GL_UNSIGNED_INT_IMAGE_2D, "uimage2D"),
                Pair(GL_UNSIGNED_INT_IMAGE_3D, "uimage3D"),
                Pair(GL_UNSIGNED_INT_IMAGE_2D_RECT, "uimage2DRect"),
                Pair(GL_UNSIGNED_INT_IMAGE_CUBE, "uimageCube"),
                Pair(GL_UNSIGNED_INT_IMAGE_BUFFER, "uimageBuffer"),
                Pair(GL_UNSIGNED_INT_IMAGE_1D_ARRAY, "uimage1DArray"),
                Pair(GL_UNSIGNED_INT_IMAGE_2D_ARRAY, "uimage2DArray"),
                Pair(GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE, "uimage2DMS"),
                Pair(GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY, "uimage2DMSArray"),
                Pair(GL_UNSIGNED_INT_ATOMIC_COUNTER, "atomic_uint")
            )
        }
    }
}