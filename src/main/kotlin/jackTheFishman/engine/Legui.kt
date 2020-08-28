package jackTheFishman.engine

import jackTheFishman.engine.util.IDrawable
import org.liquidengine.legui.DefaultInitializer
import org.liquidengine.legui.animation.AnimatorProvider
import org.liquidengine.legui.component.Frame
import org.liquidengine.legui.system.layout.LayoutManager

object Legui : IDrawable {
    val frame: Frame = Frame(Window.physicalSize.x().toFloat(), Window.physicalSize.y().toFloat())
    val initializer: DefaultInitializer

    init {
        initializer = DefaultInitializer(Window.pointer, frame)
        initializer.renderer.initialize()
    }

    fun update() {
        initializer.systemEventProcessor.processEvents(frame, initializer.context)
        initializer.guiEventProcessor.processEvents()
        AnimatorProvider.getAnimator().runAnimations()
    }

    override fun draw() {
        initializer.context.updateGlfwWindow()
        LayoutManager.getInstance().layout(frame)
        initializer.renderer.render(frame, initializer.context)
    }

    fun destroy() {
        initializer.renderer.destroy()
    }
}
