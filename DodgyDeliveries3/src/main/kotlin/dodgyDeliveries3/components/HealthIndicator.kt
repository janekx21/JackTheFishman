package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Window
import jackTheFishman.engine.graphics.Texture2D
import org.joml.Vector2f
import org.joml.Vector4f
import kotlin.math.roundToInt

class HealthIndicator : Component() {
    private val images: List<Texture2D> = (0 .. 5).map {
        Loader.createViaPath<Texture2D>("textures/healthIndication/pizza$it.png")
    }

    private val image : Texture2D?
        get() {
            if (healthComponent == null) {
                return null
            } else {
                val imageIndex = (images.size - 1) - ((images.size - 1) * healthComponent!!.percentage).roundToInt()
                return images[imageIndex]
            }
        }

    private val healthComponent: Health?
        get() = Scene.active.findViaName("Player")?.getComponents<Health>()?.singleOrNull()

    private val imageComponent: ImageComponent = ImageComponent().also {
        if (image != null) {
            it.texture = image
        }
        it.leguiComponent.style.also { style ->
            style.background.color = Vector4f(0f, 0f, 0f, 0f)
            style.border.isEnabled = false
            style.shadow.color = Vector4f(0f, 0f, 0f, 0f)
        }
        it.onSizeChange = {
            it.logicalSize = Vector2f(Window.logicalSize.x() * 0.1f, Window.logicalSize.x() * 0.1f)
            it.logicalPosition = Vector2f(Window.logicalSize.x() * 0.05f, Window.logicalSize.y() * 0.75f)
        }
    }

    override fun setOrigin(origin: GameObject) {
        super.setOrigin(origin)
        origin.addComponent(imageComponent)
    }

    override fun update() {
        updateImage()
    }

    private fun updateImage() {
        imageComponent.texture = image
    }
}
