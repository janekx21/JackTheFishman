package dodgyDeliveries3.components

import org.joml.Vector2f
import org.liquidengine.legui.component.Slider

open class Slider(var onChanged: (value: Float) -> Unit = {}) : LeguiComponentWrapper<Slider>(
    Slider().also {
        it.position = Vector2f(8F, 8F)
        it.sliderSize = 50f
    }
) {

    private var lastValue = 0f

    override fun update() {
        super.update()
        if (leguiComponent.value != lastValue) {
            onChanged(leguiComponent.value)
            lastValue = leguiComponent.value
        }
    }
}
