package graphics

import IBindable
import org.lwjgl.opengl.GL46

class Framebuffer : IBindable {
    private val fbo = GL46.glGenFramebuffers()

    companion object {
        private var bound = false
    }

    override fun bind(callback: () -> Unit) {
        check(!bound) { "you cant bind two Framebuffer" }
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, fbo)
        bound = true
        callback.invoke()
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0)
        bound = false
    }
}