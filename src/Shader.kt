import org.lwjgl.opengl.GL20
import java.io.File
import org.lwjgl.opengl.GL46.*
import java.time.LocalDateTime

class Shader(pathFrag: String, pathVert: String) {
    public val program: Int
    init {
        val vs = compileShader(pathVert, GL_VERTEX_SHADER)
        val fs = compileShader(pathFrag, GL_FRAGMENT_SHADER)
        program = glCreateProgram()
        glAttachShader(program, vs)
        glAttachShader(program, fs)
        glBindFragDataLocation(program,0, "color")
        glLinkProgram(program)
    }

    fun bind() {
        glUseProgram(program)
        glUniform1f(glGetUniformLocation(program, "time"), time)
    }

    private fun compileShader(path: String, type: Int): Int {
        val code = File(path).readText()
        val shader = glCreateShader(type)
        glShaderSource(shader,code)
        glCompileShader(shader)
        val status = intArrayOf(0)
        glGetShaderiv(shader, GL_COMPILE_STATUS, status)
        if(status[0] == GL_FALSE) {
            error(glGetShaderInfoLog(shader))
        }
        return shader
    }
}