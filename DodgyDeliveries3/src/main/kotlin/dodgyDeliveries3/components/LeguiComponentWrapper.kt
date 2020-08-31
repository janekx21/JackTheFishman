package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector2ic

open class LeguiComponentWrapper<T>(
    leguiComponent: T,
    var onPressed: () -> Unit = {},
    var onSizeChange: (LeguiComponentWrapper<T>) -> Unit = {}
) : Component() where T : org.liquidengine.legui.component.Component {
    private var isEnabled: Boolean = false
    private var isAddedToScene: Boolean = false
    private var physicalSizeLastFrame: Vector2ic = Window.physicalSize

    var leguiComponent: T = leguiComponent
        set(value) {
            if (isAddedToScene) {
                removeFromScene()
            }

            wasPressedLastUpdate = false
            field = value

            if (isEnabled) {
                addToScene()
            }
        }

    var physicalPosition: Vector2fc
        get() = Vector2f(leguiComponent.position)
        set(value) {
            leguiComponent.position = Vector2f(value)
        }

    var logicalPosition: Vector2fc
        get() = Vector2f(physicalPosition).div(Window.contentScale)
        set(value) {
            physicalPosition = Vector2f(value).mul(Window.contentScale)
        }

    /**
     * @brief Size of the LeguiComponent in Pixels
     */
    var physicalSize: Vector2fc
        get() = Vector2f(leguiComponent.size)
        set(value) {
            leguiComponent.size = Vector2f(value)
        }

    /**
     * @brief Size of the LeguiComponent in logical pixels, i.e.
     * physicalSize = logicalSize * Window.contentScale
     */
    var logicalSize: Vector2fc
        get() = Vector2f(physicalSize).div(Window.contentScale)
        set(value) {
            physicalSize = Vector2f(value).mul(Window.contentScale)
        }

    private var wasPressedLastUpdate = false

    private fun onSizeChange() {
        onSizeChange(this)
    }

    private fun addToScene() {
        check(isEnabled)

        onSizeChange()
        Scene.active.rootPanel.add(leguiComponent)
        isAddedToScene = true
    }

    private fun removeFromScene() {
        check(isAddedToScene)

        Scene.active.rootPanel.remove(leguiComponent)
    }

    override fun start() {
        super.start()
        isEnabled = true
        addToScene()
    }

    override fun stop() {
        removeFromScene()
        isEnabled = false
        super.stop()
    }

    override fun update() {
        if (Window.physicalSize != physicalSizeLastFrame) {
            onSizeChange()
            physicalSizeLastFrame = Window.physicalSize
        }

        if (leguiComponent.isPressed && !wasPressedLastUpdate) {
            onPressed()
            wasPressedLastUpdate = true
        } else if (!leguiComponent.isPressed && wasPressedLastUpdate) {
            wasPressedLastUpdate = false
        }
    }

    override fun draw() {
    }
}
