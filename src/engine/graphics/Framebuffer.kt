package engine.graphics

import engine.Window
import engine.util.IUsable
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL46
import java.nio.ByteBuffer

class Framebuffer : IUsable {
    val buffer = glGenFramebuffers()
    var size: Vector2ic = Vector2i(Window.size)

    init {
        glBindFramebuffer(GL_FRAMEBUFFER, buffer)
    }

    var texture = Texture2D(size).also {
        it.fillWithNull()
        it.makeLinear()
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, it.texture, 0)
    }

    var depth = Texture2D(size).also {
        it.fillWithZeroDepth()
        it.makeLinear()
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, it.texture, 0)
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
        if (Window.size != size) {
            size = Vector2i(Window.size)
            generateTextures()
        }
        glBindFramebuffer(GL_FRAMEBUFFER, buffer)
        callback()
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }
}