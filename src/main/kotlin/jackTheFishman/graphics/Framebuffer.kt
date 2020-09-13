package jackTheFishman.graphics

import jackTheFishman.util.Usable
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL46
import java.nio.ByteBuffer

class Framebuffer : Usable {
    val buffer = glGenFramebuffers()
    var size: Vector2ic = Vector2i(0, 0)

    init {
        glBindFramebuffer(GL_FRAMEBUFFER, buffer)
    }

    var texture = Texture2D().also {
        it.fillWithNull(size)
        it.makeLinear()
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, it.pointer, 0)
    }

    var depth = Texture2D().also {
        it.fillWithZeroDepth(size)
        it.makeLinear()
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, it.pointer, 0)
    }

    init {
        check(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) { "gl framebuffer not complete" }
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    private fun generateTextures() {
        texture.use {
            val n: ByteBuffer? = null
            GL46.glTexImage2D(
                GL46.GL_TEXTURE_2D,
                0,
                GL46.GL_RGBA,
                size.x(),
                size.y(),
                0,
                GL46.GL_RGBA,
                GL46.GL_UNSIGNED_BYTE,
                n
            )
        }
        depth.use {
            val n: ByteBuffer? = null
            GL46.glTexImage2D(
                GL46.GL_TEXTURE_2D,
                0,
                GL46.GL_DEPTH24_STENCIL8,
                size.x(),
                size.y(),
                0,
                GL46.GL_DEPTH_STENCIL,
                GL46.GL_UNSIGNED_INT_24_8,
                n
            )
        }
    }

    override fun use(callback: () -> Unit) {
        // if (GlfwWindow.physicalSize != size) {
            // size = Vector2i(GlfwWindow.physicalSize)
            // generateTextures()
        // }
        glBindFramebuffer(GL_FRAMEBUFFER, buffer)
        callback()
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }
}
